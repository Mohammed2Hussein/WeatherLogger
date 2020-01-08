package com.weatherlogger.task.core

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.weatherlogger.task.BuildConfig
import com.weatherlogger.task.interfaces.APiInterface
import com.weatherlogger.task.model.CityByID.CityByIDResponse
import com.weatherlogger.task.model.StoredCities
import com.weatherlogger.task.model.serverData.OpenWeatherResponse
import com.weatherlogger.task.view.MainActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


class Cache {
    private var retrofit: Retrofit? = null
    public var api: APiInterface? = null

    private val httpRestCityMainURL = "http://api.openweathermap.org/data/2.5/"

    public var openWeatherResponse: OpenWeatherResponse? = null
    public var cityByIDResponse: CityByIDResponse? = null
    public var storedCitiesList: ArrayList<StoredCities>? = null

    var selectedCityID: Int = 0

    var mainActivity: MainActivity? = null

    constructor() {
        initRetrofit(httpRestCityMainURL)
    }

    /**
     * Retrofit Variables
     */
    fun initRetrofit(baseURL: String) {
        val builder = OkHttpClient.Builder()
            .connectTimeout(3000, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .readTimeout(3000, TimeUnit.SECONDS)
            .callTimeout(3000, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(OkHttpProfilerInterceptor())
        }

        val client = builder.build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit?.create(APiInterface::class.java)
    }

    fun initDatabase(activity: Activity) {
        val db: SQLiteDatabase =
            activity.openOrCreateDatabase(
                "Weather.db",
                Context.MODE_ENABLE_WRITE_AHEAD_LOGGING,
                null
            )
        try {
            val CREATE_TABLE_CONTAIN = ("CREATE TABLE IF NOT EXISTS Cities ("
                    + "CityID INTEGER primary key,"
                    + "CityName VARCHAR,"
                    + "CityTemp VARCHAR,"
                    + "RequestTime VARCHAR);")
            db.execSQL(CREATE_TABLE_CONTAIN)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertIntoDB(activity: Activity) {
        val db: SQLiteDatabase =
            activity.openOrCreateDatabase("Weather.db", Context.MODE_PRIVATE, null)

        for (city in openWeatherResponse?.list!!) {
            if (city.id == selectedCityID) {
                val sql =
                    "INSERT or replace INTO Cities (CityID, CityName, CityTemp, RequestTime) VALUES(" + city.id + ",'" + city.name + "'," + city.main.temp + ",'" + Calendar.getInstance().getTime() + "')"
                db.execSQL(sql)
                break
            }
        }
        db.close()
    }
}