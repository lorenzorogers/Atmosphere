package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.content.Intent;
import android.widget.ImageView;
import androidx.core.view.WindowInsetsCompat;

import com.lorenzorogers.atmosphere.forecast.ForecastUtils;
import com.lorenzorogers.atmosphere.forecast.LocationForecast;
import com.lorenzorogers.atmosphere.network.Geocoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        boolean isCelsius = SettingsUtils.isCelsius(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button navigates to HomeActivity
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        Geocoder.getFirst("Vancouver", results -> {
            LocationForecast.get(results.latitude(), results.longitude(), results, forecast -> {
                TextView temperatureText = findViewById(R.id.temperatureValue);
                TextView windSpeedText = findViewById(R.id.windSpeedValue);
                TextView visibilityText = findViewById(R.id.visibilityValue);
                TextView apparentTempText = findViewById(R.id.apparentTempValue);
                TextView cloudCoverText = findViewById(R.id.cloudCoverValue);

                TextView cityNameText = findViewById(R.id.cityValue);
                TextView countryNameText = findViewById(R.id.countryValue);

                // MAKE THIS.. BETTER, LoR!

                LocationForecast stupidAmericanValues = ForecastUtils.convertToFahrenheit(forecast);

                if (isCelsius) {
                    temperatureText.setText(String.format("%s°", Math.round(forecast.current().temperature())));
                    windSpeedText.setText(String.format("%s km/h", forecast.current().windSpeed()));
                    visibilityText.setText(String.format("%s km", forecast.hourly().get(0).visibility() / 1000));
                    apparentTempText.setText(String.format("%s°", Math.round(forecast.current().apparentTemperature())));
                } else {
                    temperatureText.setText(String.format("%s° F", Math.round(stupidAmericanValues.current().temperature())));
                    windSpeedText.setText(String.format("%s km/h", stupidAmericanValues.current().windSpeed()));
                    visibilityText.setText(String.format("%s km", stupidAmericanValues.hourly().get(0).visibility() / 1000));
                    apparentTempText.setText(String.format("%s° F", Math.round(stupidAmericanValues.current().apparentTemperature())));
                }
                cloudCoverText.setText(String.format("%s %%", forecast.current().cloudCover()));

                cityNameText.setText(String.format("%s", results.name()));
                countryNameText.setText(results.country());
            });
        });
    }
}
