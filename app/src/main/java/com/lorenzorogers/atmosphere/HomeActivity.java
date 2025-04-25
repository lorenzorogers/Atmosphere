package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

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

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data for CardItems (you can add more cards)
        cardItemList = new ArrayList<>();
        cardItemList.add(new CardItem("Title 1", "Subtitle 1", R.drawable.ic_launcher_foreground));
        cardItemList.add(new CardItem("Title 2", "Subtitle 2", R.drawable.ic_launcher_foreground));
        cardItemList.add(new CardItem("Title 3", "Subtitle 3", R.drawable.ic_launcher_foreground));

        // Set up the adapter with the RecyclerView
        adapter = new CardItemAdapter(cardItemList);
        recyclerView.setAdapter(adapter);

        // Create the ItemTouchHelper.Callback and attach it to the RecyclerView
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {

            @Override
            public boolean isLongPressDragEnabled() {
                return true; // Enable long-press drag
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false; // Disable swipe to dismiss functionality
            }

            // Implement onMove to handle drag-and-drop logic
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (fromPosition < 0 || toPosition < 0 || fromPosition >= cardItemList.size() || toPosition >= cardItemList.size()) {
                    return false;
                }

                // Move the item in the list
                adapter.onItemMove(fromPosition, toPosition);
                return true;
            }

            // Implement onSwiped to disable swipe-to-dismiss
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // No swipe-to-dismiss behavior
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // Flags for the drag movement (up, down, left, right)
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0); // No swipe flags
            }
        };

        // Attach ItemTouchHelper to RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}