package com.nedaluof.magweath.data.api;

import com.nedaluof.magweath.data.model.WeatherModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("onecall")
    Observable<WeatherModel> getWeatherData(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("units") String units,
            @Query("lang") String language,
            @Query("appid") String api_key
    );

}
