package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lorenzorogers.atmosphere.network.Geocoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "card_prefs";
    private static final String CARD_LIST_KEY = "card_list";
    private static final int MAX_RESULTS = 5;  // Limit the number of search results

    public void saveCardList(List<CardItem> list) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(CARD_LIST_KEY, json);
        editor.apply();
    }

    private List<CardItem> loadCardList() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(CARD_LIST_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CardItem>>() {}.getType();
            return gson.fromJson(json, type);
        }

        return new ArrayList<>(); // fallback to an empty list
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Load the list of cards
        List<CardItem> cardList = loadCardList();
        if (cardList.isEmpty()) {
            // Default data if nothing is loaded
            cardList.add(new CardItem("Victoria", "22°", R.drawable.language_24px, true));
            cardList.add(new CardItem("Toronto", "20°", R.drawable.language_24px, true));
        }

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CardItemAdapter adapter = new CardItemAdapter(cardList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        // Enable drag-and-drop functionality
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter, this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        // Handle the search box popup
        CardView addCard = findViewById(R.id.addCard);
        addCard.setOnClickListener(v -> showSearchPopup());
    }

    private void showSearchPopup() {
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
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(searchResults);
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
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

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

                // Simulate the search query using Geocoder API
                Geocoder.get(query, geocodingResults -> {
                    List<Geocoder.GeocodingEntry> entries = geocodingResults.results();
                    List<SearchResultItem> results = new ArrayList<>();

                    for (int i = 0; i < Math.min(entries.size(), MAX_RESULTS); i++) {  // Limit results to MAX_RESULTS
                        Geocoder.GeocodingEntry entry = entries.get(i);
                        String title = entry.name();
                        String subtitle = entry.admin1() + ", " + entry.country();
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
            public void afterTextChanged(Editable editable) {}
        });
    }
}