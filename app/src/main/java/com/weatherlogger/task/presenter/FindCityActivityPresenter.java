package com.weatherlogger.task.presenter;

import android.app.Activity;

import com.weatherlogger.task.R;
import com.weatherlogger.task.core.Singleton;
import com.weatherlogger.task.model.serverData.OpenWeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindCityActivityPresenter {
    private String TAG = "FindCityActivityPresenter";
    private View view;

    public FindCityActivityPresenter(View view) {
        this.view = view;
    }

    public void getCityByName(final Activity activity, String cityName) {
        Call<OpenWeatherResponse> call = Singleton.getInstance().getCache().getApi().getCityByName("json", "metric", "like", cityName, "3cefdc5b00a7b0f43056ab3a337dcb06");

        call.enqueue(new Callback<OpenWeatherResponse>() {
            @Override
            public void onResponse(Call<OpenWeatherResponse> call, Response<OpenWeatherResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.isSuccessful()) {
                        Singleton.getInstance().getCache().setOpenWeatherResponse(response.body());
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
            public void onFailure(Call<OpenWeatherResponse> call, Throwable t) {
                view.onGetCityRequestFailedWithError(t.getMessage());
            }
        });
    }

    public void getCityByCoordinates(final Activity activity, String lat, String lon) {
        Call<OpenWeatherResponse> call = Singleton.getInstance().getCache().getApi().getCityByCoordinates( "metric", lat, lon, "3cefdc5b00a7b0f43056ab3a337dcb06");

        call.enqueue(new Callback<OpenWeatherResponse>() {
            @Override
            public void onResponse(Call<OpenWeatherResponse> call, Response<OpenWeatherResponse> response) {
                if (response.body() != null && response.code() == 200) {
                    if (response.isSuccessful()) {
                        Singleton.getInstance().getCache().setOpenWeatherResponse(response.body());
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
            public void onFailure(Call<OpenWeatherResponse> call, Throwable t) {
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
