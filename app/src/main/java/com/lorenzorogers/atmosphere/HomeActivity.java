package com.lorenzorogers.atmosphere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.view.inputmethod.EditorInfo;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lorenzorogers.atmosphere.network.Geocoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "card_prefs";
    private FusedLocationProviderClient fusedLocationClient;
    private static final String CARD_LIST_KEY = "card_list";
    private static final int MAX_RESULTS = 5;

    public static void saveCardList(List<CardItem> list, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(CARD_LIST_KEY, json);
        editor.apply();
    }

    public List<CardItem> loadCardList() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(CARD_LIST_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CardItem>>() {
            }.getType();
            return gson.fromJson(json, type);
        }

        return new ArrayList<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted. Tap again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Link.makeTextViewLink(this, "copyrightLink");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        List<CardItem> cardList = loadCardList();
        if (cardList.isEmpty()) {
            cardList.add(new CardItem(
                    "Victoria",
                    "British Columbia, Canada",
                    48.426037,
                    -123.363684,
                    R.drawable.language_24px,
                    true
            ));
        }

        saveCardList(cardList, this);

        TextView unitToggleText = findViewById(R.id.unitToggleText);
        CardView settingsCard = findViewById(R.id.unitCard);

        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        final boolean[] isCelsius = {prefs.getBoolean("isCelsius", true)};

        unitToggleText.setText(isCelsius[0] ? "°C/km" : "°F/miles");

        settingsCard.setOnClickListener(v -> {
            isCelsius[0] = !isCelsius[0];
            unitToggleText.setText(isCelsius[0] ? "°C/km" : "°F/miles");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isCelsius", isCelsius[0]);
            editor.apply(); // Commit the new value — no need to immediately re-read it
        });

        recyclerView.setTranslationZ(100f);

        CardItemAdapter adapter = new CardItemAdapter(this, cardList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter, this, this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        CardView addCard = findViewById(R.id.addCard);
        addCard.setOnClickListener(v -> showSearchPopup(cardList));

        // "Here" card functionality (shows current location data)
        CardView hereCard = findViewById(R.id.hereCard);
        hereCard.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                return;
            }

            Log.d("LocationCheck", "Requesting location...");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Log.d("LocationCheck", "Got location: " + location.getLatitude() + ", " + location.getLongitude());
                            // Launch MainActivity
                        } else {
                            Log.w("LocationCheck", "Location is null");
                            Toast.makeText(this, "Unable to get location.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("LocationCheck", "Failed to get location: " + e.getMessage());
                        Toast.makeText(this, "Error getting location.", Toast.LENGTH_SHORT).show();
                    });

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "Unable to get location.", Toast.LENGTH_SHORT).show();
                }
            });
        });


        // "Home" card functionality (launches MainActivity with saved "home" location)
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        CardView homeCard = findViewById(R.id.homeCard);

        homeCard.setOnLongClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                return true;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("home_lat", String.valueOf(lat));
                    editor.putString("home_lon", String.valueOf(lon));
                    editor.apply();

                    Toast.makeText(this, "Home location saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unable to get location.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
            return true;
        });

        //Temporary
        /*ImageView icon = findViewById(R.id.icon);
        icon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("query", "Calgary");
            intent.putExtras(bundle);
            startActivity(intent);
        }); */


        homeCard.setOnClickListener(v -> {
            String homeLocation = prefs.getString("home", null);
            if (homeLocation != null) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("home_location", homeLocation);
                startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this, "Home location not set.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSearchPopup(List<CardItem> cardList) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_search);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        dialog.show();

        EditText searchEditText = dialog.findViewById(R.id.searchEditText);
        ImageView searchIcon = dialog.findViewById(R.id.searchIcon);
        TextView noResultsText = dialog.findViewById(R.id.noResultsText);
        RecyclerView searchResultsRecyclerView = dialog.findViewById(R.id.recyclerViewSearchResults);

        List<SearchResultItem> searchResults = new ArrayList<>();
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(searchResults, c -> {
            cardList.add(c);
            saveCardList(cardList, this);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setMinimumHeight(recyclerView.computeVerticalScrollRange());
            dialog.hide();
        });
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        searchResultsRecyclerView.setAdapter(searchResultAdapter);

        Runnable mockSearch = () -> {
            String query = searchEditText.getText().toString().trim();
            if (query.isEmpty()) {
                noResultsText.setVisibility(View.VISIBLE);
                searchResultsRecyclerView.setVisibility(View.GONE);
            } else {
                noResultsText.setVisibility(View.GONE);
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
            }
        };

        searchIcon.setOnClickListener(v -> mockSearch.run());

        searchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)) {
                mockSearch.run();
                return true;
            }
            return false;
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString().trim();
                if (query.isEmpty()) {
                    runOnUiThread(() -> {
                        searchResultAdapter.updateData(new ArrayList<>());
                        noResultsText.setVisibility(View.VISIBLE);
                        searchResultsRecyclerView.setVisibility(View.GONE);
                    });
                    return;
                }

                Geocoder.get(query, geocodingResults -> {
                    List<Geocoder.GeocodingEntry> entries = geocodingResults.results();
                    List<SearchResultItem> results = new ArrayList<>();

                    for (int i = 0; i < Math.min(entries.size(), MAX_RESULTS); i++) {
                        Geocoder.GeocodingEntry entry = entries.get(i);
                        String title = entry.name();
                        String subtitle = entry.admin1() != null ? String.format("%s, %s", entry.admin1(), entry.country()) : entry.country();
                        double lat = entry.latitude();
                        double lon = entry.longitude();
                        results.add(new SearchResultItem(title, subtitle, lat, lon));
                    }

                    runOnUiThread(() -> {
                        if (results.isEmpty()) {
                            noResultsText.setVisibility(View.VISIBLE);
                            searchResultsRecyclerView.setVisibility(View.GONE);
                        } else {
                            noResultsText.setVisibility(View.GONE);
                            searchResultsRecyclerView.setVisibility(View.VISIBLE);
                            searchResultAdapter.updateData(results);
                        }
                    });
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void showSearchPopupForHome() {
        View dialogView = getLayoutInflater().inflate(R.layout.popup_search, null);
        EditText cityInput = dialogView.findViewById(R.id.searchEditText);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Set Home Location");
        builder.setView(dialogView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String city = cityInput.getText().toString();
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            prefs.edit().putString("home", city).apply();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
