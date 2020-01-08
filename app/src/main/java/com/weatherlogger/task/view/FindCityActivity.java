package com.weatherlogger.task.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.Listener;
import com.example.easywaylocation.LocationData;
import com.weatherlogger.task.R;
import com.weatherlogger.task.adapter.FindCityActivityAdapter;
import com.weatherlogger.task.core.Singleton;
import com.weatherlogger.task.presenter.FindCityActivityPresenter;

import java.util.List;

import static com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE;

public class FindCityActivity extends AppCompatActivity implements FindCityActivityPresenter.View, Listener {
    private EditText etxtFindCityActivityCityName;
    private TextView txtFindCityActivityNoDataFound;
    private Button btnFindCityActivityFindCity, btnFindCityActivitySave, btnFindCityActivityCancel;
    private ImageButton ibtnFindCityActivityFindLocation;
    private RecyclerView recyclerViewFindCity;

    private FindCityActivityAdapter adapter;

    private FindCityActivityPresenter findCityActivityPresenter;

    EasyWayLocation easyWayLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_city_activity);

        easyWayLocation = new EasyWayLocation(this, false, this);

        findCityActivityPresenter = new FindCityActivityPresenter(this);

        initViews();
    }

    private void initViews() {
        recyclerViewFindCity = findViewById(R.id.recyclerViewFindCity);
        etxtFindCityActivityCityName = findViewById(R.id.etxtFindCityActivityCityName);
        txtFindCityActivityNoDataFound = findViewById(R.id.txtFindCityActivityNoDataFound);
        ibtnFindCityActivityFindLocation = findViewById(R.id.ibtnFindCityActivityFindLocation);
        btnFindCityActivityFindCity = findViewById(R.id.btnFindCityActivityFindCity);
        btnFindCityActivitySave = findViewById(R.id.btnFindCityActivitySave);
        enableSaveButton(false);
        btnFindCityActivityCancel = findViewById(R.id.btnFindCityActivityCancel);

        initRecyclerView();
        setViewsListeners();
    }

    private void initRecyclerView() {
        adapter = new FindCityActivityAdapter(this);
        recyclerViewFindCity.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFindCity.setAdapter(adapter);
    }

    private void setViewsListeners() {
        btnFindCityActivityCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FindCityActivity.this, MainActivity.class));
            }
        });

        btnFindCityActivityFindCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCityNotEmpty()) {
                    findCityActivityPresenter.getCityByName(FindCityActivity.this, etxtFindCityActivityCityName.getText().toString().trim());
                }
            }
        });

        ibtnFindCityActivityFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfLocationPermissionGranted();
            }
        });

        btnFindCityActivitySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.getInstance().getCache().insertIntoDB(FindCityActivity.this);
                startActivity(new Intent(FindCityActivity.this, MainActivity.class));
            }
        });
    }

    public void enableSaveButton(Boolean isEnabled) {
        btnFindCityActivitySave.setEnabled(isEnabled);
        if (isEnabled)
            btnFindCityActivitySave.setBackgroundResource(R.drawable.button_colored_bg_rounded_corners);
        else
            btnFindCityActivitySave.setBackgroundResource(R.drawable.button_disabled_colored_bg_rounded_corners);
    }

    private boolean isCityNotEmpty() {
        if (etxtFindCityActivityCityName.getText().toString().trim().length() == 0) {
            etxtFindCityActivityCityName.requestFocus();
            etxtFindCityActivityCityName.setError(getString(R.string.find_city_activity_city_name_is_empty));
            return false;
        } else {
            return true;
        }
    }

    private void checkIfLocationPermissionGranted() {
        if (permissionIsGranted()) {
            easyWayLocation.startLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    public boolean permissionIsGranted() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void locationOn() {
        Toast.makeText(this, "Location ON", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void currentLocation(Location location) {
        Log.d(TAG, "Current Location Coodrinates -> Lat: " + location.getLatitude() + " - Long" + location.getLongitude());
        findCityActivityPresenter.getCityByCoordinates(this, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
    }

    @Override
    public void locationCancelled() {
        Toast.makeText(this, "Location Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCATION_SETTING_REQUEST_CODE:
                easyWayLocation.onActivityResult(resultCode);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (easyWayLocation != null)
            easyWayLocation.endUpdates();
    }

    /*----------------------------------Rest Country Response----------------------------------*/
    @Override
    public void onGetCityRequestSuccess() {
        txtFindCityActivityNoDataFound.setVisibility(View.GONE);
        recyclerViewFindCity.setVisibility(View.VISIBLE);
        recyclerViewFindCity.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onGetCityRequestFailedWithError(String msg) {
        showNoDataFound();
        txtFindCityActivityNoDataFound.setText(msg);
    }

    @Override
    public void onGetCityRequestFailed() {
        showNoDataFound();
    }

    private void showNoDataFound() {
        txtFindCityActivityNoDataFound.setVisibility(View.VISIBLE);
        recyclerViewFindCity.setVisibility(View.GONE);
        enableSaveButton(false);
    }
}
