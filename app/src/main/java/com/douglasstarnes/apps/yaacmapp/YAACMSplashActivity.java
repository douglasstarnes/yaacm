package com.douglasstarnes.apps.yaacmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class YAACMSplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this is an ugly hack and something I would never do in production.   I am using
        // a free service to host the Parse server (Heroku) which goes to sleep after 30 seconds of
        // inactivity.  Therefore I am making a call during the splash screen to warm up the application
        // to help prevent any timeouts later on.
        Call<YAACMContactList> dummy = APIServices.YAACM_SERVICE.listContacts();
        dummy.enqueue(new Callback<YAACMContactList>() {
            @Override
            public void onResponse(Call<YAACMContactList> call, Response<YAACMContactList> response) {
                Intent intent = new Intent(YAACMSplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<YAACMContactList> call, Throwable t) {
                Intent intent = new Intent(YAACMSplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
