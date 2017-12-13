
package com.bhavadeep.googleclustering.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResult {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @Expose
    private List<Result> results = null;
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     * 
     */
    public APIResult() {
    }

    /**
     * 
     * @param results
     * @param status
     * @param htmlAttributions
     */
    public APIResult(List<Object> htmlAttributions, List<Result> results, String status) {
        super();
        this.htmlAttributions = htmlAttributions;
        this.results = results;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
