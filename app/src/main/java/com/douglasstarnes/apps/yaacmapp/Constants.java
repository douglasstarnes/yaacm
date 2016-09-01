package com.douglasstarnes.apps.yaacmapp;


public class Constants {
    public static final String HEADER_X_PARSE_APPLICATION_ID = "X-Parse-Application-ID: <!-- your key here -->";
    public static final String PARSE_BASE_URL = "http://your-parse-server.herokuapp.com/parse/";
    public static final String ZIP_CODE_API_KEY = " <!-- your key here -->";
    public static final String ZIP_CODE_API_BASE_URL = "https://www.zipcodeapi.com/rest/" + Constants.ZIP_CODE_API_KEY + "/info.json/";
    public static final String SELECTED_CONTACT_KEY = "SELECTED_CONTACT_KEY";
    public static final String CONTACT_SERVICE_SEARCH_QUERY_TEMPLATE = "{\"%s\":{\"$regex\":\"^%s\"}}";
    public static final String CONTACT_SERVICE_FAVORITES_QUERY_TEMPLATE = "{\"favorite\":true}";
    public static final String CONTACT_SEARCH_FIELD_CITY = "city";
    public static final String CONTACT_SEARCH_FIELD_FIRST_NAME = "firstName";
    public static final String CONTACT_SEARCH_FIELD_LAST_NAME = "lastName";
    public static final String CONTACT_SEARCH_FIELD_STATE = "state";
    public static final String[] CONTACT_SEARCH_FIELDS = new String[] {
            CONTACT_SEARCH_FIELD_CITY, CONTACT_SEARCH_FIELD_FIRST_NAME, CONTACT_SEARCH_FIELD_LAST_NAME, CONTACT_SEARCH_FIELD_STATE
    };
    public static final String SUPPRESS_DELETE_WARNING_KEY = "SUPPRESS_DELETE_WARNING_KEY";
    public static final String YAACM_PREFERENCES_KEY = "YAACM_PREFERENCES_KEY";
}
