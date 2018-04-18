package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class MoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favMoviesDb.db";

    private static final int VERSION = 1;

    MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + FavoriteMoviesContract.MovieEntry.TABLE_NAME + " (" +
                FavoriteMoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
