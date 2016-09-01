package com.douglasstarnes.apps.yaacmapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YAACMListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private ArrayList<YAACMContact> contacts;

    @BindView(R.id.checkbox_suppress_delete_warning)
    CheckBox cbSuppressDeleteWarning;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
        contacts = new ArrayList<>();
        YAACMListAdapter adapter = new YAACMListAdapter(getContext(), 0, contacts);
        setListAdapter(adapter);
        refreshItems();
    }

    private void refreshItems() {
        Call<YAACMContactList> listCall = APIServices.YAACM_SERVICE.listContacts();

        listCall.enqueue(new Callback<YAACMContactList>() {
            @Override
            public void onResponse(Call<YAACMContactList> call, Response<YAACMContactList> response) {
                YAACMContactList contactList = response.body();
                if (contactList.results.size() > 0) {
                    contacts.clear();
                    contacts.addAll(contactList.results);
                } else {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View emptyView = inflater.inflate(R.layout.list_empty_view, null);
                    ((ViewGroup)getListView().getParent()).addView(emptyView, 0);
                }
                ((BaseAdapter)getListAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<YAACMContactList> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // contact to show in the details fragment
        YAACMContact selectedContact = (YAACMContact)getListAdapter().getItem(i);
        Fragment detailsFragment = new DetailsFragment();
        // bundle to hold the selected contacts
        Bundle bundle = new Bundle();
        // convert selected contact to Parcelable
        bundle.putParcelable(Constants.SELECTED_CONTACT_KEY, selectedContact);
        // attach bundle to fragment
        detailsFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.list_context_menu_delete:
                LayoutInflater inflater = getActivity().getLayoutInflater();
                // delete uses a custom dialog with a checkbox to suppress the delete warning
                View deleteDialog = inflater.inflate(R.layout.delete_dialog_layout, null);

                ButterKnife.bind(this, deleteDialog);

                // when the checkbox in the delete dialog is selected, it sets a key
                // in the preferences to true
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences(Constants.YAACM_PREFERENCES_KEY, Context.MODE_PRIVATE);

                // if the key is false, the warning is not suppressed so show build and show the dialog
                if (!sharedPrefs.getBoolean(Constants.SUPPRESS_DELETE_WARNING_KEY, false)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Confirm Delete");
                    alert.setView(deleteDialog);
                    alert.setCancelable(false);
                    // should the checkbox be considered with the negative button?
                    // not sure, depends on the situation but I left it in for the demo
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (cbSuppressDeleteWarning.isChecked()) suppressDeleteWarning();
                        }
                    });
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (cbSuppressDeleteWarning.isChecked()) suppressDeleteWarning();
                            YAACMContact selectedContact = (YAACMContact) getListAdapter().getItem(menuInfo.position);
                            deleteContact(selectedContact);
                        }
                    });
                    alert.show();
                } else {
                    // otherwise just perform the delete
                    YAACMContact selectedContact = (YAACMContact) getListAdapter().getItem(menuInfo.position);
                    deleteContact(selectedContact);
                }
                return true;
            case R.id.list_context_menu_edit:
                // similar to the details fragment
                // covering the selected contact (via the menuInfo data) to a Parcelable
                // and then putting it in a Bundle for the edit fragment
                YAACMContact selectedContact = (YAACMContact)getListAdapter().getItem(menuInfo.position);
                Fragment editFragment = new EditFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.SELECTED_CONTACT_KEY, selectedContact);
                editFragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, editFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteContact(YAACMContact selectedContact) {
        Call<Void> deleteCall = APIServices.YAACM_SERVICE.deleteContact(selectedContact.getObjectId());
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getActivity(), "Contact deleted", Toast.LENGTH_SHORT).show();
                refreshItems();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // set the delete warning key in the preferences
    // from this fragment, it can only be set to true
    private void suppressDeleteWarning() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.YAACM_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SUPPRESS_DELETE_WARNING_KEY, true);
        editor.commit();
    }
}
