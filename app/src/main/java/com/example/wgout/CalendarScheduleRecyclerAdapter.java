package com.example.wgout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarScheduleRecyclerAdapter extends RecyclerView.Adapter<CalendarScheduleRecyclerAdapter.ViewHolder> {
    private ArrayList<CalendarScheduleRecyclerItem> lists = new ArrayList<>();
    private SQLiteDatabase sqliteDB;

    CalendarScheduleRecyclerAdapter(){

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_calendar_schedule_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_calendar_schedule_content = (TextView)itemView.findViewById(R.id.tv_calendar_schedule_content);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_calendar_schedule,parent,false);
        CalendarScheduleRecyclerAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarScheduleRecyclerItem item = lists.get(position);

        holder.tv_calendar_schedule_content.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String content){
        CalendarScheduleRecyclerItem item = new CalendarScheduleRecyclerItem();

        item.setContent(content);

        lists.add(item);
    }
}
