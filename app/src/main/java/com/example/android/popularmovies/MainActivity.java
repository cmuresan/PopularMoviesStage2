package com.example.android.popularmovies;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.networkmodule.model.MoviesApiResponse;
import com.example.android.networkmodule.network.ApiImpl;
import com.example.android.networkmodule.network.CallbackInterface;

public class MainActivity extends AppCompatActivity {

    private static final int NUMBER_OF_COLUMNS = 3;
    private static int nextPage = 1;
    private MoviesAdapter moviesAdapter;
    private boolean isPopularSelected = true;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupRecyclerView();
        getMovies();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.movies_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter(this);
        recyclerView.setAdapter(moviesAdapter);


        DividerItemDecoration verticalItemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        DividerItemDecoration horizontalItemDecorator = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            verticalItemDecorator.setDrawable(divider);
            horizontalItemDecorator.setDrawable(divider);

            recyclerView.addItemDecoration(verticalItemDecorator);
            recyclerView.addItemDecoration(horizontalItemDecorator);
        }
    }

    private void getMovies() {
        showProgressDialog();
        if (isPopularSelected) {
            new ApiImpl(getString(R.string.api_key)).getPopularMovies(nextPage, moviesApiResponseCallbackInterface);
        } else {
            new ApiImpl(getString(R.string.api_key)).getTopRatedMovies(nextPage, moviesApiResponseCallbackInterface);
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
    }

    CallbackInterface<MoviesApiResponse> moviesApiResponseCallbackInterface = new CallbackInterface<MoviesApiResponse>() {
        @Override
        public void success(MoviesApiResponse response) {
            cancelProgressDialog();
            moviesAdapter.setMovies(response.getResults());
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            cancelProgressDialog();
            Toast.makeText(MainActivity.this, getString(R.string.api_erorr), Toast.LENGTH_SHORT).show();
        }
    };

    private void cancelProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflateMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        inflateMenu(menu);
        return true;
    }

    private void inflateMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (isPopularSelected) {
            inflater.inflate(R.menu.top_rated, menu);
        } else {
            inflater.inflate(R.menu.popular, menu);
        }
    }

    public void loadTopRated(MenuItem item) {
        isPopularSelected = false;
        invalidateOptionsMenu();
        getMovies();
    }

    public void loadPopular(MenuItem item) {
        isPopularSelected = true;
        invalidateOptionsMenu();
        getMovies();
    }
}
