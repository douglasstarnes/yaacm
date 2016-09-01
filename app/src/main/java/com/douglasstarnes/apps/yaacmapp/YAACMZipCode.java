package com.douglasstarnes.apps.yaacmapp;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class YAACMZipCode {
    private String city;
    private String state;

    public YAACMZipCode(String city, String state) {
        this.city = city;
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
