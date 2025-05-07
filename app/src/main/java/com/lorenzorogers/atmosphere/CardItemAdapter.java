package com.lorenzorogers.atmosphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.ViewHolder> {

    private final List<CardItem> cardItems;

    public CardItemAdapter(List<CardItem> cardItems) {
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardIcon);
            titleView = itemView.findViewById(R.id.cardTitle);
            subtitleView = itemView.findViewById(R.id.cardSubtitle);
            cardItemContainer = itemView.findViewById(R.id.cardItemContainer);
        }
    }
}