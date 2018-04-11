package com.example.android.popularmovies.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.networkmodule.model.Movie;
import com.example.android.networkmodule.model.ReviewsApiResponse;
import com.example.android.networkmodule.model.VideosApiResponse;
import com.example.android.networkmodule.network.ApiImpl;
import com.example.android.networkmodule.network.ApiInterface;
import com.example.android.networkmodule.network.CallbackInterface;
import com.example.android.popularmovies.ConstantMoviePosterSizes;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "MovieDetailsActivity.EXTRA_MOVIE";
    private ImageView moviePoster;
    private TextView movieTitle;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView movieOverview;
    private TextView movieReleaseDate;
    private TextView movieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Movie movie = getIntentData();
        if (movie != null) {
            initViews();
            bindData(movie);
            getVideos(movie.getId());
            getReviews(movie.getId());
        } else {
            Toast.makeText(this, getString(R.string.api_erorr), Toast.LENGTH_SHORT).show();
        }
    }

    private void getVideos(int id) {
        ApiInterface apiInterface = new ApiImpl(getString(R.string.api_key));
        apiInterface.getVideosById(id, videosApiResponseCallbackInterface);
    }

    private void getReviews(int id) {
        ApiInterface apiInterface = new ApiImpl(getString(R.string.api_key));
        apiInterface.getReviewsById(id, reviewsApiResponseCallbackInterface);
    }

    private void initViews() {
        moviePoster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        movieOverview = findViewById(R.id.movie_overview);
        movieReleaseDate = findViewById(R.id.movie_release_date);
        movieRating = findViewById(R.id.movie_rating);
    }

    private void bindData(Movie movie) {
        String posterUrl = String.format(getString(R.string.poster_base_url),
                ConstantMoviePosterSizes.getOriginal(), movie.getPosterPath());
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.string.image_error)
                .into(moviePoster);
        toolbarLayout.setTitleEnabled(true);
        toolbarLayout.setTitle(movie.getOriginalTitle());
        movieOverview.setText(movie.getOverview());
        movieReleaseDate.setText(movie.getReleaseDate());
        String rating = String.format(getString(R.string.movie_rating_template), String.valueOf(movie.getVoteAverage()));
        movieRating.setText(rating);
    }

    @Nullable
    private Movie getIntentData() {
        Intent movieIntent = getIntent();
        if (movieIntent != null && movieIntent.hasExtra(EXTRA_MOVIE)) {
            Movie movie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
            if (movie != null) {
                return movie;
            }
        }
        return null;
    }

    private static final String TAG = "MovieDetailsActivity";
    private final CallbackInterface<VideosApiResponse> videosApiResponseCallbackInterface = new CallbackInterface<VideosApiResponse>() {
        @Override
        public void success(VideosApiResponse response) {
            Log.d(TAG, "success: " + response.getResults().size());
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.videos_erorr), Toast.LENGTH_SHORT).show();
        }
    };

    private final CallbackInterface<ReviewsApiResponse> reviewsApiResponseCallbackInterface = new CallbackInterface<ReviewsApiResponse>() {
        @Override
        public void success(ReviewsApiResponse response) {
            Log.d(TAG, "success: " + response.getResults().size());
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.reviews_erorr), Toast.LENGTH_SHORT).show();
        }
    };
}
