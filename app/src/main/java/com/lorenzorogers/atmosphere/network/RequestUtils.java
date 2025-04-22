package com.lorenzorogers.atmosphere.network;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

// THIS MUST RUN ASYNC
public class RequestUtils {
    public static final MediaType JSON = MediaType.get("application/json");
    static OkHttpClient client = new OkHttpClient();

    public static void fetch(String url, Consumer<String> callback) {
        try {
            fetch(new URL(url), callback);
        } catch (MalformedURLException e) {
            System.out.printf("Failed to fetch URL %s", url);
        }
    }

    public static void fetch(URL url, Consumer<String> callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Log.println(Log.DEBUG, "Atmosphere", String.format("Sending fetch to %s", url));

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful() || responseBody == null) {
                        throw new IOException(String.format("Unexpected error in API request to URL %s, %s", url, responseBody));
                    }

                    callback.accept(responseBody.string());
                }
            }
        });
    }

    public static void fetchForecast(double latitude, double longitude, Consumer<String> callback) {
        String requestUrl = String.format("https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m,rain,wind_speed_10m,visibility,apparent_temperature&timezone=America%%2FLos_Angeles", latitude, longitude);
        fetch(requestUrl, callback);
    }
}
