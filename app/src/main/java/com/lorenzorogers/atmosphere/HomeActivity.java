package com.lorenzorogers.atmosphere;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardItemAdapter adapter;
    private ArrayList<CardItem> cardItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // Your layout with the RecyclerView

        recyclerView = findViewById(R.id.recyclerView);
        cardItemList = new ArrayList<>();

        // Add sample cards (replace with your actual logic)
        cardItemList.add(new CardItem("Card 1", "This is the first card", R.drawable.ic_sample_icon, true));
        cardItemList.add(new CardItem("Card 2", "Fixed card", R.drawable.ic_sample_icon, false));
        cardItemList.add(new CardItem("Card 3", "Another moveable one", R.drawable.ic_sample_icon, true));

        adapter = new CardItemAdapter(cardItemList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));  // Grid with 2 columns
        recyclerView.setAdapter(adapter);

        // Drag & drop logic with immovable card support
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
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

                // Don't allow moving non-moveable items or into positions between two fixed cards
                if (!cardItemList.get(fromPos).isMoveable() || !cardItemList.get(toPos).isMoveable()) {
                    return false;
                }

                // Don't allow placing moveable cards between two immovable cards
                boolean beforeFixed = toPos > 0 && !cardItemList.get(toPos - 1).isMoveable();
                boolean afterFixed = toPos < cardItemList.size() - 1 && !cardItemList.get(toPos + 1).isMoveable();
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
                if (!cardItemList.get(pos).isMoveable()) {
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
