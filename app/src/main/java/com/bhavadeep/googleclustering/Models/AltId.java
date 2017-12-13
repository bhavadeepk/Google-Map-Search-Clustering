
package com.bhavadeep.googleclustering.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AltId {

    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("scope")
    @Expose
    private String scope;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AltId() {
    }

    /**
     * 
     * @param scope
     * @param placeId
     */
    public AltId(String placeId, String scope) {
        super();
        this.placeId = placeId;
        this.scope = scope;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
