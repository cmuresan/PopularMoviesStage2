package com.example.android.networkmodule.network;

import com.example.android.networkmodule.model.MoviesApiResponse;

/**
 * Created by Cristian on 3/6/2018.
 */

public interface ApiInterface {
    void getPopularMovies(int page, CallbackInterface<MoviesApiResponse> result);

    void getTopRatedMovies(int page, CallbackInterface<MoviesApiResponse> result);
}
