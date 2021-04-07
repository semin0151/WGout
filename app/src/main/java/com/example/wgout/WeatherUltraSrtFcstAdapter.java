package com.example.wgout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherUltraSrtFcstAdapter extends RecyclerView.Adapter<WeatherUltraSrtFcstAdapter.ViewHolder> {
    private ArrayList<WeatherUltraSrtFcstItem> lists = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        //private TextView tv_ultra;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //tv_ultra = (TextView)itemView.findViewById(R.id.tv_ultra);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //View view = inflater.inflate(R.layout.recycler_ultra_srt_fcst, parent, false);
        //ViewHolder viewHolder = new ViewHolder(view);
        //return viewHolder;
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherUltraSrtFcstItem item = lists.get(position);
        String str = item.getDate() + "/" + item.getTime() + "\n" +
                "강수형태 : " + item.getPTY() + "\n" +
                "1시간 강수량 : " + item.getRN1() + "\n" +
                "하늘상태 : " + item.getSKY() + "\n" +
                "기온 : " + item.getT1H() + "\n" +
                "습도 : " + item.getREH() + "\n";
        //holder.tv_ultra.setText(str);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String date, String time, String PTY, String RN1, String SKY, String T1H, String REH){
        WeatherUltraSrtFcstItem item = new WeatherUltraSrtFcstItem();

        item.setDate(date);
        item.setTime(time);
        item.setPTY(PTY);
        item.setREH(REH);
        item.setRN1(RN1);
        item.setSKY(SKY);
        item.setT1H(T1H);

        lists.add(item);
    }

}
