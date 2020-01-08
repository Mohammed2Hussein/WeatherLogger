package com.weatherlogger.task.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weatherlogger.task.R;
import com.weatherlogger.task.core.Singleton;
import com.weatherlogger.task.model.serverData.ListItem;
import com.weatherlogger.task.view.FindCityActivity;

public class FindCityActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private FindCityActivity activity;
    private RelativeLayout lastRelativeLayoutSingleCityRow;

    public FindCityActivityAdapter(FindCityActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_city_row, parent, false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItem currentItem = Singleton.getInstance().getCache().getOpenWeatherResponse().getList().get(position);

        ImageView imgCityRowWeatherIcon = holder.itemView.findViewById(R.id.imgCityRowWeatherIcon);
        TextView txtCityRowCountryName = holder.itemView.findViewById(R.id.txtCityRowCountryName);
        TextView txtCityRowCityName = holder.itemView.findViewById(R.id.txtCityRowCityName);
        ImageView imgCityRowCountryFlag = holder.itemView.findViewById(R.id.imgCityRowCountryFlag);
        TextView txtCityRowFlagScattered = holder.itemView.findViewById(R.id.txtCityRowFlagScattered);
        TextView txtCityRowTemp = holder.itemView.findViewById(R.id.txtCityRowTemp);

        RelativeLayout relativeLayoutSingleCityRow = holder.itemView.findViewById(R.id.relativeLayoutSingleCityRow);

        Glide.with(activity).load("http://openweathermap.org/img/wn/" + currentItem.getWeather().get(0).getIcon() + ".png").into(imgCityRowWeatherIcon);
        Glide.with(activity).load("https://openweathermap.org/images/flags/" + currentItem.getSys().getCountry().toLowerCase() + ".png").into(imgCityRowCountryFlag);

        txtCityRowCountryName.setText(activity.getString(R.string.find_city_activity_country_name_title) + " " + currentItem.getSys().getCountry());
        txtCityRowCityName.setText(activity.getString(R.string.find_city_activity_city_name_title) + " " + currentItem.getName());
        txtCityRowFlagScattered.setText(currentItem.getWeather().get(0).getDescription());
        txtCityRowTemp.setText(activity.getString(R.string.find_city_activity_temp_title) + " " + (int) currentItem.getMain().getTemp() + " \u2109");

        relativeLayoutSingleCityRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastRelativeLayoutSingleCityRow != null)
                    lastRelativeLayoutSingleCityRow.setBackgroundResource(R.drawable.colored_bg_rounded_corners);
                relativeLayoutSingleCityRow.setBackgroundResource(R.drawable.colored_selected_bg_rounded_corners);
                lastRelativeLayoutSingleCityRow = relativeLayoutSingleCityRow;
                activity.enableSaveButton(true);
                Singleton.getInstance().getCache().setSelectedCityID(currentItem.getId());
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return Singleton.getInstance().getCache().getOpenWeatherResponse().getCount();
    }
}
