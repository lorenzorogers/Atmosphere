package com.lorenzorogers.atmosphere.network.data;

import java.util.List;

public record RawWeatherData(double latitude, double longitude, double generationtime_ms, float utc_offset_seconds, String timezone, String timezone_abbreviation, int elevation, RawCurrentUnits current_units, RawCurrentData current, RawHourlyUnits hourly_units, RawHourlyData hourly) {
    public record RawCurrentUnits(String time, String temperature_2m, String is_day, String apparent_temperature, String relative_humidity_2m, String precipitation, String surface_pressure, String wind_speed_10m, String cloud_cover) {}
    public record RawCurrentData(String time, float temperature_2m, int is_day, float apparent_temperature, int relative_humidity_2m, float precipitation, float surface_pressure, float wind_speed_10m, int cloud_cover) {}
    public record RawHourlyUnits(String time, String temperature_2m, String rain, String wind_speed_10m, String visibility, String apparent_temperature) {}
    public record RawHourlyData(List<String> time, List<Float> temperature_2m, List<Float> rain, List<Float> wind_speed_10m, List<Integer> visibility, List<Float> apparent_temperature, List<Integer> relative_humidity_2m, List<Float> surface_pressure) {}
}
