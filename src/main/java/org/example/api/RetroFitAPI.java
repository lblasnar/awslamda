package org.example.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author luigi
 * 16/02/2023
 */
public class RetroFitAPI {

    public static final String BASE_URL = "https://api.abcotvs.com";

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private final Retrofit retrofit;

    public RetroFitAPI() {
        Gson gson = new GsonBuilder().setLenient().create();
        // Due to 2 * 99p lambda availability
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .writeTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .build();
        retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
    }
}
