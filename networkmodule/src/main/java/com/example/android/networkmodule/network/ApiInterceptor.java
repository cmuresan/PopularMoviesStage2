package com.example.android.networkmodule.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Cristian on 3/11/2018.
 */

class ApiInterceptor implements Interceptor {

    private final String apiKey;

    ApiInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build();

        Request.Builder builder = original.newBuilder()
                .url(url)
                .addHeader("Accept", "*/*")
                .method(original.method(), original.body());

        return chain.proceed(builder.build());
    }
}
