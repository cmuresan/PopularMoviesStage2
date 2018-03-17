package com.example.android.networkmodule.network;

import com.example.android.networkmodule.model.MoviesApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Cristian on 3/11/2018.
 */

interface MoviesApi {
    @GET("top_rated")
    Call<MoviesApiResponse> getTopRatedMovies(@Query("page") int page);

    @GET("popular")
    Call<MoviesApiResponse> getPopularMovies(@Query("page") int page);
}
