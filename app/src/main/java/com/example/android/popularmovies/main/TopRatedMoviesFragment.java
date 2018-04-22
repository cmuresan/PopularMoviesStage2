package com.example.android.popularmovies.main;


import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.android.networkmodule.model.MoviesApiResponse;
import com.example.android.networkmodule.network.ApiImpl;
import com.example.android.networkmodule.network.CallbackInterface;
import com.example.android.popularmovies.R;

import java.util.ArrayList;

public class TopRatedMoviesFragment extends Fragment {

    private static final String TOP_RATED_MOVIES_KEY = "TopRatedMoviesFragment.TOP_RATED_MOVIES_KEY";
    private MoviesAdapter moviesAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Movie> movies;

    public TopRatedMoviesFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static TopRatedMoviesFragment newInstance() {
        return new TopRatedMoviesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView(getContext(), view);
        if (savedInstanceState == null || !savedInstanceState.containsKey(TOP_RATED_MOVIES_KEY)) {
            getMovies();
        } else {
            movies = savedInstanceState.getParcelableArrayList(TOP_RATED_MOVIES_KEY);
            moviesAdapter.setMovies(movies);
        }
    }

    private void setupRecyclerView(Context context, View view) {
        RecyclerView recyclerView = view.findViewById(R.id.movies_recycler_view);
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
        showProgressDialog();
        int nextPage = 1;
        new ApiImpl(getString(R.string.api_key)).getTopRatedMovies(nextPage, moviesApiResponseCallbackInterface);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
    }

    private final CallbackInterface<MoviesApiResponse> moviesApiResponseCallbackInterface = new CallbackInterface<MoviesApiResponse>() {
        @Override
        public void success(MoviesApiResponse response) {
            cancelProgressDialog();
            movies = response.getResults();
            moviesAdapter.setMovies(movies);
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            cancelProgressDialog();
            Toast.makeText(getContext(), getString(R.string.api_erorr), Toast.LENGTH_SHORT).show();
        }
    };

    private void cancelProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TOP_RATED_MOVIES_KEY, movies);
    }
}
