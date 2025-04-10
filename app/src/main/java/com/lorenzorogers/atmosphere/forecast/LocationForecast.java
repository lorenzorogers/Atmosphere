package com.lorenzorogers.atmosphere.forecast;

import java.sql.Time;
import java.util.List;

public record LocationForecast(double latitude, double longitude, String timezone, int elevation, String name, List<TimestampedWeatherData> data) {
    public record TimestampedWeatherData(float temperature, float speed, int visibility, float apparentTemperature, Time time) {}
}