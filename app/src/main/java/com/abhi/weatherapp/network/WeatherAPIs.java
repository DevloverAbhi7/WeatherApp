package com.abhi.weatherapp.network;

import com.abhi.weatherapp.model.Example;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPIs {
    /*
    Get request to fetch city weather.Takes in two parameter-city name and API key.
    */
    @GET("/data/2.5/weather")
    Call<Example> getWeatherByCity(@Query("appid") String apiKey, @Query("q") String city);
}