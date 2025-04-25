package com.lorenzorogers.atmosphere;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CardItemAdapter adapter;

    public MyItemTouchHelperCallback(CardItemAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        CardItem item = adapter.getCardItems().get(position);

        if (item.isMovable()) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, 0);
        } else {
            return 0;
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder source,
                          @NonNull RecyclerView.ViewHolder target) {

        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        CardItem fromItem = adapter.getCardItems().get(fromPosition);
        CardItem toItem = adapter.getCardItems().get(toPosition);

        // Prevent moving if either item is not movable
        if (fromItem.isMovable() && toItem.isMovable()) {
            return false;
        }

        // Prevent placing between two non-movable cards
        if ((toPosition > 0 && !adapter.getCardItems().get(toPosition - 1).isMovable()) &&
                (toPosition < adapter.getItemCount() - 1 && !adapter.getCardItems().get(toPosition + 1).isMovable())) {
            return false;
        }

        adapter.moveItem(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // No swiping supported
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true; // You can also toggle this if you want to enable/disable drag on long press
    }
}
