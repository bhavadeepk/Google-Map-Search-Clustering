
package com.bhavadeep.googleclustering.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;

public class Geometry extends RealmObject {

    @Expose
    private Location location;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Geometry() {
    }

    /**
     * 
     * @param location
     */
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

}
