package com.douglasstarnes.apps.yaacmapp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class APIServices {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    private static Retrofit parseAPIBuilder = new Retrofit.Builder()
            .baseUrl(Constants.PARSE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    private static Retrofit zipCodeAPIBuilder = new Retrofit.Builder()
            .baseUrl(Constants.ZIP_CODE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    public static YAACMService YAACM_SERVICE = APIServices.parseAPIBuilder.create(YAACMService.class);
    public static ZipCodeService ZIP_CODE_SERVICE = APIServices.zipCodeAPIBuilder.create(ZipCodeService.class);
}
