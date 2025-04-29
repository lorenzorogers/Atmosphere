package com.lorenzorogers.atmosphere.network;

import static com.lorenzorogers.atmosphere.network.RequestUtils.GSON;

import java.util.List;
import java.util.function.Consumer;

public class Geocoder {

    public static record GeocodingResults(List<GeocodingEntry> results) {}
    public record GeocodingEntry(
            int id,
            String name,
            double latitude,
            double longitude,
            int elevation,
            String feature_code,
            String country_code,
            String admin1_id,
            String admin2_id,
            String timezone,
            int population,
            int country_id,
            String country,
            String admin1,
            String admin2
    ) {}

    public static void getFirst(String query, Consumer<GeocodingEntry> results) {
        RequestUtils.fetchGeocoder(query, data -> {
            GeocodingResults rawData = GSON.fromJson(data, Geocoder.GeocodingResults.class);

            results.accept(rawData.results.get(0));
        });
    }
}
