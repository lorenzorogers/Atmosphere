package com.lorenzorogers.atmosphere;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<SearchResultItem> itemList;

    public SearchResultAdapter(List<SearchResultItem> itemList) {
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView subtitleText;

        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textViewSearchResultTitle);
            subtitleText = itemView.findViewById(R.id.textViewSearchResultSubtitle);
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
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateData(List<SearchResultItem> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }
}
