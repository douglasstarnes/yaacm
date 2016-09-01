package com.douglasstarnes.apps.yaacmapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// this is *very* similar to the AddFragment
// see the comments in that class for details
public class EditFragment extends Fragment {
    private boolean markOverflow = true;
    private YAACMContact selectedContact = null;

    @BindView(R.id.edit_text_first_name)
    EditText etFirstName;

    @BindView(R.id.edit_text_last_name)
    EditText etLastName;

    @BindView(R.id.edit_text_city)
    EditText etCity;

    @BindView(R.id.edit_text_state)
    EditText etState;

    @BindView(R.id.edit_text_zip_code)
    EditText etZipCode;

    @BindView(R.id.edit_text_dob_month)
    EditText etDobMonth;

    @BindView(R.id.edit_text_dob_day)
    EditText etDobDay;

    @BindView(R.id.edit_text_dob_year)
    EditText etDobYear;

    @BindView(R.id.edit_text_comment)
    EditText etComment;

    @BindView(R.id.checkbox_favorite)
    CheckBox cbFavorite;

    @BindView(R.id.text_view_remaining)
    TextView tvRemaining;

    @BindView(R.id.button_add)
    Button btnAdd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_fragment, container, false);
        ButterKnife.bind(this, root);

        // get the selected contact from the list fragment
        Bundle bundle = this.getArguments();
        selectedContact = (YAACMContact)bundle.getParcelable(Constants.SELECTED_CONTACT_KEY);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedContact.getDob());
        String dobMonth = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String dobDay = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String dobYear = Integer.toString(calendar.get(Calendar.YEAR));

        etFirstName.setText(selectedContact.getFirstName());
        etLastName.setText(selectedContact.getLastName());
        etCity.setText(selectedContact.getCity());
        etState.setText(selectedContact.getState());
        etZipCode.setText(selectedContact.getZipCode());
        etDobMonth.setText(dobMonth);
        etDobDay.setText(dobDay);
        etDobYear.setText(dobYear);
        etComment.setText(selectedContact.getComments());
        cbFavorite.setChecked(selectedContact.isFavorite());

        // reusing the layout for the edit fragment
        // need to change the positive button text which is "Add" in the layout file
        btnAdd.setText("Update");

        return root;
    }

    @OnClick(R.id.button_add)
    protected void btnAddClick(final View view) {
        dismissKeyboard();

        int dobDay = Integer.parseInt(etDobDay.getText().toString());
        int dobMonth = Integer.parseInt(etDobMonth.getText().toString()) - 1;
        int dobYear = Integer.parseInt(etDobYear.getText().toString());

        if (Utils.validateDateValues(dobDay, dobMonth, dobYear)) {
            Call<Void> newContact = APIServices.YAACM_SERVICE.updateContact(selectedContact.getObjectId(), new YAACMContactDTO(
                    etCity.getText().toString(),
                    etComment.getText().toString(),
                    dobDay,
                    dobMonth,
                    dobYear,
                    cbFavorite.isChecked(),
                    etFirstName.getText().toString(),
                    etLastName.getText().toString(),
                    etState.getText().toString(),
                    etZipCode.getText().toString()
            ));
            newContact.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Snackbar.make(view, "Contact updated", Snackbar.LENGTH_LONG)
                            .setAction("Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dismissKeyboard();
                                    if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                                    }
                                }
                            })
                            .show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                    .setTitle("Error")
                    .setMessage("The values for Date of Birth are invalid")
                    .setCancelable(false)
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            etDobMonth.requestFocus();
                        }
                    });
            alert.show();
        }

    }

    @OnClick(R.id.button_cancel)
    protected void btnCancelClick(View View) {
        dismissKeyboard();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @OnTextChanged(R.id.edit_text_comment)
    protected void commentTextChanged(Editable editable) {
        String text = editable.toString();
        int remaining = 255 - text.length();
        if (remaining < 0) {
            if (markOverflow) {
                markOverflow = false;
                tvRemaining.setTextColor(Color.RED);
                SpannableString builder = new SpannableString(text);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
                builder.setSpan(colorSpan, 255, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                int cursorPosition = etComment.getSelectionEnd();
                etComment.setText(builder);
                etComment.setSelection(cursorPosition);
            } else {
                markOverflow = true;
            }
            btnAdd.setEnabled(false);
        } else if (remaining < 27) {
            tvRemaining.setTextColor(Color.argb(255, 223, 203, 0));
            btnAdd.setEnabled(true);
        } else {
            tvRemaining.setTextColor(Color.BLACK);
        }
        tvRemaining.setText(Integer.toString(remaining));
    }

    @OnFocusChange(R.id.edit_text_zip_code)
    protected void zipCodeFocusChanged(View view, boolean hasFocus) {
        if (!hasFocus) {
            Call<YAACMZipCode> zipCodeLookup = APIServices.ZIP_CODE_SERVICE.lookupZipCode(etZipCode.getText().toString());
            zipCodeLookup.enqueue(new Callback<YAACMZipCode>() {
                @Override
                public void onResponse(Call<YAACMZipCode> call, Response<YAACMZipCode> response) {
                    YAACMZipCode zipCode = response.body();
                    if (zipCode != null) {
                        etCity.setText(zipCode.getCity());
                        etState.setText(zipCode.getState());
                    } else {
                        dismissKeyboard();
                        Context ctx = getContext();
                        if (ctx != null) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(ctx)
                                    .setTitle("Error")
                                    .setMessage("The value for Zip Code is invalid")
                                    .setCancelable(false)
                                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // clear the invalid zip code
                                            etZipCode.setText("");
                                            // return focus to the zip code
                                            etZipCode.requestFocus();
                                        }
                                    });
                            alert.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<YAACMZipCode> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void dismissKeyboard() {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(btnAdd.getWindowToken(), 0);
        }
    }
}
