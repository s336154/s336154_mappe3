package com.example.s336154_mappe3;


import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {


    private int id;
    private String place;
    private String comment;
    private String address;

    private double latitude;
    private double longitude;



    public Place(String place,String comment, String address, double latitude, double longitude) {

        this.place = place;
        this.comment=comment;
        this.address=address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Implement Parcelable methods for passing data to SavedPlacesActivity

    protected Place(Parcel in) {
        place = in.readString();
        comment = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(place);
        dest.writeString(comment);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public String toString() {
        return place;
    }
}

