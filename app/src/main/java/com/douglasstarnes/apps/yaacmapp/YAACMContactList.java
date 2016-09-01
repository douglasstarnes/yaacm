package com.douglasstarnes.apps.yaacmapp;

import java.util.List;

/**
 * Created by douglasstarnes on 8/31/16.
 */

// the REST API call to get a list of contacts will return a JSON object with a single key, 'results',
// that will be an array of objects for each contact
public class YAACMContactList {
    public List<YAACMContact> results;
}
