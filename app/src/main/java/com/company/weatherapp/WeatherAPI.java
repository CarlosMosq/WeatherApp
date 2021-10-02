package com.company.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather?appid=6846af233cdc74b44664bc8dc69c5b5d&units=metric")
    Call<WeatherMap> getWeatherMapWithLocation(@Query("lat") double lat, @Query("lon") double lon );

    @GET("weather?appid=6846af233cdc74b44664bc8dc69c5b5d&units=metric")
    Call<WeatherMap> getWeatherMapWithCityName(@Query("q") String name);
}
