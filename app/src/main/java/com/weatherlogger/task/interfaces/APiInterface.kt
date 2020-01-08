package com.weatherlogger.task.interfaces

import com.weatherlogger.task.model.CityByID.CityByIDResponse
import com.weatherlogger.task.model.serverData.OpenWeatherResponse
import retrofit2.Call
import retrofit2.http.*


interface APiInterface {
    /**
     * Get City by name
     */
    @GET("find")
    fun getCityByName(
        @Query("mode") mode: String, @Query("units") units: String, @Query("type") type: String, @Query(
            "q") q: String, @Query("appid") appid: String
    ): Call<OpenWeatherResponse>

    /**
     * Get City by Coodrinates
     */
    @GET("find")
    fun getCityByCoordinates(@Query("units") units: String,@Query("lat") lat: String, @Query("lon") long: String, @Query("appid") appid: String): Call<OpenWeatherResponse>

    /**
     * Get City by ID
     */
    @GET("weather")
    fun getCityByID(@Query("units") units: String,@Query("id") id: Int,  @Query("appid") appid: String): Call<CityByIDResponse>
}