package com.astro.weathernow;

import com.astro.weathernow.model.WeatherResponse;

import java.io.IOException;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherRequest {
    public interface OpenWeather {
        @GET("data/2.5/weather")
        Call<WeatherResponse> loadWeather(@Query("q") String cityCountry, @Query("appid") String keyApi);
    }
    static final String APP_ID = "acfe5130bfbb7a72e0c1dc06e1c2d5c7";

    static private Retrofit retrofit;

    static private Retrofit getRetrofit(){
        if(retrofit != null) return retrofit;
        return retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private String location;

    WeatherRequest(String location){
        this.location = location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    WeatherResponse run(){
        OpenWeather weather = getRetrofit().create(OpenWeather.class);
        try {
            return weather.loadWeather(location, APP_ID).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
