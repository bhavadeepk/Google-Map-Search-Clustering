
package com.bhavadeep.googleclustering.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Geometry extends RealmObject {
    @PrimaryKey
    private int uID;

    @Expose
    private Location location;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Geometry() {
    }


    public Geometry(Location location) {
        super();
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }
}
