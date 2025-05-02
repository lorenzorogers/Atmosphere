package com.lorenzorogers.atmosphere.forecast;

public class ForecastUtils {
    public static LocationForecast convertToFahrenheit(LocationForecast forecast) {
        LocationForecast.CurrentWeatherData currentData = forecast.current();

        float convertedTemperature = (currentData.temperature() * 9) / 5 + 32;
        float convertedApparentTemperature = (currentData.apparentTemperature() * 9) / 5 + 32;
        float convertedWindSpeed = (float) (currentData.windSpeed() * 0.621);
        float convertedPrecipitation = (float) (currentData.precipitation() / 25.4);

        LocationForecast.CurrentWeatherData convertedCurrentData = new LocationForecast.CurrentWeatherData(
                convertedTemperature,
                currentData.isDay(),
                convertedApparentTemperature,
                currentData.relativeHumidity(),
                convertedPrecipitation,
                currentData.surfacePressure(),
                convertedWindSpeed,
                currentData.cloudCover()
        );

        return new LocationForecast(
                forecast.latitude(),
                forecast.longitude(),
                forecast.timezone(),
                forecast.timezoneAbbr(),
                forecast.elevation(),
                forecast.data(),
                convertedCurrentData,
                forecast.hourly()
        );
    }
}
