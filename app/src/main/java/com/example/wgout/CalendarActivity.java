package com.example.wgout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {
    private Button btn_calendar_past;
    private Button btn_calendar_future;

    private TextView tv_calendar_date;
    private RecyclerView rv_calendar;

    private GregorianCalendar today = new GregorianCalendar();
    private int year = today.get(Calendar.YEAR), month = today.get(Calendar.MONTH);
    private static final int CALENDAR_EMPTY = 0, CALENDAR_DAY = 1;


    private CalendarRecyclerAdapter adapter_calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btn_calendar_past = (Button)findViewById(R.id.btn_calendar_past);
        btn_calendar_future = (Button)findViewById(R.id.btn_calendar_future);

        tv_calendar_date = (TextView)findViewById(R.id.tv_calendar_date);
        rv_calendar = (RecyclerView)findViewById(R.id.rv_calendar);

        btn_calendar_past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month--;
                rv_calendar.setAdapter(adapter_calendar);
                setCalender(year,month);
            }
        });

        btn_calendar_future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month++;
                rv_calendar.setAdapter(adapter_calendar);
                setCalender(year,month);
            }
        });
        setCalender(year,month);
        Intent intent = getIntent();

    }

    private void setCalender(int year, int month){

        adapter_calendar = new CalendarRecyclerAdapter(year, month);
        rv_calendar.setLayoutManager(new StaggeredGridLayoutManager(7,StaggeredGridLayoutManager.VERTICAL));
        rv_calendar.setAdapter(adapter_calendar);
        GregorianCalendar calendar = new GregorianCalendar(year, month, 1);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) -1 ;
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        tv_calendar_date.setText(Integer.toString(calendar.get(Calendar.YEAR)) + "/" + Integer.toString(calendar.get(Calendar.MONTH)+1));
        for(int i = 0; i < dayOfWeek; i++){
            adapter_calendar.addItem(CALENDAR_EMPTY,0);
        }
        for(int i = 1; i <= max; i++){
            adapter_calendar.addItem(CALENDAR_DAY,i);
        }
    }
}
