package com.lorenzorogers.atmosphere;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CardItemAdapter adapter;
    private final List<CardItem> cardItemList;

    public MyItemTouchHelperCallback(CardItemAdapter adapter, List<CardItem> cardItemList) {
        this.adapter = adapter;
        this.cardItemList = cardItemList;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        int position = viewHolder.getAdapterPosition();
        CardItem item = adapter.getItem(position);

        if (item.isMovable()) {
            dragFlags = 0; // Prevent movement
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {

        int fromPos = viewHolder.getAdapterPosition();
        int toPos = target.getAdapterPosition();

        // Prevent dragging to or between immovable cards
        if (cardItemList.get(toPos).isMovable()) return false;

        // Swap in data list
        CardItem fromItem = cardItemList.get(fromPos);
        cardItemList.remove(fromPos);
        cardItemList.add(toPos, fromItem);

        // Notify adapter
        adapter.notifyItemMoved(fromPos, toPos);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Not used
    }
}
