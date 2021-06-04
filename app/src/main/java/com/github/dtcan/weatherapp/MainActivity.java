package com.github.dtcan.weatherapp;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.dtcan.weatherapp.adapter.DailyForecastAdapter;
import com.github.dtcan.weatherapp.api.*;

import java.util.Locale;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView listDaily = findViewById(R.id.list_daily);
        listDaily.setLayoutManager(new LinearLayoutManager(this));
        listDaily.setAdapter(new DailyForecastAdapter(new Forecast[0]));

        loadForecast();
    }

    private void loadForecast() {
        TextView tvName = findViewById(R.id.name);
        TextView tvTemp = findViewById(R.id.temp);
        TextView tvWeather = findViewById(R.id.weather);
        RecyclerView listDaily = findViewById(R.id.list_daily);

        // TODO: Load city from preferences
        City city = new City("Toronto, CA", 43.7001, -79.4163);

        API api = API.getInstance(this);
        api.getCompleteForecast(city, new ResponseHandler<CompleteForecast>() {
            @Override
            public void onResponse(CompleteForecast response) {
                Forecast current = response.current;
                tvName.setText(city.name);
                tvTemp.setText(format(Locale.getDefault(), "%.1f deg C", current.getTempCelsius()));
                tvWeather.setText(format(Locale.getDefault(), "%s, %s", current.weather.toString(), current.description));
                listDaily.setAdapter(new DailyForecastAdapter(response.daily));
            }

            @Override
            public void onError(int statusCode, String error) {
                tvName.setText(format(Locale.getDefault(), "%d", statusCode));
                tvTemp.setText(error);
                tvWeather.setText("");
                listDaily.setAdapter(new DailyForecastAdapter(new Forecast[0]));
            }
        });
    }
}