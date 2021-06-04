package com.github.dtcan.weatherapp.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Forecast {

    private final double tempCelsius;
    public final Weather weather;
    public final String description;

    private Forecast(double tempCelsius, Weather weather, String description) {
        this.tempCelsius = tempCelsius;
        this.weather = weather;
        this.description = description;
    }

    public static Forecast currentFromJSON(JSONObject forecastJSON) throws JSONException {
        double tempCelsius = forecastJSON.getDouble("temp");

        JSONArray weatherJSONArray = forecastJSON.getJSONArray("weather");
        JSONObject weatherJSON = weatherJSONArray.getJSONObject(0);
        Weather weather = Forecast.weatherFromJSON(weatherJSON);
        String description = weatherJSON.getString("description");

        return new Forecast(tempCelsius, weather, description);
    }

    public static Forecast dailyFromJSON(JSONObject forecastJSON) throws JSONException {
        JSONObject temps = forecastJSON.getJSONObject("temp");
        double tempCelsius = temps.getDouble("day");

        JSONArray weatherJSONArray = forecastJSON.getJSONArray("weather");
        JSONObject weatherJSON = weatherJSONArray.getJSONObject(0);
        Weather weather = Forecast.weatherFromJSON(weatherJSON);
        String description = weatherJSON.getString("description");

        return new Forecast(tempCelsius, weather, description);
    }

    private static Weather weatherFromJSON(JSONObject weatherJSON) throws JSONException {
        String weatherName = weatherJSON.getString("main");

        switch(weatherName) {
            case "Clear":
                return Weather.Clear;
            case "Clouds":
                return Weather.Clouds;
            case "Thunderstorm":
                return Weather.Thunderstorm;
            case "Drizzle":
                return Weather.Drizzle;
            case "Rain":
                return Weather.Rain;
            case "Snow":
                return Weather.Snow;
            case "Mist":
                return Weather.Mist;
            case "Smoke":
                return Weather.Smoke;
            case "Haze":
                return Weather.Haze;
            case "Dust":
                return Weather.Dust;
            case "Fog":
                return Weather.Fog;
            case "Sand":
                return Weather.Sand;
            case "Ash":
                return Weather.Ash;
            case "Squall":
                return Weather.Squall;
            case "Tornado":
                return Weather.Tornado;
        }
        return Weather.Unknown;
    }

    public double getTempCelsius() {
        return tempCelsius;
    }

    public double getTempFahrenheit() {
        return (tempCelsius * 1.8) + 32;
    }
}
