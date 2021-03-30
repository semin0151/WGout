package com.example.wgout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DestinationRecyclerAdapter extends RecyclerView.Adapter<DestinationRecyclerAdapter.ViewHolder> {
    private ArrayList<DestinationRecyclerItem> lists = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_destination_location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_destination_location = (TextView)itemView.findViewById(R.id.tv_destination_location);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_destination,parent,false);
        DestinationRecyclerAdapter.ViewHolder vh = new DestinationRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DestinationRecyclerItem item = lists.get(position);

        holder.tv_destination_location.setText(item.getLocation());

        selItem(holder, item);

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String location, double latitude, double longitude){
        DestinationRecyclerItem item = new DestinationRecyclerItem();

        item.setLocation(location);
        item.setLatitude(latitude);
        item.setLongitude(longitude);

        lists.add(item);
    }

    private void selItem(ViewHolder holder, DestinationRecyclerItem item){
        holder.tv_destination_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DestinationShowActivity.class);
                intent.putExtra("location", item.getLocation());
                intent.putExtra("lat", item.getLatitude());
                intent.putExtra("lng", item.getLongitude());
                ((Activity)v.getContext()).startActivityForResult(intent, 0);
            }
        });
    }
}
