package com.lorenzorogers.atmosphere;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CardItemAdapter adapter;
    private final HomeActivity activity;

    public MyItemTouchHelperCallback(CardItemAdapter adapter, HomeActivity activity) {
        this.adapter = adapter;
        this.activity = activity;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        if (position < 0 || position >= adapter.getItemCount()) {
            return makeMovementFlags(0, 0); // No movement if position invalid
        }

        CardItem item = adapter.getItem(position);
        int dragFlags = (item != null && item.isMovable())
                ? ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                : 0;

        return makeMovementFlags(dragFlags, 0); // Swipe not used
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        int fromPos = viewHolder.getAdapterPosition();
        int toPos = target.getAdapterPosition();

        if (fromPos < 0 || toPos < 0 || fromPos >= adapter.getItemCount() || toPos >= adapter.getItemCount()) {
            return false; // Don't crash on bad positions
        }

        Collections.swap(adapter.getCardItems(), fromPos, toPos);
        adapter.notifyItemMoved(fromPos, toPos);

        // Save the new list order
        activity.saveCardList(adapter.getCardItems());

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Swipe disabled
    }
}
