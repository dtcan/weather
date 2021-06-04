package com.github.dtcan.weatherapp.api;

import org.json.JSONException;
import org.json.JSONObject;

public class City {

    public final String name;
    public final double lat, lon;

    public City(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public static City fromJSON(JSONObject cityJSON) throws JSONException {
        String name = cityJSON.getString("name");
        String country = cityJSON.getString("country");
        double lat = cityJSON.getDouble("lat");
        double lon = cityJSON.getDouble("lon");

        String state = "";
        try {
            state = cityJSON.getString("state");
        }catch(JSONException ignored) {} // If excepted, then state = ""

        if(state.length() > 0) {
            name += ", " + state;
        }
        name += ", " + country;

        return new City(name, lat, lon);
    }

}
