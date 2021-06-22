package com.example.wgout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private ArrayList<com.example.wgout.WeatherItem> lists = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_weather;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_weather = (TextView)itemView.findViewById(R.id.tv_weather);
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_weather, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        com.example.wgout.WeatherItem item = lists.get(position);

        String str = item.getDate() + "/" + item.getTime() + "/" + item.getCode() + "\n" +
                "강수확률 : " + item.getPOP() + "\n" +
                "강수형태 : " + item.getPTY() + "\n" +
                "1시간 강수량 : " + item.getRN1() + "\n" +
                "하늘상태 : "  + item.getSKY() + "\n" +
                "기온 : " + item.getT1H() + "\n" +
                "습도 : " + item.getREH() + "\n";

        holder.tv_weather.setText(str);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void addItem(String date, String time, int code, String POP, String PTY, String RN1, String SKY, String T1H, String REH){
        com.example.wgout.WeatherItem item = new com.example.wgout.WeatherItem();

        item.setDate(date);
        item.setTime(time);
        item.setCode(code);

        item.setPOP(POP);
        item.setPTY(PTY);
        item.setRN1(RN1);
        item.setSKY(SKY);
        item.setT1H(T1H);
        item.setREH(REH);

        lists.add(item);
    }
}
