package com.douglasstarnes.apps.yaacmapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface YAACMService {
    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @GET("classes/YAACMContact?order=lastName")
    Call<YAACMContactList> listContacts();

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @POST("classes/YAACMContact?order=lastName")
    Call<Void> createContact(@Body YAACMContactDTO yaacmContactDTO);

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @DELETE("classes/YAACMContact/{objectId}")
    Call<Void> deleteContact(@Path("objectId") String objectId);

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @GET("classes/YAACMContact?order=lastName")
    Call<YAACMContactList> searchContacts(@Query("where") String where);

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @PUT("classes/YAACMContact/{objectId}")
    Call<Void> updateContact(@Path("objectId") String objectId, @Body YAACMContactDTO yaacmContactDTO);

}
