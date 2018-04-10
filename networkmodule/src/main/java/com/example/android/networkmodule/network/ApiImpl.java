package com.example.android.networkmodule.network;

import com.example.android.networkmodule.model.MoviesApiResponse;
import com.example.android.networkmodule.model.ReviewsApiResponse;
import com.example.android.networkmodule.model.VideosApiResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cristian on 3/11/2018.
 */

public class ApiImpl implements ApiInterface {

    /**
     * API URL
     */
    private static final String API_URL = "https://api.themoviedb.org/3/movie/";
    private static final long TIME_TO_CONNECT = 30;
    private final MoviesApi apiService;
    private final String apiKey;

    public ApiImpl(String apiKey) {
        this.apiKey = apiKey;
        Retrofit retrofit = buildRetrofit();
        apiService = retrofit.create(MoviesApi.class);
    }

    private Retrofit buildRetrofit() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_TO_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIME_TO_CONNECT, TimeUnit.SECONDS)
                .writeTimeout(TIME_TO_CONNECT, TimeUnit.SECONDS)
                .addInterceptor(new ApiInterceptor(apiKey))
                .build();

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    @Override
    public void getPopularMovies(int page, CallbackInterface<MoviesApiResponse> callback) {
        Call<MoviesApiResponse> call = apiService.getPopularMovies(page);
        call.enqueue(new RestCallbackImpl<>(callback));
    }

    @Override
    public void getTopRatedMovies(int page, CallbackInterface<MoviesApiResponse> callback) {
        Call<MoviesApiResponse> call = apiService.getTopRatedMovies(page);
        call.enqueue(new RestCallbackImpl<>(callback));
    }

    @Override
    public void getVideosById(int movieId, CallbackInterface<VideosApiResponse> callback) {
        Call<VideosApiResponse> call = apiService.getVideosById(movieId);
        call.enqueue(new RestCallbackImpl<>(callback));
    }

    @Override
    public void getReviewsById(int movieId, CallbackInterface<ReviewsApiResponse> callback) {
        Call<ReviewsApiResponse> call = apiService.getReviewsById(movieId);
        call.enqueue(new RestCallbackImpl<>(callback));
    }
}
