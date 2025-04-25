package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardItemAdapter adapter;
    private List<CardItem> cardItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize your card list
        // Sample data
        cardItemList = new ArrayList<>();

        cardItemList.add(new CardItem(1, "Victoria", "22°", R.drawable.language_24px, true));
        cardItemList.add(new CardItem(2, "Toronto", "20°", R.drawable.language_24px, true));

        adapter = new CardItemAdapter(cardItemList);
        recyclerView.setAdapter(adapter);

        // Enable drag & drop
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter, cardItemList);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }
}
