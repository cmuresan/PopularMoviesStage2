package com.example.android.popularmovies.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.networkmodule.model.Movie;
import com.example.android.popularmovies.ConstantMoviePosterSizes;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "MovieDetailsActivity.EXTRA_MOVIE";
    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieOverview;
    private TextView movieReleaseDate;
    private TextView movieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Movie movie = getIntentData();
        if (movie != null) {
            initViews();
            bindData(movie);
        } else {
            Toast.makeText(this, getString(R.string.api_erorr), Toast.LENGTH_SHORT).show();
        }

    }

    private void initViews() {
        moviePoster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
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
        movieTitle.setText(movie.getOriginalTitle());
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
}
