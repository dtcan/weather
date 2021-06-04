package com.github.dtcan.weatherapp;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView listDaily = findViewById(R.id.list_daily);
        listDaily.setLayoutManager(new LinearLayoutManager(this));
        listDaily.setAdapter(new DailyForecastAdapter(new Forecast[0]));

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadForecast() {
        TextView tvName = findViewById(R.id.name);
        TextView tvTemp = findViewById(R.id.temp);
        TextView tvWeather = findViewById(R.id.weather);
        RecyclerView listDaily = findViewById(R.id.list_daily);

        // TODO: Load city from preferences
        City city = new City("Toronto, CA", 43.7001f, -79.4163f);

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