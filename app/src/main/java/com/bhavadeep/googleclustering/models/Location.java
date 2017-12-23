
package com.bhavadeep.googleclustering.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Location extends RealmObject {
    @PrimaryKey
    private String uGID;

    public String getuGID() {
        return uGID;
    }

    public void setuGID(String uGID) {
        this.uGID = uGID;
    }

    @Expose
    private Double lat;
    @Expose
    private Double lng;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Location() {
    }


    public Location(Double lat, Double lng) {
        super();
        this.lat = lat;
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
