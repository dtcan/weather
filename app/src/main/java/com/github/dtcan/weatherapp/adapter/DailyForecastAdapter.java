package com.github.dtcan.weatherapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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

        private final TextView tvDate, tvTemp, tvWeather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvDate = itemView.findViewById(R.id.daily_date);
            this.tvTemp = itemView.findViewById(R.id.daily_temp);
            this.tvWeather = itemView.findViewById(R.id.daily_weather);
        }

        public void bindView(Forecast forecastDay) {
            this.tvDate.setText(forecastDay.date.getDisplayName(
                    Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()
            ));
            this.tvTemp.setText(format(Locale.getDefault(), "%.1f deg C", forecastDay.getTempCelsius()));
            this.tvWeather.setText(format(Locale.getDefault(), "%s, %s", forecastDay.weather.toString(), forecastDay.description));
        }

    }
}
