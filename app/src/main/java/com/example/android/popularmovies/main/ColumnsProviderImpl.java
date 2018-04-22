package com.example.android.popularmovies.main;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ColumnsProviderImpl implements ColumnsProvider {
    @Override
    public int getNumberOfColumns(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 300;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }
}
