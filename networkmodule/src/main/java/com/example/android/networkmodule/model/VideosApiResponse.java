package com.example.android.networkmodule.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Cristian on 4/10/2018.
 */

public class VideosApiResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private ArrayList<Video> results;

    public ArrayList<Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }
}
