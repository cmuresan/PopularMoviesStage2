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
import com.example.android.networkmodule.model.Review;
import com.example.android.networkmodule.model.ReviewsApiResponse;
import com.example.android.networkmodule.model.Video;
import com.example.android.networkmodule.model.VideosApiResponse;
import com.example.android.networkmodule.network.ApiImpl;
import com.example.android.networkmodule.network.ApiInterface;
import com.example.android.networkmodule.network.CallbackInterface;
import com.example.android.popularmovies.ConstantMoviePosterSizes;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.FavoriteMoviesContract;
import com.example.android.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "MovieDetailsActivity.EXTRA_MOVIE";
    private static final String MOVIE_KEY = "MovieDetailsActivity.MOVIE_KEY";
    private static final String VIDEOS_KEY = "MovieDetailsActivity.VIDEOS_KEY";
    private static final String REVIEWS_KEY = "MovieDetailsActivity.REVIEWS_KEY";
    private ActivityMovieDetailsBinding detailsBinding;
    private VideosAdapter videosAdapter;
    private ReviewsAdapter reviewsAdapter;
    private boolean isMovieFav = false;
    private Movie movie;
    private ArrayList<Video> videos;
    private ArrayList<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        detailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fetchMovie(savedInstanceState);
        if (movie != null) {
            handleFab(movie.getId());
            setupVideosRecyclerView();
            setupReviewsRecyclerView();
            bindData(movie);
            fetchVideos(savedInstanceState);
            fetchReviews(savedInstanceState);
        } else {
            Toast.makeText(this, getString(R.string.api_erorr), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMovie(Bundle savedInstanceState) {
        if (savedInstanceState == null || savedInstanceState.containsKey(MOVIE_KEY)) {
            movie = getIntentData();
        } else {
            movie = savedInstanceState.getParcelable(MOVIE_KEY);
        }
    }

    private void fetchVideos(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(VIDEOS_KEY)) {
            getVideos(movie.getId());
        } else {
            videos = savedInstanceState.getParcelableArrayList(VIDEOS_KEY);
            handleSuccessfulVideosFetching();
        }
    }

    private void fetchReviews(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(REVIEWS_KEY)) {
            getReviews(movie.getId());
        } else {
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
            handleSuccessfulReviewsFetching();
        }
    }

    private void handleFab(int id) {
        detailsBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie = getIntentData();
                if (movie == null) {
                    Snackbar.make(view, R.string.could_not_save_movie, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                    return;
                }
                if (isMovieFav) {
                    Uri uri = FavoriteMoviesContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(String.valueOf(movie.getId())).build();

                    int id = getContentResolver().delete(uri, null, null);
                    if (id != 0) {
                        isMovieFav = false;
                        Snackbar.make(view, R.string.deleted_movie, Snackbar.LENGTH_LONG)
                                .setAction(R.string.action, null).show();
                    } else {
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
                        Snackbar.make(view, R.string.saved_movie, Snackbar.LENGTH_LONG)
                                .setAction(R.string.action, null).show();
                        isMovieFav = true;
                    }
                }
                setFabImageResource();
            }
        });

        Uri uri = FavoriteMoviesContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            handleFabButton(cursor);
        }
    }

    private void handleFabButton(Cursor cursor) {
        isMovieFav = cursor.getCount() > 0;
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

    private final CallbackInterface<VideosApiResponse> videosApiResponseCallbackInterface = new CallbackInterface<VideosApiResponse>() {
        @Override
        public void success(VideosApiResponse response) {
            videos = response.getResults();
            handleSuccessfulVideosFetching();
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            detailsBinding.content.videosHeader.setVisibility(View.GONE);
            detailsBinding.content.videosRecyclerView.setVisibility(View.GONE);
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.videos_erorr), Toast.LENGTH_SHORT).show();
        }
    };

    private void handleSuccessfulVideosFetching() {
        if (!videos.isEmpty()) {
            detailsBinding.content.videosHeader.setVisibility(View.VISIBLE);
            detailsBinding.content.videosRecyclerView.setVisibility(View.VISIBLE);
        }
        videosAdapter.setVideos(videos);
    }

    private final CallbackInterface<ReviewsApiResponse> reviewsApiResponseCallbackInterface = new CallbackInterface<ReviewsApiResponse>() {
        @Override
        public void success(ReviewsApiResponse response) {
            reviews = response.getResults();
            handleSuccessfulReviewsFetching();
        }

        @Override
        public void failure(String errorMessage, String errorCode) {
            detailsBinding.content.reviewsHeader.setVisibility(View.GONE);
            detailsBinding.content.reviewsRecyclerView.setVisibility(View.GONE);
            Toast.makeText(MovieDetailsActivity.this, getString(R.string.reviews_erorr), Toast.LENGTH_SHORT).show();
        }
    };

    private void handleSuccessfulReviewsFetching() {
        if (!reviews.isEmpty()) {
            detailsBinding.content.reviewsHeader.setVisibility(View.VISIBLE);
            detailsBinding.content.reviewsRecyclerView.setVisibility(View.VISIBLE);
        }
        reviewsAdapter.setReviews(reviews);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_KEY, movie);
        outState.putParcelableArrayList(VIDEOS_KEY, videos);
        outState.putParcelableArrayList(REVIEWS_KEY, reviews);
    }
}
