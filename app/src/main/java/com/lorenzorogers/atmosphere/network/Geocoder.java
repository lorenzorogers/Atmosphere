package com.lorenzorogers.atmosphere.network;

import static com.lorenzorogers.atmosphere.network.RequestUtils.GSON;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Geocoder {

    public record GeocodingResults(List<GeocodingEntry> results) {
    }

    public record Location(
            double latitude,
            double longitude,
            String name,
            String countryCode,
            String country,
            String region,
            String district
    ) {}

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
    ) {
    }

    public static void getFirst(String query, Consumer<GeocodingEntry> results) {
        RequestUtils.fetchGeocoder(query, data -> {
            GeocodingResults rawData = GSON.fromJson(data, Geocoder.GeocodingResults.class);

            Handler handler = new Handler(Looper.getMainLooper());
            Runnable callbackRunnable = () -> results.accept(rawData.results.get(0));
            handler.post(callbackRunnable);
        });
    }

    public static void get(String query, Consumer<GeocodingResults> results) {
        RequestUtils.fetchGeocoder(query, data -> {
            GeocodingResults rawData = GSON.fromJson(data, GeocodingResults.class);
            GeocodingResults emptyData = new GeocodingResults(new ArrayList<>());

            Handler handler = new Handler(Looper.getMainLooper());
            Runnable callbackRunnable = () -> results.accept(rawData.results() != null ? rawData : emptyData);
            handler.post(callbackRunnable);
        });
    }

    public static void get(double latitude, double longitude, Consumer<GeocodingEntry> result) {
        RequestUtils.fetchReverseGeocoder(latitude, longitude, data -> {
            RawReverseGeocodedLocation rawData = GSON.fromJson(data, RawReverseGeocodedLocation.class);

            GeocodingEntry parsedResult = new GeocodingEntry(
                    0,
                    rawData.address().city(),
                    rawData.lat(),
                    rawData.lon(),
                    0,
                    "null",
                    rawData.address().country_code(),
                    "null",
                    "null",
                    "null",
                    0,
                    0,
                    rawData.address().country(),
                    rawData.address().county(),
                    rawData.address().state()
            );

            Handler handler = new Handler(Looper.getMainLooper());
            Runnable callbackRunnable = () -> result.accept(parsedResult);
            handler.post(callbackRunnable);
        });
    }

    public record Coordinates(double longitude, double latitude) {}

    public record RawReverseGeocodedLocation(
            int place_id,
            String license,
            String osm_type,
            int osm_id,
            double lat,
            double lon,
            String type,
            int place_rank,
            double importance,
            String addresstype,
            String name,
            String display_name,
            RawReverseGeocodedAddress address,
            List<String> boundingbox) {
        public record RawReverseGeocodedAddress(
                String amenity,
                String house_number,
                String road,
                String city,
                String county,
                String state,
                String postcode,
                String country,
                String country_code) {}
    }
}
