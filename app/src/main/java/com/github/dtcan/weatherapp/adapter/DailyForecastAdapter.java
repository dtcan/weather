package com.github.dtcan.weatherapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.github.dtcan.weatherapp.R;
import com.github.dtcan.weatherapp.api.Forecast;

import java.util.Calendar;
import java.util.Locale;

import static java.lang.String.format;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.ViewHolder> {

    private final Forecast[] daily;

    public DailyForecastAdapter(Forecast[] daily) {
        this.daily = daily;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Forecast forecastDay = daily[position];
        holder.bindView(forecastDay);
    }

    @Override
    public int getItemCount() {
        int MAX_DAYS = 5;
        return Math.min(daily.length, MAX_DAYS);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bindView(Forecast forecastDay) {
            Context context = itemView.getContext();

            ImageView image = itemView.findViewById(R.id.daily_image);
            TextView tvDate = itemView.findViewById(R.id.daily_date);
            TextView tvTemp = itemView.findViewById(R.id.daily_temp);
            TextView tvWeather = itemView.findViewById(R.id.daily_weather);

            image.setImageDrawable(context.getDrawable(forecastDay.getDrawable()));
            tvDate.setText(format(Locale.getDefault(), "%1$tA, %1$tb %1$td", forecastDay.date));
            tvTemp.setText(format(Locale.getDefault(), "%.1f °C", forecastDay.getTempCelsius()));
            tvWeather.setText(format(Locale.getDefault(), "%s, %s", forecastDay.weather.toString(), forecastDay.description));
        }

    }
}
