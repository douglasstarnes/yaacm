package com.douglasstarnes.apps.yaacmapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class DetailsFragment extends Fragment {
    @BindView(R.id.details_full_name)
    TextView tvFullName;

    @BindView(R.id.details_location)
    TextView tvLocation;

    @BindView(R.id.details_zip_code)
    TextView tvZipCode;

    @BindView(R.id.details_birthdate)
    TextView tvBirthdate;

    @BindView(R.id.details_comment)
    TextView tvComment;

    @BindView(R.id.details_favorite)
    TextView tvFavorite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.details_fragment, container, false);
        ButterKnife.bind(this, root);

        // get the selected contact from the list fragment
        Bundle bundle = this.getArguments();
        YAACMContact contact = (YAACMContact)bundle.getParcelable(Constants.SELECTED_CONTACT_KEY);

        // populate the details fragment
        String fullName = contact.getFirstName() + " " + contact.getLastName();
        String location = contact.getCity() + ", " + contact.getState();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM. d, yyyy", Locale.ENGLISH);

        tvFullName.setText(fullName);
        tvLocation.setText(location);
        tvZipCode.setText(contact.getZipCode());
        tvBirthdate.setText(simpleDateFormat.format(contact.getDob()));
        tvComment.setText(contact.getComments());

        if (contact.isFavorite()) {
            tvFavorite.setText("Is a Favorite");
            tvFavorite.setTextColor(Color.BLUE);
            tvFavorite.setTypeface(null, Typeface.BOLD);
        } else {
            tvFavorite.setText("Is not a favorite");
            tvFavorite.setTextColor(Color.RED);
            tvFavorite.setTypeface(null, Typeface.ITALIC);
        }

        return root;
    }
}
