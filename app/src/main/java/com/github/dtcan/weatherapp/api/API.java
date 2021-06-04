package com.github.dtcan.weatherapp.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;

@SuppressLint("StaticFieldLeak")
public class API {

    private static final String API_KEY = "a94ace9be7994ce0beb8b5e27cefcd7b";
    private static final int SEARCH_LIMIT = 100;

    private static API instance;
    private static Context context;
    private final RequestQueue requestQueue;

    private API(Context contextArg) {
        context = contextArg;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized API getInstance(Context context) {
        if(instance == null) {
            instance = new API(context);
        }
        return instance;
    }

    public void getCompleteForecast(City city, ResponseHandler<CompleteForecast> handler) {
        String endpointFormat = "https://api.openweathermap.org/data/2.5/onecall?lat=%f&lon=%f&exclude=minutely,hourly&units=metric&appid=%s";
        String endpoint = format(Locale.getDefault(), endpointFormat, city.lat, city.lon, API_KEY);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, endpoint, null, responseJSON -> {
            try {
                CompleteForecast forecast = CompleteForecast.fromJSON(responseJSON);
                handler.onResponse(forecast);
            }catch(JSONException e) {
                Log.e("API", e.toString());
                handler.onError(e.getMessage());
            }
        }, e -> {
            Log.e("API", e.toString());
            handler.onError(e.getMessage());
        });

        requestQueue.add(request);
    }

    public void searchCities(String query, ResponseHandler<List<City>> handler) {
        String endpointFormat = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=%d&appid=%s";
        String endpoint = format(Locale.getDefault(), endpointFormat, query, SEARCH_LIMIT, API_KEY);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, endpoint, null, responseJSON -> {
            List<City> response = new ArrayList<>();
            for (int i = 0; i < responseJSON.length(); i++) {
                try {
                    JSONObject cityJSON = responseJSON.getJSONObject(i);
                    City city = City.fromJSON(cityJSON);
                    response.add(city);
                } catch (JSONException e) {
                    Log.e("API", e.toString());
                    handler.onError(e.getMessage());
                }
            }
            handler.onResponse(response);
        }, e -> {
            Log.e("API", e.toString());
            handler.onError(e.getMessage());
        });

        requestQueue.add(request);
    }

}
