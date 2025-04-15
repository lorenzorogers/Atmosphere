package com.lorenzorogers.atmosphere.network.data;

import java.util.List;

public record RawWeatherData(double latitude, double longitude, double generationtime_ms, float utc_offset_seconds, String timezone, String timezone_abbreviation, int elevation, RawHourlyUnits hourly_units, RawHourlyData hourly) {
    public record RawHourlyUnits(String time, String temperature_2m, String rain, String wind_speed_10m, String visibility, String apparent_temperature) {}

    public record RawHourlyData(List<String> time, List<Float> temperature_2m, List<Float> rain, List<Float> wind_speed_10m, List<Integer> visibility, List<Float> apparent_temperature) {}
}
