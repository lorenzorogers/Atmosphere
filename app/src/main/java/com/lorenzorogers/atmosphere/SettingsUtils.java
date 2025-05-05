package com.lorenzorogers.atmosphere;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsUtils {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_IS_CELSIUS = "isCelsius";

    public static boolean isCelsius(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_CELSIUS, true);
    }

    // Save the unit preference (Celsius or Fahrenheit)
    public static void setCelsius(Context context, boolean isCelsius) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_CELSIUS, isCelsius);
        editor.apply();
    }
}