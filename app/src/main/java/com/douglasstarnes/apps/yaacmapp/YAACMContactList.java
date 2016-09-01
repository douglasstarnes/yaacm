package com.douglasstarnes.apps.yaacmapp;

import java.util.List;

// the REST API call to get a list of contacts will return a JSON object with a single key, 'results',
// that will be an array of objects for each contact
public class YAACMContactList {
    public List<YAACMContact> results;
}
