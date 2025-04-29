package com.lorenzorogers.atmosphere.forecast;

import static com.lorenzorogers.atmosphere.network.RequestUtils.GSON;

import android.os.Handler;
import android.os.Looper;

import com.lorenzorogers.atmosphere.network.RequestUtils;
import com.lorenzorogers.atmosphere.network.Geocoder;
import com.lorenzorogers.atmosphere.network.data.RawWeatherData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Consumer;

public record LocationForecast(double latitude, double longitude, String timezone, String timezoneAbbr, int elevation, Geocoder.GeocodingEntry data, CurrentWeatherData current, List<TimestampedWeatherData> hourly) {

    public static void get(double latitude, double longitude, Geocoder.GeocodingEntry locationData, Consumer<LocationForecast> callback) {
        RequestUtils.fetchForecast(latitude, longitude, TimeZone.getDefault(), response -> {
            RawWeatherData rawData = GSON.fromJson(response, RawWeatherData.class);

            CurrentWeatherData currentWeatherData = new CurrentWeatherData(
                    rawData.current().temperature_2m(),
                    rawData.current().is_day() == 1,
                    rawData.current().apparent_temperature(),
                    rawData.current().relative_humidity_2m(),
                    rawData.current().precipitation(),
                    rawData.current().surface_pressure(),
                    rawData.current().wind_speed_10m()
            );

            ArrayList<TimestampedWeatherData> forecastList = new ArrayList<>();

            for (int i = 0; i < rawData.hourly().time().size(); i++) {
                float temperature = rawData.hourly().temperature_2m().get(i);
                float rain = rawData.hourly().rain().get(i);
                float windSpeed = rawData.hourly().wind_speed_10m().get(i);
                int visibility = rawData.hourly().visibility().get(i);
                float apparentTemperature = rawData.hourly().apparent_temperature().get(i);
                float relativeHumidity = rawData.hourly().relative_humidity_2m().get(i);
                float surfacePressure = rawData.hourly().surface_pressure().get(i);
                LocalDateTime timestamp = LocalDateTime.parse(rawData.hourly().time().get(i));
                TimestampedWeatherData newEntry = new TimestampedWeatherData(temperature, rain, windSpeed, visibility, apparentTemperature, relativeHumidity, surfacePressure, timestamp);
                forecastList.add(newEntry);
            }

            LocationForecast forecast = new LocationForecast(latitude, longitude, rawData.timezone(), rawData.timezone_abbreviation(), rawData.elevation(), locationData, currentWeatherData, forecastList);

            Handler handler = new Handler(Looper.getMainLooper());

            Runnable callbackRunnable = () -> callback.accept(forecast);

            handler.post(callbackRunnable);
        });
    }

    public record TimestampedWeatherData(float temperature, float rain, float windSpeed, int visibility, float apparentTemperature, float relativeHumidity, float surfacePressure, LocalDateTime time) {}
    public record CurrentWeatherData(float temperature, boolean isDay, float apparentTemperature, int relativeHumidity, float precipitation, float surfacePressure, float windSpeed) {}
}