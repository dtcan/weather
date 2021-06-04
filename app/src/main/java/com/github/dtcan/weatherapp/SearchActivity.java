package com.github.dtcan.weatherapp;

import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.dtcan.weatherapp.adapter.CityListAdapter;
import com.github.dtcan.weatherapp.api.API;
import com.github.dtcan.weatherapp.api.City;
import com.github.dtcan.weatherapp.api.ResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity {

    private Timer searchTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView listCities = findViewById(R.id.list_cities);
        listCities.setLayoutManager(new LinearLayoutManager(this));
        listCities.setAdapter(new CityListAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                scheduleSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void scheduleSearch(String query) {
        int SEARCH_DELAY_MS = 1000;

        if(searchTimer != null) {
            searchTimer.cancel();
        }
        searchTimer = new Timer("searchTimer");
        searchTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadResults(query);
            }
        }, SEARCH_DELAY_MS);
    }

    private void loadResults(String query) {
        RecyclerView listCities = findViewById(R.id.list_cities);
        CityListAdapter listAdapter = (CityListAdapter) listCities.getAdapter();
        if(listAdapter == null) {
            return;
        }

        API api = API.getInstance(this);
        api.searchCities(query, new ResponseHandler<List<City>>() {
            @Override
            public void onResponse(List<City> response) {
                listAdapter.setCities(response);
            }

            @Override
            public void onError(int statusCode, String error) {
                listAdapter.clearCities();
            }
        });
    }
}