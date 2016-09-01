package com.douglasstarnes.apps.yaacmapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class PreferencesFragment extends Fragment {
    @BindView(R.id.switch_suppress_delete_warning)
    Switch swSuppressDeleteWarning;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.preferences_layout, container, false);
        ButterKnife.bind(this, root);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.YAACM_PREFERENCES_KEY, Context.MODE_PRIVATE);
        swSuppressDeleteWarning.setChecked(sharedPreferences.getBoolean(Constants.SUPPRESS_DELETE_WARNING_KEY, false));

        return root;
    }

    @OnCheckedChanged(R.id.switch_suppress_delete_warning)
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.YAACM_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SUPPRESS_DELETE_WARNING_KEY, b);
        editor.commit();
    }
}
