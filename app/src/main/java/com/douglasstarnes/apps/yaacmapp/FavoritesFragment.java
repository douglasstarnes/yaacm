package com.douglasstarnes.apps.yaacmapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesFragment extends ListFragment implements AdapterView.OnItemClickListener{
    private ArrayList<YAACMContact> contacts;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemClickListener(this);
        contacts = new ArrayList<>();
        YAACMListAdapter adapter = new YAACMListAdapter(getContext(), 0, contacts);
        setListAdapter(adapter);
        refreshItems();
    }

    private void refreshItems() {
        // reuse the search method here with a different query template
        Call<YAACMContactList> listFavorites = APIServices.YAACM_SERVICE.searchContacts(Constants.CONTACT_SERVICE_FAVORITES_QUERY_TEMPLATE);

        listFavorites.enqueue(new Callback<YAACMContactList>() {
            @Override
            public void onResponse(Call<YAACMContactList> call, Response<YAACMContactList> response) {
                YAACMContactList contactList = response.body();
                if (contactList.results.size() > 0) {
                    contacts.clear();
                    contacts.addAll(contactList.results);
                } else {
                    // use empty view if no results
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
        YAACMContact selectedContact = (YAACMContact)getListAdapter().getItem(i);
        Fragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.SELECTED_CONTACT_KEY, selectedContact);
        detailsFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, detailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
