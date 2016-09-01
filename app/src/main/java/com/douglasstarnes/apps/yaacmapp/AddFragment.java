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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFragment extends Fragment {
    private boolean markOverflow = true;

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
        return root;
    }

    @OnClick(R.id.button_add)
    protected void btnAddClick(final View view) {
        dismissKeyboard();

        int dobDay = Integer.parseInt(etDobDay.getText().toString());
        int dobMonth = Integer.parseInt(etDobMonth.getText().toString()) - 1;
        int dobYear = Integer.parseInt(etDobYear.getText().toString());

        if (Utils.validateDateValues(dobDay, dobMonth, dobYear)) {
            // create a DTO to send data to Parse Server
            // this will be converted to JSON in the request body
            Call<Void> newContact = APIServices.YAACM_SERVICE.createContact(new YAACMContactDTO(
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
            // enqueue the call to avoid the NetworkOnMainThreadException
            newContact.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    // snackbar is more subtle than a dialog as it will disappear
                    // after a few seconds but unlike a toast, gives the user
                    // the option to take action as well
                    Snackbar.make(view, "Contact added", Snackbar.LENGTH_LONG)
                            .setAction("back", new View.OnClickListener() {
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
            // determining which date component causes the error is beyond the scope of the demo
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
    // on cancel just go back to the previous fragment
    // for demo purposes this is enough
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
            // prevent an infinite loop
            if (markOverflow) {
                markOverflow = false;
                tvRemaining.setTextColor(Color.RED);
                SpannableString builder = new SpannableString(text);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
                builder.setSpan(colorSpan, 255, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                // save the current cursor position
                int cursorPosition = etComment.getSelectionEnd();
                // setText() will put the cursor at the beginning of the EditText
                etComment.setText(builder);
                // restore the cursor position
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
    // when the zip code field loses focus, check with zipcodeapi.com to see if the value
    // is a valid zip code and if so, update the city and state fields in the fragment
    protected void zipCodeFocusChanged(View view, boolean hasFocus) {
        if (!hasFocus) {
            Call<YAACMZipCode> zipCodeLookup = APIServices.ZIP_CODE_SERVICE.lookupZipCode(etZipCode.getText().toString());
            zipCodeLookup.enqueue(new Callback<YAACMZipCode>() {
                @Override
                public void onResponse(Call<YAACMZipCode> call, Response<YAACMZipCode> response) {
                    YAACMZipCode zipCode = response.body();
                    // valid zip code
                    if (zipCode != null) {
                        etCity.setText(zipCode.getCity());
                        etState.setText(zipCode.getState());
                    } else {
                        dismissKeyboard();
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
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

                @Override
                public void onFailure(Call<YAACMZipCode> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnAdd.getWindowToken(), 0);
    }
}
