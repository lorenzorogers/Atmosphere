package com.lorenzorogers.atmosphere;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.CardItemViewHolder> {

    private List<CardItem> cardItemList;

    public CardItemAdapter(List<CardItem> cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardItemViewHolder holder, int position) {
        CardItem item = cardItemList.get(position);

        holder.cardTitle.setText(item.getTitle());
        holder.cardSubtitle.setText(item.getSubtitle());
        holder.cardIcon.setImageResource(item.getIconResId());

        holder.moreOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.card_item_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menu_remove) {
                    removeItem(holder.getAdapterPosition());
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cardItemList.size()) {
            cardItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < 0 || toPosition < 0 ||
                fromPosition >= cardItemList.size() || toPosition >= cardItemList.size()) return;

        CardItem item = cardItemList.remove(fromPosition);
        cardItemList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<CardItem> getCardItems() {
        return cardItemList;
    }

    public static class CardItemViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle, cardSubtitle;
        ImageView cardIcon, moreOptions;

        public CardItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardSubtitle = itemView.findViewById(R.id.cardSubtitle);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            moreOptions = itemView.findViewById(R.id.moreOptions);
        }
    }
}
