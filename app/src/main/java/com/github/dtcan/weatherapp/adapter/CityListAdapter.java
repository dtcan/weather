package com.github.dtcan.weatherapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.dtcan.weatherapp.MainActivity;
import com.github.dtcan.weatherapp.R;
import com.github.dtcan.weatherapp.api.City;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<City> cities;

    public CityListAdapter() {
        cities = new ArrayList<>();
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }

    public void clearCities() {
        cities.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = cities.get(position);
        holder.bindView(city);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bindView(City city) {
            TextView tvName = itemView.findViewById(R.id.city_name);
            tvName.setText(city.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity) view.getContext();

                    SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.preferences_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(activity.getString(R.string.preferences_name), city.name);
                    editor.putFloat(activity.getString(R.string.preferences_lat), city.lat);
                    editor.putFloat(activity.getString(R.string.preferences_lon), city.lon);
                    editor.apply();

                    activity.finish();
                }
            });
        }
    }
}
