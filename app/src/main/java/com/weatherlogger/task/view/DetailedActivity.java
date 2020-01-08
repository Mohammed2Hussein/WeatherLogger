package com.weatherlogger.task.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weatherlogger.task.R;
import com.weatherlogger.task.core.Singleton;

import org.w3c.dom.Text;

public class DetailedActivity extends AppCompatActivity {
    private TextView txtDetailedCountry, txtDetailedCity, txtDetailedDesc, txtDetailedTemp, txtDetailedMaxTemp, txtDetailedMinTemp, txtDetailedVisibility, txtDetailedWindSpeed;
    private ImageView imgDetailedIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);

        initViews();
    }

    private void initViews() {
        imgDetailedIcon = findViewById(R.id.imgDetailedIcon);
        txtDetailedCountry = findViewById(R.id.txtDetailedCountry);
        txtDetailedCity = findViewById(R.id.txtDetailedCity);
        txtDetailedDesc = findViewById(R.id.txtDetailedDesc);
        txtDetailedTemp = findViewById(R.id.txtDetailedTemp);
        txtDetailedMaxTemp = findViewById(R.id.txtDetailedMaxTemp);
        txtDetailedMinTemp = findViewById(R.id.txtDetailedMinTemp);
        txtDetailedVisibility = findViewById(R.id.txtDetailedVisibility);
        txtDetailedWindSpeed = findViewById(R.id.txtDetailedWindSpeed);

        setViewsValues();
    }

    private void setViewsValues() {
        Glide.with(this).load("http://openweathermap.org/img/wn/" + Singleton.getInstance().getCache().getCityByIDResponse().getWeather().get(0).getIcon() + ".png").into(imgDetailedIcon);

        txtDetailedCountry.setText(getString(R.string.detailed_activity_country) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getSys().getCountry());
        txtDetailedCity.setText(getString(R.string.detailed_activity_city) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getName());
        txtDetailedDesc.setText(getString(R.string.detailed_activity_desc) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getWeather().get(0).getDescription());
        txtDetailedTemp.setText(getString(R.string.detailed_activity_temp) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getMain().getTemp());
        txtDetailedMaxTemp.setText(getString(R.string.detailed_activity_max_temp) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getMain().getTempMax());
        txtDetailedMinTemp.setText(getString(R.string.detailed_activity_min_temp) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getMain().getTempMin());
        txtDetailedVisibility.setText(getString(R.string.detailed_activity_visibilty) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getVisibility());
        txtDetailedWindSpeed.setText(getString(R.string.detailed_activity_wind_speed) + " " + Singleton.getInstance().getCache().getCityByIDResponse().getWind().getSpeed());
    }
}
