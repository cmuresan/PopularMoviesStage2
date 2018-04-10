package com.example.android.networkmodule.network;

import com.example.android.networkmodule.model.MoviesApiResponse;
import com.example.android.networkmodule.model.ReviewsApiResponse;
import com.example.android.networkmodule.model.VideosApiResponse;

/**
 * Created by Cristian on 3/6/2018.
 */

public interface ApiInterface {
    void getPopularMovies(int page, CallbackInterface<MoviesApiResponse> result);

    void getTopRatedMovies(int page, CallbackInterface<MoviesApiResponse> result);

    void getVideosById(int movieId, CallbackInterface<VideosApiResponse> result);

    void getReviewsById(int movieId, CallbackInterface<ReviewsApiResponse> result);
}
