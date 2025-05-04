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
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0; // Swipe not used

        int position = viewHolder.getAdapterPosition();
        CardItem item = adapter.getItem(position);

        // Disable movement if item is NOT movable
        if (!item.isMovable()) {
            dragFlags = 0;
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        int fromPos = viewHolder.getAdapterPosition();
        int toPos = target.getAdapterPosition();

        Collections.swap(adapter.getCardItems(), fromPos, toPos);
        adapter.notifyItemMoved(fromPos, toPos);

        // Save the new list order
        activity.saveCardList(adapter.getCardItems());

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Swipe is disabled, so this is unused
    }
}
