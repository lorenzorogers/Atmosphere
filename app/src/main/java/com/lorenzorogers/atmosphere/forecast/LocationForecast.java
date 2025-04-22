package com.lorenzorogers.atmosphere.forecast;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.lorenzorogers.atmosphere.network.RequestUtils;
import com.lorenzorogers.atmosphere.network.data.RawWeatherData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record LocationForecast(double latitude, double longitude, String timezone, int elevation, String name, List<TimestampedWeatherData> data) {

    static final Gson GSON = new Gson();

    public static void get(double latitude, double longitude, Consumer<LocationForecast> callback) {
        RequestUtils.fetchForecast(latitude, longitude, response -> {
            RawWeatherData rawData = GSON.fromJson(response, RawWeatherData.class);

            ArrayList<TimestampedWeatherData> forecastList = new ArrayList<>();

            for (int i = 0; i < rawData.hourly().time().size(); i++) {
                float temperature = rawData.hourly().temperature_2m().get(i);
                float windSpeed = rawData.hourly().wind_speed_10m().get(i);
                int visibility = rawData.hourly().visibility().get(i);
                float apparentTemperature = rawData.hourly().apparent_temperature().get(i);
                LocalDateTime timestamp = LocalDateTime.parse(rawData.hourly().time().get(i));
                TimestampedWeatherData newEntry = new TimestampedWeatherData(temperature, windSpeed, visibility, apparentTemperature, timestamp);
                forecastList.add(newEntry);
            }

            LocationForecast forecast = new LocationForecast(latitude, longitude, rawData.timezone(), rawData.elevation(), "Town Name", forecastList);

            Handler handler = new Handler(Looper.getMainLooper());

            Runnable callbackRunnable = () -> callback.accept(forecast);

            handler.post(callbackRunnable);
        });
    }

    public record TimestampedWeatherData(float temperature, float windSpeed, int visibility, float apparentTemperature, LocalDateTime time) {}
}