package com.douglasstarnes.apps.yaacmapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

// the class that represents the contact data
// several interesting notes:
// 1) Implements Parcelable to allow transporting between fragments
// 2) Each date component for DOB is store separately.  This makes the Parcelable implementation
//    more straightforward.  Parse Server can handle dates and Java dates can be Parcelable
//    but for demo purposes I chose to keep it simple
// the Parcelable implementation is mostly boilerplate
public class YAACMContact implements Parcelable {
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String zipCode;
    private int dobYear;
    private int dobMonth;
    private int dobDay;
    private String comments;
    private boolean favorite;
    private String objectId;

    public YAACMContact(Date dob, String city, String comments, boolean favorite, String firstName, String lastName, String state, String zipCode) {
        this.city = city;
        this.comments = comments;
        this.favorite = favorite;
        this.firstName = firstName;
        this.lastName = lastName;
        this.state = state;
        this.zipCode = zipCode;

        // parse date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dob);
        this.dobYear = calendar.get(Calendar.YEAR);
        this.dobMonth = calendar.get(Calendar.MONTH);
        this.dobDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    protected YAACMContact(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        city = in.readString();
        state = in.readString();
        zipCode = in.readString();
        comments = in.readString();
        favorite = in.readByte() != 0;
        objectId = in.readString();
        dobYear = in.readInt();
        dobMonth = in.readInt();
        dobDay = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipCode);
        dest.writeString(comments);
        dest.writeByte((byte) (favorite ? 1 : 0));
        dest.writeString(objectId);
        dest.writeInt(dobYear);
        dest.writeInt(dobMonth);
        dest.writeInt(dobDay);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<YAACMContact> CREATOR = new Creator<YAACMContact>() {
        @Override
        public YAACMContact createFromParcel(Parcel in) {
            return new YAACMContact(in);
        }

        @Override
        public YAACMContact[] newArray(int size) {
            return new YAACMContact[size];
        }
    };

    public void setDob(Date dob) {
        // separate the Java date into individual components
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dob);
        this.dobYear = calendar.get(Calendar.YEAR);
        this.dobMonth = calendar.get(Calendar.MONTH);
        this.dobDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public Date getDob() {
        // combine individual date components
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.dobYear);
        calendar.set(Calendar.MONTH, this.dobMonth);
        calendar.set(Calendar.DAY_OF_MONTH, this.dobDay);
        return calendar.getTime();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


}
