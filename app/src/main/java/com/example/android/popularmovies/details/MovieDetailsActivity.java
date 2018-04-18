package com.example.android.popularmovies.details;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.android.networkmodule.model.Movie;
import com.example.android.networkmodule.model.ReviewsApiResponse;
import com.example.android.networkmodule.model.VideosApiResponse;
import com.example.android.networkmodule.network.ApiImpl;
import com.example.android.networkmodule.network.ApiInterface;
import com.example.android.networkmodule.network.CallbackInterface;
import com.example.android.popularmovies.ConstantMoviePosterSizes;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.FavoriteMoviesContract;
import com.example.android.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "MovieDetailsActivity.EXTRA_MOVIE";
    private ActivityMovieDetailsBinding detailsBinding;
    private VideosAdapter videosAdapter;
    private ReviewsAdapter reviewsAdapter;
    private boolean isMovieFav = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        detailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Movie movie = getIntentData();
        if (movie != null) {
            handleFab(movie.getId());
            setupVideosRecyclerView();
            setupReviewsRecyclerView();
            bindData(movie);
            getVideos(movie.getId());
            getReviews(movie.getId());
        } else {
            Toast.makeText(this, getString(R.string.api_erorr), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFab(int id) {
        detailsBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie = getIntentData();
                if (movie == null) {
                    Snackbar.make(view, "Could not save movie", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (isMovieFav) {
                    Uri uri = FavoriteMoviesContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(String.valueOf(movie.getId())).build();

                    int id = getContentResolver().delete(uri, null, null);
                    if (id != 0) {
                        isMovieFav = false;
                        Snackbar.make(view, "Deleted", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else {
                        isMovieFav = true;
                    }
                } else {


                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                    contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
                    contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
                    contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
                    contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());
                    contentValues.put(FavoriteMoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

                    Uri uri = getContentResolver().insert(FavoriteMoviesContract.MovieEntry.CONTENT_URI, contentValues);

                    if (uri != null) {
                        Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        isMovieFav = true;
                    }
                }
                setFabImageResource();
            }
        });

        Uri uri = FavoriteMoviesContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        handleFabButton(cursor);
    }

    private void handleFabButton(Cursor cursor) {
        if (cursor.getCount() > 0) {
            isMovieFav = true;
        } else {
            isMovieFav = false;
        }
        setFabImageResource();
    }

    private void setFabImageResource() {
        if (isMovieFav) {
            detailsBinding.fab.setImageResource(android.R.drawable.star_big_on);
        } else {
            detailsBinding.fab.setImageResource(android.R.drawable.star_big_off);
        }
    }

    private void setupVideosRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        detailsBinding.content.videosRecyclerView.setLayoutManager(linearLayoutManager);
        detailsBinding.content.videosRecyclerView.setHasFixedSize(true);

        videosAdapter = new VideosAdapter(this);
        detailsBinding.content.videosRecyclerView.setAdapter(videosAdapter);


        DividerItemDecoration horizontalItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.space_divider);
        if (divider != null) {
            horizontalItemDecoration.setDrawable(divider);
            detailsBinding.content.videosRecyclerView.addItemDecoration(horizontalItemDecoration);
        }
    }

    private void setupReviewsRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        detailsBinding.content.reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        detailsBinding.content.reviewsRecyclerView.setHasFixedSize(true);

        reviewsAdapter = new ReviewsAdapter(this);
        detailsBinding.content.reviewsRecyclerView.setAdapter(reviewsAdapter);


        DividerItemDecoration horizontalItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.space_divider);
        if (divider != null) {
            horizontalItemDecoration.setDrawable(divider);
            detailsBinding.content.reviewsRecyclerView.addItemDecoration(horizontalItemDecoration);
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

    private void bindData(Movie movie) {
        String posterUrl = getPosterUrl(movie.getPosterPath());
        Picasso.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.string.image_error)
                .into(detailsBinding.moviePoster);
        detailsBinding.toolbarLayout.setTitleEnabled(true);
        detailsBinding.toolbarLayout.setTitle(movie.getOriginalTitle());
        detailsBinding.content.movieOverview.setText(movie.getOverview());
        detailsBinding.content.movieReleaseDate.setText(movie.getReleaseDate());
        String rating = String.format(getString(R.string.movie_rating_template), String.valueOf(movie.getVoteAverage()));
        detailsBinding.content.movieRating.setText(rating);
    }

    private String getPosterUrl(String posterPath) {
        return String.format(getString(R.string.poster_base_url),
                ConstantMoviePosterSizes.getOriginal(), posterPath);
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
            detailsBinding.content.videosHeader.setVisibility(View.VISIBLE);
            detailsBinding.content.videosRecyclerView.setVisibility(View.VISIBLE);
            videosAdapter.setVideos(response.getResults());
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            detailsBinding.content.videosHeader.setVisibility(View.GONE);
            detailsBinding.content.videosRecyclerView.setVisibility(View.GONE);
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.videos_erorr), Toast.LENGTH_SHORT).show();
        }
    };

    private final CallbackInterface<ReviewsApiResponse> reviewsApiResponseCallbackInterface = new CallbackInterface<ReviewsApiResponse>() {
        @Override
        public void success(ReviewsApiResponse response) {
            detailsBinding.content.reviewsHeader.setVisibility(View.VISIBLE);
            detailsBinding.content.reviewsRecyclerView.setVisibility(View.VISIBLE);
            reviewsAdapter.setReviews(response.getResults());
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            detailsBinding.content.reviewsHeader.setVisibility(View.GONE);
            detailsBinding.content.reviewsRecyclerView.setVisibility(View.GONE);
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.reviews_erorr), Toast.LENGTH_SHORT).show();
        }
    };
}
