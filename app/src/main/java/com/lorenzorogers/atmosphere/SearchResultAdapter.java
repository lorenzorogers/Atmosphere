package com.lorenzorogers.atmosphere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<SearchResultItem> itemList;
    Consumer<CardItem> addCallback;
    public SearchResultAdapter(List<SearchResultItem> itemList, Consumer<CardItem> callback) {
        this.itemList = itemList;
        this.addCallback = callback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView subtitleText;

        CardView container;
        TextView coordinatesText; // Added for displaying geo info

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewSearchResultTitle);
            subtitleText = itemView.findViewById(R.id.textViewSearchResultSubtitle);
            container = itemView.findViewById(R.id.searchResultItemContainer);
            //coordinatesText = itemView.findViewById(R.id.textViewSearchResultCoordinates); // New TextView for coordinates
        }
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        SearchResultItem item = itemList.get(position);
        holder.titleText.setText(item.getTitle());
        holder.subtitleText.setText(item.getSubtitle());
        holder.container.setOnClickListener(listener -> {
            addCallback.accept(new CardItem(
                    item.getTitle(),
                    item.getSubtitle(),
                    item.getLatitude(),
                    item.getLongitude(),
                    R.drawable.language_24px,
                    true
            ));
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateData(List<SearchResultItem> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return itemList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return itemList.get(oldItemPosition).getTitle()
                        .equals(newList.get(newItemPosition).getTitle());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return itemList.get(oldItemPosition).getSubtitle()
                        .equals(newList.get(newItemPosition).getSubtitle());
            }
        });

        itemList = newList;
        diffResult.dispatchUpdatesTo(this);
    }
}
