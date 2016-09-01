package com.douglasstarnes.apps.yaacmapp;

// the response for zipcodeapi.com has more data than this.
// for simplicity I only included fields for the keys I am interested in
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
