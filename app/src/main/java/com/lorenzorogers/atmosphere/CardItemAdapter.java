package com.lorenzorogers.atmosphere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.ViewHolder> {

    private final Context context;

    private final List<CardItem> cardItems;

    public CardItemAdapter(Context context, List<CardItem> cardItems) {
        this.context = context;
        this.cardItems = cardItems;
    }

    public List<CardItem> getCardItems() {
        return cardItems;
    }

    public CardItem getItem(int position) {
        return cardItems.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardItem item = cardItems.get(position);
        holder.imageView.setImageResource(item.resIcon());  // small icon
        holder.titleView.setText(item.title());
        holder.subtitleView.setText(item.subtitle());
        holder.cardItemContainer.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            Bundle bundle = new Bundle();
            System.out.println(item.latitude() + " " + item.longitude());
            bundle.putDouble("latitude", item.latitude());
            bundle.putDouble("longitude", item.longitude());
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        });


        holder.moreOptions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.moreOptions);
            popup.inflate(R.menu.card_item_menu);
            popup.setOnMenuItemClickListener(more_item -> {
                if (more_item.getItemId() == R.id.menu_remove) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        cardItems.remove(pos);
                        HomeActivity.saveCardList(cardItems, context);
                        notifyItemRemoved(pos);
                    }
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView subtitleView;

        CardView cardItemContainer;

        ImageView moreOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardIcon);
            titleView = itemView.findViewById(R.id.cardTitle);
            subtitleView = itemView.findViewById(R.id.cardSubtitle);
            cardItemContainer = itemView.findViewById(R.id.cardItemContainer);
            moreOptions = itemView.findViewById(R.id.moreOptions);
        }
    }
}