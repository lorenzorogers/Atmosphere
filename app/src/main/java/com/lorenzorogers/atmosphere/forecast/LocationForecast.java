package com.lorenzorogers.atmosphere.forecast;

import java.sql.Time;
import java.util.List;

public record LocationForecast(double latitude, double longitude, String timezone, int elevation, String name, List<TemperatureEntry> temperatures) {
    public record TemperatureEntry(float temperature, Time time) {}
    public record WindSpeedEntry(float speed, Time time) {}
    public record VisibilityEntry(int visibility, Time time) {}
    public record ApparentTemperatureEntry(float apparentTemperature, Time time) {}
}