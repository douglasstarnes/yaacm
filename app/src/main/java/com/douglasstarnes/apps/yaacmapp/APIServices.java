package com.douglasstarnes.apps.yaacmapp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServices {
    // set the timeout to 60 seconds
    // this is set to be long to make up for the free Heroku instance sleeping after 30 seconds
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    // the builders to create the services that talk to the REST APIs
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

    // the services that translater between Java and HTTP/REST
    public static YAACMService YAACM_SERVICE = APIServices.parseAPIBuilder.create(YAACMService.class);
    public static ZipCodeService ZIP_CODE_SERVICE = APIServices.zipCodeAPIBuilder.create(ZipCodeService.class);
}
