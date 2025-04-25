package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CardItemAdapter adapter;
    private ArrayList<CardItem> cardItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // Your layout with the RecyclerView

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        List<CardItem> cardItems = new ArrayList<>();
        cardItems.add(new CardItem("Here", "Use location services", R.drawable.location_on_24px, false));
        cardItems.add(new CardItem("Home", "Set home location in settings", R.drawable.home_pin_24px, false));
        cardItems.add(new CardItem("Add location", "", R.drawable.add_24px, false));
        cardItems.add(new CardItem("Settings", "", R.drawable.settings_24px, false));

        CardItemAdapter adapter = new CardItemAdapter(cardItems);
        recyclerView.setAdapter(adapter);
// Attach drag-and-drop handler
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        // Drag & drop logic with immovable card support
        ItemTouchHelper.SimpleCallback c = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                0) {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                int fromPos = viewHolder.getAdapterPosition();
                int toPos = target.getAdapterPosition();

                if (!cardItemList.get(fromPos).isMovable() || !cardItemList.get(toPos).isMovable()) {
                    return false;
                }

                boolean beforeFixed = toPos > 0 && !cardItemList.get(toPos - 1).isMovable();
                boolean afterFixed = toPos < cardItemList.size() - 1 && !cardItemList.get(toPos + 1).isMovable();
                if (beforeFixed && afterFixed) {
                    return false;
                }
                Collections.swap(cardItemList, fromPos, toPos);
                adapter.notifyItemMoved(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Swipe disabled
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int pos = viewHolder.getAdapterPosition();
                if (!cardItemList.get(pos).isMovable()) {
                    return 0;  // No movement allowed
                }
                return makeMovementFlags(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        0
                );
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }
}
