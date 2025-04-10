package com.lorenzorogers.atmosphere.network;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
// THIS MUST RUN ASYNC
public class RequestUtils {
    public static final MediaType JSON = MediaType.get("application/json");
    static OkHttpClient client = new OkHttpClient();

    public static String fetch(String url, Consumer<String> callback) {
        try {
            return fetch(new URL(url));
        } catch (MalformedURLException e) {
            return String.format("Failed to fetch URL %s", url);
        }
    }
    public static String fetch(URL url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException(String.format("Unexpected error in API request to URL %s, %s", url, response));
            }

            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
