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

public class HomeActivity extends AppCompatActivity {
    private CardItemAdapter adapter;
    private List<CardItem> cardItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CardView addCard = findViewById(R.id.addCard);
        addCard.setOnClickListener(v -> showSearchPopup());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        cardItemList = new ArrayList<>();
        cardItemList.add(new CardItem(1, "Victoria", "22°", R.drawable.language_24px, true));
        cardItemList.add(new CardItem(2, "Toronto", "20°", R.drawable.language_24px, true));

        adapter = new CardItemAdapter(cardItemList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter, cardItemList);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
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

        // Sample search result items for demonstration
        List<SearchResultItem> defaultSearchResults = new ArrayList<>();
        defaultSearchResults.add(new SearchResultItem("Victoria", "22°"));
        defaultSearchResults.add(new SearchResultItem("Toronto", "20°"));

        // Adapter and RecyclerView setup
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(defaultSearchResults);
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        searchResultsRecyclerView.setAdapter(searchResultAdapter);

        // Update visibility based on search query (mocked logic)
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

        // TextWatcher for real-time search filtering
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString().toLowerCase();
                List<SearchResultItem> filteredResults = new ArrayList<>();

                // Filter based on the title
                for (SearchResultItem item : defaultSearchResults) {
                    if (item.getTitle().toLowerCase().contains(query)) {
                        filteredResults.add(item);
                    }
                }

                // Update the adapter with filtered results
                searchResultAdapter.updateData(filteredResults);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}