package com.example.wgout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherVilageFcstAdapter extends RecyclerView.Adapter<WeatherVilageFcstAdapter.ViewHolder> {
    private ArrayList<WeatherVilageFcstItem> lists = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        //private TextView tv_vilage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //tv_vilage = (TextView)itemView.findViewById(R.id.tv_vilage);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View view = inflater.inflate(R.layout.recycler_vilage_fcst, parent, false);
        //ViewHolder viewHolder = new ViewHolder(view);
        //return viewHolder;
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherVilageFcstItem item = lists.get(position);
        String str = item.getDate() + "/" + item.getTime() + "\n" +
                "강수확률 : " + item.getPOP() + "\n" +
                "강수형태 : " + item.getPTY() + "\n" +
                "하늘 상태 : " + item.getSKY() + "\n" +
                "기온 : " + item.getT3H() + "\n" +
                "습도 : " + item.getREH() + "\n";
        //holder.tv_vilage.setText(str);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String date, String time, String POP, String PTY, String SKY, String T3H, String REH){
        WeatherVilageFcstItem item = new WeatherVilageFcstItem();

        item.setDate(date);
        item.setTime(time);
        item.setPOP(POP);
        item.setPTY(PTY);
        item.setSKY(SKY);
        item.setT3H(T3H);
        item.setREH(REH);

        lists.add(item);
    }
}
