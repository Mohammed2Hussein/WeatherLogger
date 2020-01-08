package com.weatherlogger.task.presenter;

import android.app.Activity;

import com.weatherlogger.task.R;
import com.weatherlogger.task.core.Singleton;
import com.weatherlogger.task.model.CityByID.CityByIDResponse;
import com.weatherlogger.task.model.serverData.OpenWeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPresenter {
    private String TAG = "MainActivityPresenter";
    private View view;

    public MainActivityPresenter(View view) {
        this.view = view;
    }

    public void getCityByID(final Activity activity, int id) {
        Call<CityByIDResponse> call = Singleton.getInstance().getCache().getApi().getCityByID("metric", id, "3cefdc5b00a7b0f43056ab3a337dcb06");

        call.enqueue(new Callback<CityByIDResponse>() {
            @Override
            public void onResponse(Call<CityByIDResponse> call, Response<CityByIDResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.isSuccessful()) {
                        Singleton.getInstance().getCache().setCityByIDResponse(response.body());
                        view.onGetCityRequestSuccess();
                    } else {
                        view.onGetCityRequestFailedWithError(activity.getString(R.string.find_city_activity_city_request_error));
                    }
                } else {
                    /*try {
                        RestCityErrorResponse restCityErrorResponse = Singleton.getInstance().getCache().getGsonInstance().fromJson(response.errorBody().toString(), RestCityErrorResponse.class);
                        view.onGetCityRequestFailedWithError(restCityErrorResponse.getMessage());
                    } catch (IllegalStateException e) {
                        view.onGetCityRequestFailedWithError(e.getMessage());
                    }*/
                }
            }

            @Override
            public void onFailure(Call<CityByIDResponse> call, Throwable t) {
                view.onGetCityRequestFailedWithError(t.getMessage());
            }
        });
    }

    public interface View {
        void onGetCityRequestSuccess();

        void onGetCityRequestFailedWithError(String msg);

        void onGetCityRequestFailed();
    }
}
