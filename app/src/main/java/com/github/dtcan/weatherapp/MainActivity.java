package com.github.dtcan.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
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

    private boolean useFahrenheit = false;
    private Forecast currentForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView listDaily = findViewById(R.id.list_daily);
        listDaily.setLayoutManager(new LinearLayoutManager(this));
        listDaily.setAdapter(new DailyForecastAdapter(new Forecast[0], useFahrenheit));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadForecast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_reload) {
            loadForecast();
            return true;
        }else if(item.getItemId() == R.id.item_city) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if(item.getItemId() == R.id.item_temp) {
            useFahrenheit = !useFahrenheit;
            RecyclerView listDaily = findViewById(R.id.list_daily);
            DailyForecastAdapter listAdapter = (DailyForecastAdapter) listDaily.getAdapter();
            if(listAdapter != null) {
                listAdapter.useFahrenheit(useFahrenheit);
            }
            updateForecastView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadForecast() {
        RecyclerView listDaily = findViewById(R.id.list_daily);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), MODE_PRIVATE);
        String name = preferences.getString(getString(R.string.preferences_name), "Toronto, CA");
        float lat = preferences.getFloat(getString(R.string.preferences_lat), -79.4163f);
        float lon = preferences.getFloat(getString(R.string.preferences_lon), 43.7001f);
        City city = new City(name, lat, lon);

        API api = API.getInstance(this);
        api.getCompleteForecast(city, new ResponseHandler<CompleteForecast>() {
            @Override
            public void onResponse(CompleteForecast response) {
                currentForecast = response.current;
                updateForecastView();
                listDaily.setAdapter(new DailyForecastAdapter(response.daily, useFahrenheit));
            }

            @Override
            public void onError(int statusCode, String error) {
                currentForecast = null;
                updateForecastView();
                listDaily.setAdapter(new DailyForecastAdapter(new Forecast[0], useFahrenheit));
            }
        });
    }

    private void updateForecastView() {
        ImageView image = findViewById(R.id.image);
        TextView tvDate = findViewById(R.id.date);
        TextView tvName = findViewById(R.id.name);
        TextView tvTemp = findViewById(R.id.temp);
        TextView tvWeather = findViewById(R.id.weather);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), MODE_PRIVATE);
        String name = preferences.getString(getString(R.string.preferences_name), "Toronto, CA");
        tvName.setText(name);

        if(currentForecast != null) {
            image.setImageDrawable(getDrawable(currentForecast.getDrawable()));
            tvDate.setText(format(Locale.getDefault(), "%1$tA, %1$tb %1$td", currentForecast.date));
            tvWeather.setText(format(Locale.getDefault(), "%s, %s", currentForecast.weather.toString(), currentForecast.description));

            if(useFahrenheit) {
                tvTemp.setText(format(Locale.getDefault(), "%.1f °F", currentForecast.getTempFahrenheit()));
            } else {
                tvTemp.setText(format(Locale.getDefault(), "%.1f °C", currentForecast.getTempCelsius()));
            }
        }else {
            image.setImageDrawable(getDrawable(R.drawable.ic_cloud));
            tvDate.setText("");
            tvTemp.setText("");
            tvWeather.setText(getString(R.string.error));
        }
    }
}