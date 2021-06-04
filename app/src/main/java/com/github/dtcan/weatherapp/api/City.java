package com.github.dtcan.weatherapp.api;

import org.json.JSONException;
import org.json.JSONObject;

public class City {

    public final String name;
    public final float lat, lon;

    public City(String name, float lat, float lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public static City fromJSON(JSONObject cityJSON) throws JSONException {
        String name = cityJSON.getString("name");
        String state = cityJSON.optString("state");
        String country = cityJSON.getString("country");
        float lat = (float) cityJSON.getDouble("lat");
        float lon = (float) cityJSON.getDouble("lon");

        if(state.length() > 0) {
            name += ", " + state;
        }
        name += ", " + country;

        return new City(name, lat, lon);
    }

}
