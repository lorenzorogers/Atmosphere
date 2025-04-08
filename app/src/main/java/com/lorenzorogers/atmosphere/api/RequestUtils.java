package com.lorenzorogers.atmosphere.api;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestUtils {
    public static final MediaType JSON = MediaType.get("application/json");
    static OkHttpClient client = new OkHttpClient();
    public static String fetch(URL url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException(String.format("Unexpected error in API request to URL %s, %s", url, response));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "hi";
    }
}
