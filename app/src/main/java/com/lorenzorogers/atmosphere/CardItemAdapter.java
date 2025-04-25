// CardItemAdapter.java
package com.lorenzorogers.atmosphere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.CardItemViewHolder> {

    private List<CardItem> cardItemList;

    // Constructor to pass data to the adapter
    public CardItemAdapter(List<CardItem> cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the card item layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardItemViewHolder holder, int position) {
        CardItem cardItem = cardItemList.get(position);

        // Set data to the views
        holder.cardTitle.setText(cardItem.getTitle());
        holder.cardSubtitle.setText(cardItem.getSubtitle());
        holder.cardIcon.setImageResource(cardItem.getIconResId());

        // Set the draggable status based on the 'isMovable' flag
        holder.itemView.setOnTouchListener((v, event) -> {
            if (cardItem.isMovable()) {
                return false; // Prevent dragging
            } else {
                // Ensure that the view performs a click action if touched
                v.performClick();
                return true; // Allow dragging
            }
        });
    }



    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    // ViewHolder class to hold references to card views
    public static class CardItemViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardSubtitle;
        ImageView cardIcon;

        public CardItemViewHolder(View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardSubtitle = itemView.findViewById(R.id.cardSubtitle);
            cardIcon = itemView.findViewById(R.id.cardIcon);
        }
    }

    // Method to move an item within the list
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 || fromPosition >= cardItemList.size() || toPosition >= cardItemList.size()) {
            return;
        }

        CardItem movedItem = cardItemList.get(fromPosition);
        cardItemList.remove(fromPosition);
        cardItemList.add(toPosition, movedItem);

        notifyItemMoved(fromPosition, toPosition);
    }
}
