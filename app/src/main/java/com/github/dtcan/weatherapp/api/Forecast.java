package com.github.dtcan.weatherapp.api;

import com.github.dtcan.weatherapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Forecast {

    public final Calendar date;
    private final double tempCelsius;
    public final Weather weather;
    public final String description;
    private final Icon icon;

    private Forecast(Calendar date, double tempCelsius, Weather weather, String description, Icon icon) {
        this.date = date;
        this.tempCelsius = tempCelsius;
        this.weather = weather;
        this.description = description;
        this.icon = icon;
    }

    public static Forecast currentFromJSON(JSONObject forecastJSON) throws JSONException {
        double tempCelsius = forecastJSON.getDouble("temp");
        return Forecast.fromJSONAndTemp(forecastJSON, tempCelsius);
    }

    public static Forecast dailyFromJSON(JSONObject forecastJSON) throws JSONException {
        JSONObject temps = forecastJSON.getJSONObject("temp");
        double tempCelsius = temps.getDouble("day");
        return Forecast.fromJSONAndTemp(forecastJSON, tempCelsius);
    }

    private static Forecast fromJSONAndTemp(JSONObject forecastJSON, double tempCelsius) throws JSONException {
        long unixSeconds = forecastJSON.getLong("dt");
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(unixSeconds * 1000);

        JSONArray weatherJSONArray = forecastJSON.getJSONArray("weather");
        JSONObject weatherJSON = weatherJSONArray.getJSONObject(0);
        Weather weather = Forecast.weatherFromJSON(weatherJSON);
        String description = weatherJSON.getString("description");
        String iconText = weatherJSON.getString("icon");
        Icon icon;
        switch(iconText) {
            case "01n":
            case "01d":
                icon = Icon.Clear;
                break;
            case "02d":
            case "02n":
                icon = Icon.Cloudy;
                break;
            case "09d":
            case "09n":
                icon = Icon.Rain;
                break;
            case "10d":
            case "10n":
                icon = Icon.Rainy;
                break;
            case "11d":
            case "11n":
                icon = Icon.Lightning;
                break;
            case "13d":
            case "13n":
                icon = Icon.Snow;
                break;
            case "50d":
            case "50n":
                icon = Icon.Mist;
                break;
            default:
                icon = Icon.Cloud;
                break;
        }

        return new Forecast(date, tempCelsius, weather, description, icon);
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
            default:
                return Weather.Unknown;
        }
    }

    public double getTempCelsius() {
        return tempCelsius;
    }

    public double getTempFahrenheit() {
        return (tempCelsius * 1.8) + 32;
    }

    public int getDrawable() {
        switch(icon) {
            case Clear:
                return R.drawable.ic_sun;
            case Cloudy:
                return R.drawable.ic_cloudy;
            case Rain:
                return R.drawable.ic_rain;
            case Rainy:
                return R.drawable.ic_rainy;
            case Lightning:
                return R.drawable.ic_lightning;
            case Snow:
                return R.drawable.ic_snow;
            case Mist:
                return R.drawable.ic_mist;
            default:
                return R.drawable.ic_cloud;
        }
    }
}
