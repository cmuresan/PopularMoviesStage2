package com.example.android.popularmovies.main;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.networkmodule.model.Movie;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.FavoriteMoviesContract;

import java.util.ArrayList;


public class FavoriteMoviesFragment extends Fragment {
    private static final int NUMBER_OF_COLUMNS = 3;
    private MoviesAdapter moviesAdapter;
    private ProgressDialog progressDialog;

    public FavoriteMoviesFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static FavoriteMoviesFragment newInstance() {
        return new FavoriteMoviesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView(getContext(), view);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMovies();
    }

    private void setupRecyclerView(Context context, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.popular_movies_recycler_view);
        ColumnsProvider columnsProvider = new ColumnsProviderImpl();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,
                columnsProvider.getNumberOfColumns(getActivity()));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(context);
        recyclerView.setAdapter(moviesAdapter);


        DividerItemDecoration verticalItemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        DividerItemDecoration horizontalItemDecorator = new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL);
        Drawable divider = ContextCompat.getDrawable(context, R.drawable.divider);
        if (divider != null) {
            verticalItemDecorator.setDrawable(divider);
            horizontalItemDecorator.setDrawable(divider);

            recyclerView.addItemDecoration(verticalItemDecorator);
            recyclerView.addItemDecoration(horizontalItemDecorator);
        }
    }

    private void getMovies() {
        int nextPage = 1;
        Cursor cursor = getContext().getContentResolver().query(FavoriteMoviesContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.api_erorr), Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<Movie> movies = getMoviesFromCursor(cursor);
        moviesAdapter.setMovies(movies);
    }

    private ArrayList<Movie> getMoviesFromCursor(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();

        int movieIdIndex = cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID);
        int titleIndex = cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE);
        int posterIndex = cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER);
        int synopsisIndex = cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_SYNOPSIS);
        int ratingIndex = cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_RATING);
        int releaseDateIndex = cursor.getColumnIndex(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE);

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            int movieId = cursor.getInt(movieIdIndex);
            String title = cursor.getString(titleIndex);
            String poster = cursor.getString(posterIndex);
            String synopsis = cursor.getString(synopsisIndex);
            Double rating = cursor.getDouble(ratingIndex);
            String releaseDate = cursor.getString(releaseDateIndex);

            Movie movie = new Movie(movieId, title, poster, synopsis, rating, releaseDate);
            movies.add(movie);
            cursor.moveToNext();
        }
        return movies;
    }
}
