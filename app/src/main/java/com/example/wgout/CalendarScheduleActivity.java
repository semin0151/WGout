package com.example.wgout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class CalendarScheduleActivity extends AppCompatActivity {
    private Button btn_calenadar_schedule_add;
    private TextView tv_calendar_schedule_date;
    private TextView tv_calendar_schedule_no;

    private RecyclerView rv_calendar_schedule;

    private CalendarScheduleRecyclerAdapter adapter_calendar_schedule;

    private SQLiteDatabase sqliteDB;

    private int year, month, day;
    private String str;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule);

        init_view();
        init_intent();
        init_rv();

        sqliteDB = init_DB();
        init_tables();

        btn_clicked();



    }

    private void init_view(){
        btn_calenadar_schedule_add = (Button)findViewById(R.id.btn_calendar_schedule_add);
        tv_calendar_schedule_date = (TextView)findViewById(R.id.tv_calendar_schedule_date);
        tv_calendar_schedule_no = (TextView)findViewById(R.id.tv_calendar_schedule_no);
        rv_calendar_schedule = (RecyclerView)findViewById(R.id.rv_calendar_schedule);
    }

    private void init_intent(){
        Intent intent = getIntent();
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);

        str = Integer.toString(year) + "/" + Integer.toString(month+1) + "/" + Integer.toString(day);
        tv_calendar_schedule_date.setText(str);

    }

    private void btn_clicked(){
        btn_calenadar_schedule_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarScheduleActivity.this, CalendarScheduleAddActivity.class);
                intent.putExtra("year",year);
                intent.putExtra("month", month);
                intent.putExtra("day",day);
                startActivity(intent);
            }
        });
    }

    private SQLiteDatabase init_DB(){
        SQLiteDatabase db = null;

        File file = new File(getFilesDir(), "wgout.db");
        try{
            db = SQLiteDatabase.openOrCreateDatabase(file,null);
        }catch (Exception e){
            e.printStackTrace();
        }

        return db;
    }

    private void init_tables(){
        if(sqliteDB != null) {
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS SCHEDULE (" +
                    "DATE " + "TEXT," +
                    "CONTENT " + "TEXT," +
                    "LAT " + "DOUBLE," +
                    "LNG " + "DOUBLE" +
                    ")";
            sqliteDB.execSQL(sqlCreateTbl);
        }
    }

    private void init_rv(){
        rv_calendar_schedule.setLayoutManager(new LinearLayoutManager(this));

        adapter_calendar_schedule = new CalendarScheduleRecyclerAdapter();

        rv_calendar_schedule.setAdapter(adapter_calendar_schedule);

        if (adapter_calendar_schedule.getItemCount()==0) {
            rv_calendar_schedule.setVisibility(View.GONE);
            tv_calendar_schedule_no.setVisibility(View.VISIBLE);
        }
    }

    private void save_values(){

    }

    private void load_values(){

    }
}
