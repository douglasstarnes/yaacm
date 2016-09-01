package com.douglasstarnes.apps.yaacmapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class YAACMListAdapter extends ArrayAdapter<YAACMContact> {
    List<YAACMContact> contacts;

    public YAACMListAdapter(Context context, int resource, List<YAACMContact> objects) {
        super(context, resource, objects);
        contacts = objects;
    }

    public void refresh(List<YAACMContact> contacts) {
        this.contacts.clear();
        this.contacts.addAll(contacts);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder contactHolder;

        if (row == null) {
            LayoutInflater inflater = ((MainActivity)getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.yaacm_list_row_layout, parent, false);

            contactHolder = new ContactHolder();
            contactHolder.tvFullName = (TextView)row.findViewById(R.id.text_view_list_full_name);
            contactHolder.tvLocation = (TextView)row.findViewById(R.id.text_view_list_location);
            contactHolder.ivFavorite = (ImageView)row.findViewById(R.id.image_view_favorite);

            row.setTag(contactHolder);
        } else {
            contactHolder = (ContactHolder)row.getTag();
        }

        YAACMContact contact = contacts.get(position);
        contactHolder.tvFullName.setText(contact.getFirstName() + " " + contact.getLastName());
        contactHolder.tvLocation.setText(contact.getCity() + ", " + contact.getState());
        if (contact.isFavorite()) {
            contactHolder.ivFavorite.setImageResource(R.drawable.star_filled);
        } else {
            contactHolder.ivFavorite.setImageResource(R.drawable.star);
        }

        return row;
    }

    static class ContactHolder {
        TextView tvFullName;
        TextView tvLocation;
        ImageView ivFavorite;
    }
}
