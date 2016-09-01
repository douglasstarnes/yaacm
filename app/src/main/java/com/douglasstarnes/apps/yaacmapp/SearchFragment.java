package com.douglasstarnes.apps.yaacmapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<YAACMContact> contacts;

    @BindView(R.id.edit_text_search_term)
    EditText etSearchTerm;

    @BindView(R.id.list_view_search)
    ListView lvSearch;

    @BindView(R.id.spinner_search_field)
    Spinner spSearchField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, root);

        ArrayAdapter<String> spinnerAdapater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getSearchFields());
        spinnerAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSearchField.setAdapter(spinnerAdapater);

        contacts = new ArrayList<>();
        YAACMListAdapter listAdapter = new YAACMListAdapter(getContext(), 0, contacts);
        lvSearch.setAdapter(listAdapter);
        // lvSearch.setOnItemClickListener(this);

        return root;
    }

    private List<String> getSearchFields() {
        List<String> searchFields = new ArrayList<>();
        searchFields.add("Select Search Field");
        searchFields.add("City");
        searchFields.add("First Name");
        searchFields.add("Last Name");
        searchFields.add("State");

        return searchFields;
    }

    @OnClick(R.id.button_search_bar)
    public void btnSearchClick(View view) {
        dismissKeyboard();
        if (spSearchField.getSelectedItemPosition() > 0) {
            String searchField = Constants.CONTACT_SEARCH_FIELDS[spSearchField.getSelectedItemPosition() - 1];
            String searchTerm = etSearchTerm.getText().toString();
            String query = String.format(Constants.CONTACT_SERVICE_SEARCH_QUERY_TEMPLATE, searchField, searchTerm);

            Call<YAACMContactList> searchCall = APIServices.YAACM_SERVICE.searchContacts(query);
            searchCall.enqueue(new Callback<YAACMContactList>() {
                @Override
                public void onResponse(Call<YAACMContactList> call, Response<YAACMContactList> response) {
                    if (response.body().results.size() > 0) {
                        contacts.clear();
                        contacts.addAll(response.body().results);
                    } else {
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View emptyView = inflater.inflate(R.layout.list_empty_view, null);
                        ((ViewGroup)lvSearch.getParent()).addView(emptyView, 2);
                    }
                    ((BaseAdapter) lvSearch.getAdapter()).notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<YAACMContactList> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        YAACMContact selectedContact = (YAACMContact)lvSearch.getAdapter().getItem(i);
        Fragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.SELECTED_CONTACT_KEY, selectedContact);
        detailsFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearchTerm.getWindowToken(), 0);
    }
}
