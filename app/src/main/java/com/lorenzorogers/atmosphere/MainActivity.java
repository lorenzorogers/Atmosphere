package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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

        Button testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(
                view -> {
                    LocationForecast.get(52.52, 13.41, forecast -> {
                        Log.println(Log.DEBUG, "Atmosphere", String.valueOf(forecast.name()));
                    });
                    /*RequestUtils.fetch(
                            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,rain,wind_speed_10m,visibility,apparent_temperature",
                            e -> Log.println(Log.INFO, "Atmosphere", e)
                    );*/
                }
        );
    }
}