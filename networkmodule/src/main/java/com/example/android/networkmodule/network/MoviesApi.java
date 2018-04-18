package com.example.android.networkmodule.network;

import com.example.android.networkmodule.model.MoviesApiResponse;
import com.example.android.networkmodule.model.ReviewsApiResponse;
import com.example.android.networkmodule.model.VideosApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Cristian on 3/11/2018.
 */

interface MoviesApi {
    @GET("top_rated")
    Call<MoviesApiResponse> getTopRatedMovies(@Query("page") int page);

    @GET("popular")
    Call<MoviesApiResponse> getPopularMovies(@Query("page") int page);

    @GET("{movie_id}/videos")
    Call<VideosApiResponse> getVideosById(@Path("movie_id") int movieId);

    @GET("{movie_id}/reviews")
    Call<ReviewsApiResponse> getReviewsById(@Path("movie_id") int movieId);
}
