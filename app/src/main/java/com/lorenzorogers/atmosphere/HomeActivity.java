package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.text.Editable;

import android.text.TextWatcher;
import android.widget.EditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lorenzorogers.atmosphere.network.Geocoder;

public class HomeActivity extends AppCompatActivity {
    private static final int MAX_RESULTS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CardView addCard = findViewById(R.id.addCard);
        addCard.setOnClickListener(v -> showSearchPopup());

        //RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<CardItem> cardItemList = new ArrayList<>();
        cardItemList.add(new CardItem(1, "Victoria", "22°", R.drawable.language_24px, true));
        cardItemList.add(new CardItem(2, "Toronto", "20°", R.drawable.language_24px, true));

        CardItemAdapter adapter = new CardItemAdapter(cardItemList);
        //recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter, cardItemList);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //touchHelper.attachToRecyclerView(recyclerView);
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

        // Initial dummy results (if needed for preview or placeholder)
        List<SearchResultItem> defaultSearchResults = new ArrayList<>();

        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(defaultSearchResults);
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

        // Replace this with live geocoding
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

                Geocoder.get(query, geocodingResults -> {
                    List<Geocoder.GeocodingEntry> entries = geocodingResults.results();
                    List<SearchResultItem> searchResults = new ArrayList<>();

                    for (int i = 0; i < Math.min(entries.size(), MAX_RESULTS); i++) {
                        Geocoder.GeocodingEntry entry = entries.get(i);
                        String title = entry.name();
                        String subtitle = entry.admin1() + ", " + entry.country();
                        double lat = entry.latitude();
                        double lon = entry.longitude();
                        searchResults.add(new SearchResultItem(title, subtitle, lat, lon));
                    }

                    runOnUiThread(() -> {
                        if (searchResults.isEmpty()) {
                            noResultsText.setVisibility(View.VISIBLE);
                            searchResultsRecyclerView.setVisibility(View.GONE);
                        } else {
                            noResultsText.setVisibility(View.GONE);
                            searchResultsRecyclerView.setVisibility(View.VISIBLE);
                            searchResultAdapter.updateData(searchResults);
                        }
                    });
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}