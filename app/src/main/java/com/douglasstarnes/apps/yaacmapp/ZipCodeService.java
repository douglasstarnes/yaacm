package com.douglasstarnes.apps.yaacmapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ZipCodeService {
    @GET("{zipCode}/radians")
    Call<YAACMZipCode> lookupZipCode(@Path("zipCode") String zipCode);
}
