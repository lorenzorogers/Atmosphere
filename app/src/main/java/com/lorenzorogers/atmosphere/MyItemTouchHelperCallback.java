package com.lorenzorogers.atmosphere;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CardItemAdapter adapter;
    private final HomeActivity activity;
    private final Context context;

    public MyItemTouchHelperCallback(CardItemAdapter adapter, HomeActivity activity, Context context) {
        this.adapter = adapter;
        this.activity = activity;
        this.context = context;
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

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        viewHolder.itemView.setElevation(200f);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        if (fromPos < 0 || toPos < 0 || fromPos >= adapter.getItemCount() || toPos >= adapter.getItemCount()) {
            return false; // Don't crash on bad positions
        }

        Collections.swap(adapter.getCardItems(), fromPos, toPos);
        adapter.notifyItemMoved(fromPos, toPos);

        // Save the new list order
        HomeActivity.saveCardList(adapter.getCardItems(), context);

        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
            // Log to verify drag start
            //Log.d("MyItemTouchHelper", "Drag started, providing haptic feedback");

            // Check if the item can provide haptic feedback
            if (viewHolder.itemView.isHapticFeedbackEnabled()) {
                viewHolder.itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            } else {
                Log.w("MyItemTouchHelper", "Haptic feedback is not enabled");
            }
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        viewHolder.itemView.setElevation(200f);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setElevation(12f);
    }

}
