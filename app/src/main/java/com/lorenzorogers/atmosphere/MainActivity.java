package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lorenzorogers.atmosphere.forecast.LocationForecast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LocationForecast.get(48.451816, -123.32826, forecast -> {

            TextView temperatureText = findViewById(R.id.temperatureText);
            TextView windSpeedText = findViewById(R.id.windSpeed);
            TextView visibilityText = findViewById(R.id.visibilityValue);
            TextView apparentTempText = findViewById(R.id.apparentTempValue);

            temperatureText.setText(String.format("%s°", Math.round(forecast.current().temperature())));
            windSpeedText.setText(String.format("%s km/h", forecast.current().windSpeed()));
            visibilityText.setText(String.format("%s km", forecast.hourly().get(0).visibility() / 1000));
            apparentTempText.setText(String.format("%s°", Math.round(forecast.current().apparentTemperature())));
        });
    }
}