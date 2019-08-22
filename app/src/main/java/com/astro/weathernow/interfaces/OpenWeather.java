package com.astro.weathernow.interfaces;


import com.astro.weathernow.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather")
Call<WeatherResponse> loadWeather(@Query("q") String cityCountry, @Query("appid") String keyApi);
}