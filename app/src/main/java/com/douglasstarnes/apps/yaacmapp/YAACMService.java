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
    // all Parse Server calls require a header with the application id
    // this is set during the Parse Server installation on heroku
    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    // the URL is appended to the BASE_URL in the constants
    // this one gets a list of all the contacts
    // in a production situation this would be limited and paged
    @GET("classes/YAACMContact?order=lastName")
    // The generic type will be returned in the body of the Retrofit response
    Call<YAACMContactList> listContacts();

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    // add a new contact
    @POST("classes/YAACMContact")
    // the Void generic type will suppress parsing any JSON response from the API
    // for demo purposes, assume that everything succeeds
    // the @Body annotation will convert the fields of the DTO (just a class of public fields
    // representing to keys in a JSON object) to JSON and put them in the body
    // of the HTTP request
    Call<Void> createContact(@Body YAACMContactDTO yaacmContactDTO);

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @DELETE("classes/YAACMContact/{objectId}")
    // the @Path annotation will replace the field in curly braces in the URL with the value
    // of the parameter it annotates
    Call<Void> deleteContact(@Path("objectId") String objectId);

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @GET("classes/YAACMContact?order=lastName")
    // the @Query annotation will append the value of the annotated parameter to the URL query string
    // with the key passed to the annotation
    // this is used because the search query for Parse Server contains curly braces and using
    // a replacement field like with deleteContact() would confuse Retrofit
    Call<YAACMContactList> searchContacts(@Query("where") String where);

    @Headers(Constants.HEADER_X_PARSE_APPLICATION_ID)
    @PUT("classes/YAACMContact/{objectId}")
    Call<Void> updateContact(@Path("objectId") String objectId, @Body YAACMContactDTO yaacmContactDTO);

}
