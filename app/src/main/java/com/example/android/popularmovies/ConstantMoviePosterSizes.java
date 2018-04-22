package com.example.android.popularmovies;

import android.support.annotation.StringDef;

import org.jetbrains.annotations.Contract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Cristian on 3/17/2018.
 */

public class ConstantMoviePosterSizes {
    //Constants
    private static final String W92 = "w92";
    private static final String W154 = "w154";
    private static final String W185 = "w185";
    private static final String W342 = "w342";
    private static final String W500 = "w500";
    private static final String W780 = "w780";
    private static final String ORIGINAL = "original";

    @StringDef({W92, W154, W185, W342, W500, W780, ORIGINAL})
    @Retention(RetentionPolicy.SOURCE)
    private @interface MoviePosterSize {
    }

    @Contract(pure = true)
    @MoviePosterSize
    public static String getW92() {
        return W92;
    }

    @Contract(pure = true)
    @MoviePosterSize
    public static String getW154() {
        return W154;
    }

    @Contract(pure = true)
    @MoviePosterSize
    public static String getW185() {
        return W185;
    }

    @Contract(pure = true)
    @MoviePosterSize
    public static String getW342() {
        return W342;
    }

    @Contract(pure = true)
    @MoviePosterSize
    public static String getW500() {
        return W500;
    }

    @Contract(pure = true)
    @MoviePosterSize
    public static String getW780() {
        return W780;
    }

    @Contract(pure = true)
    @MoviePosterSize
    public static String getOriginal() {
        return ORIGINAL;
    }
}
