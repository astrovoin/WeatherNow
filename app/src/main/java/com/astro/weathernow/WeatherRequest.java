package com.astro.weathernow;

import com.astro.weathernow.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherRequest {
    static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=acfe5130bfbb7a72e0c1dc06e1c2d5c7";
    static final String PART_URL = "&units=metric&appid=";

    private String OPEN_WEATHER_MAP_API = "acfe5130bfbb7a72e0c1dc06e1c2d5c7";
    private String location;


    WeatherRequest(String location) {
        this.location = location;
    }

    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    WeatherResponse run() {
        String FINAL_URL = BASE_URL;// + PART_URL + OPEN_WEATHER_MAP_API;
        OkHttpClient client = new OkHttpClient();
        client.retryOnConnectionFailure();
        Request.Builder builder = new Request.Builder();
        builder.url(String.format(FINAL_URL, location));
        final Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            return new Gson().fromJson(jsonString, WeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
