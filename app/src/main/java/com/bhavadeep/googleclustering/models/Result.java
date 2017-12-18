
package com.bhavadeep.googleclustering.models;

import android.graphics.Bitmap;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Result extends RealmObject {

    @SerializedName("formatted_address")
    @Expose
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Expose
    private Geometry geometry;
    @Expose
    private String icon;
    @PrimaryKey
    @Expose
    private String id;
    @Expose
    private String name;
    @Ignore
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @Expose
    @Ignore
    private List<Photo> photos = null;

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Expose
    private String rating;


    /**
     * No args constructor for use in serialization
     * 
     */
    public Result() {
    }


    public Result(Geometry geometry, String icon, String id, String name, OpeningHours openingHours, List<Photo> photos) {
        super();
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.openingHours = openingHours;
        this.photos = photos;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

}
