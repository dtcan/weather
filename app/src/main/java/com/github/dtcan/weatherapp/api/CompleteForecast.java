package com.github.dtcan.weatherapp.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CompleteForecast {

    public final Forecast current;
    public final Forecast[] daily;

    private CompleteForecast(Forecast current, Forecast[] daily) {
        this.current = current;
        this.daily = daily;
    }

    public static CompleteForecast fromJSON(JSONObject completeJSON) throws JSONException {
        JSONObject currentJSON = completeJSON.getJSONObject("current");
        Forecast current = Forecast.currentFromJSON(currentJSON);

        JSONArray dailyJSON = completeJSON.getJSONArray("daily");
        Forecast[] daily = new Forecast[dailyJSON.length()];
        for(int i = 0; i < dailyJSON.length(); i++) {
            JSONObject dailyItemJSON = dailyJSON.getJSONObject(i);
            daily[i] = Forecast.dailyFromJSON(dailyItemJSON);
        }

        return new CompleteForecast(current, daily);
    }
}
