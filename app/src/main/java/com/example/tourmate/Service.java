package com.example.tourmate;

import com.example.tourmate.WeatherPojo.WeatherPojoClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Service {

    @GET
    Call<WeatherPojoClass> getWeatherData(@Url String url);

}
