package com.douglasstarnes.apps.yaacmapp;

/**
 * Created by douglasstarnes on 8/31/16.
 */
public class YAACMContactDTO {
    public String firstName;
    public String lastName;
    public String city;
    public String state;
    public String zipCode;
    public int dobYear;
    public int dobMonth;
    public int dobDay;
    public String comments;
    public boolean favorite;

    public YAACMContactDTO(String city, String comments, int dobDay, int dobMonth, int dobYear, boolean favorite, String firstName, String lastName, String state, String zipCode) {
        this.city = city;
        this.comments = comments;
        this.dobDay = dobDay;
        this.dobMonth = dobMonth;
        this.dobYear = dobYear;
        this.favorite = favorite;
        this.firstName = firstName;
        this.lastName = lastName;
        this.state = state;
        this.zipCode = zipCode;
    }
}
