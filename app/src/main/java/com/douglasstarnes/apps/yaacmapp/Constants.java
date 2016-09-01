package com.douglasstarnes.apps.yaacmapp;


public class Constants {
    // Parse Server installation on Heroku via https://devcenter.heroku.com/articles/deploying-a-parse-server-to-heroku
    public static final String HEADER_X_PARSE_APPLICATION_ID = "X-Parse-Application-ID: <!-- your parse server application id here -->";
    public static final String PARSE_BASE_URL = "http://your-heroku-app-subdomain.herokuapp.com/parse/";

    // http://www.zipcodeapi.com/
    public static final String ZIP_CODE_API_KEY = "<!-- your zipcodeapi.com key here -->";
    public static final String ZIP_CODE_API_BASE_URL = "https://www.zipcodeapi.com/rest/" + Constants.ZIP_CODE_API_KEY + "/info.json/";

    // used as the key to send Parcelables between fragments
    public static final String SELECTED_CONTACT_KEY = "SELECTED_CONTACT_KEY";

    // used as query string parameters to Parse Server queries
    public static final String CONTACT_SERVICE_SEARCH_QUERY_TEMPLATE = "{\"%s\":{\"$regex\":\"^%s\"}}";
    public static final String CONTACT_SERVICE_FAVORITES_QUERY_TEMPLATE = "{\"favorite\":true}";

    // associated with the spinner in the search fragment
    public static final String CONTACT_SEARCH_FIELD_CITY = "city";
    public static final String CONTACT_SEARCH_FIELD_FIRST_NAME = "firstName";
    public static final String CONTACT_SEARCH_FIELD_LAST_NAME = "lastName";
    public static final String CONTACT_SEARCH_FIELD_STATE = "state";
    public static final String[] CONTACT_SEARCH_FIELDS = new String[] {
            CONTACT_SEARCH_FIELD_CITY, CONTACT_SEARCH_FIELD_FIRST_NAME, CONTACT_SEARCH_FIELD_LAST_NAME, CONTACT_SEARCH_FIELD_STATE
    };

    // preferences keys
    public static final String SUPPRESS_DELETE_WARNING_KEY = "SUPPRESS_DELETE_WARNING_KEY";
    public static final String YAACM_PREFERENCES_KEY = "YAACM_PREFERENCES_KEY";
}
