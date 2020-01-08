package com.weatherlogger.task.view

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weatherlogger.task.R
import com.weatherlogger.task.adapter.FindCityActivityAdapter
import com.weatherlogger.task.adapter.MainActivityAdapter
import com.weatherlogger.task.core.Singleton
import com.weatherlogger.task.model.StoredCities
import com.weatherlogger.task.presenter.MainActivityPresenter
import java.lang.Exception

class MainActivity : AppCompatActivity(), MainActivityPresenter.View {
    private val TAG = "MainActivity"

    private var txtMainActivityNoDataFound: TextView? = null
    private var recyclerViewMainActivity: RecyclerView? = null
    private var btnMainActivityAddCity: Button? = null
    private var adapter: MainActivityAdapter? = null

    private var mainActivityPresenter: MainActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Singleton.getInstance().cache.initDatabase(this)
        mainActivityPresenter = MainActivityPresenter(this)
        Singleton.getInstance().cache.mainActivity = this

        initViews()

        getAllCitiesFromDB()
    }

    private fun initViews() {
        txtMainActivityNoDataFound = findViewById(R.id.txtMainActivityNoDataFound)
        recyclerViewMainActivity = findViewById(R.id.recyclerViewMainActivity)
        btnMainActivityAddCity = findViewById(R.id.btnMainActivityAddCity)

        txtMainActivityNoDataFound?.visibility = View.VISIBLE
        recyclerViewMainActivity?.visibility = View.GONE

        initRecyclerView()
        setViewsListeners()
    }

    private fun initRecyclerView() {
        adapter = MainActivityAdapter()
        recyclerViewMainActivity?.setLayoutManager(LinearLayoutManager(this))
        recyclerViewMainActivity?.setAdapter(adapter)
    }

    private fun setViewsListeners() {
        btnMainActivityAddCity?.setOnClickListener {
            startActivity(Intent(this@MainActivity, FindCityActivity::class.java))
        }
    }

    public fun requestCityByID(){
        mainActivityPresenter?.getCityByID(this, Singleton.getInstance().cache.selectedCityID)
    }

    private fun getAllCitiesFromDB() {
        Singleton.getInstance().cache.storedCitiesList = ArrayList()

        val db: SQLiteDatabase =
            this.openOrCreateDatabase("Weather.db", Context.MODE_PRIVATE, null)

        try {
            val cursor = db.rawQuery("SELECT  * FROM Cities", null)
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                var storedCities = StoredCities()
                storedCities.cityID = (cursor.getInt(cursor.getColumnIndex("CityID")))
                storedCities.cityName = (cursor.getString(cursor.getColumnIndex("CityName")))
                storedCities.cityTemp = (cursor.getFloat(cursor.getColumnIndex("CityTemp")))
                storedCities.requestTime = (cursor.getString(cursor.getColumnIndex("RequestTime")))
                Log.d(TAG, "storedCities.requestTime: " + storedCities.requestTime)
                Singleton.getInstance().cache.storedCitiesList?.add(storedCities)

                cursor.moveToNext()
            }
            if (Singleton.getInstance().cache.storedCitiesList?.size!! > 0) {
                txtMainActivityNoDataFound?.visibility = View.GONE
                recyclerViewMainActivity?.visibility = View.VISIBLE
                recyclerViewMainActivity?.adapter?.notifyDataSetChanged()
            }
            db.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onGetCityRequestFailedWithError(msg: String?) {

    }

    override fun onGetCityRequestSuccess() {
        startActivity(Intent(this@MainActivity, DetailedActivity::class.java))
    }

    override fun onGetCityRequestFailed() {

    }
}
