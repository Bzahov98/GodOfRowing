package com.bzahov.elsys.godofrowing.RecyclerView;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bzahov.elsys.godofrowing.Models.ResourcesFromActivity;
import com.bzahov.elsys.godofrowing.R;
//import com.firebase.client.Query;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class HistoryAdapter extends FirebaseRecyclerAdapter<HistoryAdapter.ViewHolder, ResourcesFromActivity> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewTotMeters;

        public ViewHolder(View view) {
            super(view);
            textViewName = (TextView) view.findViewById(R.id.start_date_head_text_header);
            textViewTotMeters = (TextView) view.findViewById(R.id.list_item_child_layout).findViewById(R.id.list_item_child_meters_total);
            //TODO:
        }
    }

    public HistoryAdapter(Query query, @Nullable ArrayList<ResourcesFromActivity> items,
                          @Nullable ArrayList<String> keys) {
        super(query, items, keys);
    }

    @Override public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_history_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        ResourcesFromActivity item = getItem(position);
        holder.textViewName.setText(item.getCurrentTime());
        holder.textViewTotMeters.setText(String.valueOf(item.getTotalMeters()));
    }

    @Override protected void itemAdded(ResourcesFromActivity item, String key, int position) {
        Log.d("MyAdapter", "Added a new item to the adapter.");
    }

    @Override protected void itemChanged(ResourcesFromActivity oldItem, ResourcesFromActivity newItem, String key, int position) {
        Log.d("MyAdapter", "Changed an item.");
    }

    @Override protected void itemRemoved(ResourcesFromActivity item, String key, int position) {
        Log.d("MyAdapter", "Removed an item from the adapter.");
    }

    @Override protected void itemMoved(ResourcesFromActivity item, String key, int oldPosition, int newPosition) {
        Log.d("MyAdapter", "Moved an item.");
    }
}
