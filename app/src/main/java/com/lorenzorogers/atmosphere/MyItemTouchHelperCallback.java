package com.lorenzorogers.atmosphere;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CardItemAdapter mAdapter;
    private final List<CardItem> mCardItemList;

    public MyItemTouchHelperCallback(CardItemAdapter adapter, List<CardItem> cardItemList) {
        mAdapter = adapter;
        mCardItemList = cardItemList;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true; // Allows dragging by long press
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; // Disabling swipe for this example
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // Determine whether the item can be moved or not based on isMovable
        CardItem cardItem = mCardItemList.get(viewHolder.getAdapterPosition());

        if (cardItem.isMovable()) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        } else {
            return 0; // If not movable, return 0 to prevent dragging
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        CardItem fromItem = mCardItemList.get(fromPosition);
        CardItem toItem = mCardItemList.get(toPosition);

        // Only allow moving if both items are movable
        if (fromItem.isMovable() && toItem.isMovable()) {
            Collections.swap(mCardItemList, fromPosition, toPosition);
            mAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        return false; // Prevent movement if either item is not movable
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Handle swipe if needed (e.g., for deletion)
    }
}
