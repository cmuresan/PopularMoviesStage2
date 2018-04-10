package com.example.android.networkmodule.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Cristian on 3/11/2018.
 */

public class MoviesApiResponse {
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;
    private ArrayList<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
