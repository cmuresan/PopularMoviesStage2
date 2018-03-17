package com.example.android.networkmodule.model;

import java.io.Serializable;

/**
 * Created by Cristian on 3/11/2018.
 */

public class Movie implements Serializable{
    private int vote_count;
    private int id;
    private boolean video;
    private double vote_average;
    private String title;
    private double popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private int[] genre_ids;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;

    public double getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

}
