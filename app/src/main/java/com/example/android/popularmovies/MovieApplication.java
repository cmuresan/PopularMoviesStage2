package com.example.android.popularmovies;

import android.app.Application;

/**
 * Created by Cristian on 3/11/2018.
 */

public class MovieApplication extends Application {

    private static MovieApplication applicationContext;

    public static MovieApplication getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }
}
