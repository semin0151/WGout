package com.example.wgout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.io.File;

public class CalendarScheduleShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mv_calendar_schedule_show;
    private Button btn_calendar_schedule_show_delete;
    private TextView tv_calendar_schedule_show_content;

    private String content;

    private SQLiteDatabase sqliteDB;

    private Marker marker = new Marker();

    private double lat, lng;
    private NaverMap naverMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule_show);
//날짜 표시
        Intent intent = getIntent();

        content = intent.getStringExtra("content");
        lat = intent.getDoubleExtra("lat",1);
        lng = intent.getDoubleExtra("lng",1);

        init_view();
        sqliteDB = init_DB();
        init_tables();

        tv_calendar_schedule_show_content.setText(content);
        mv_calendar_schedule_show.onCreate(savedInstanceState);
        mv_calendar_schedule_show.getMapAsync(this);

        delete_values();
    }

    private void init_view(){
        mv_calendar_schedule_show = (MapView)findViewById(R.id.mv_calendar_schedule_show);
        btn_calendar_schedule_show_delete = (Button)findViewById(R.id.btn_calendar_schedule_show_delete);
        tv_calendar_schedule_show_content = (TextView)findViewById(R.id.tv_calendar_schedule_show_content);
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

    private void delete_values(){
        btn_calendar_schedule_show_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DB 데이터 삭제 해야함
                sqliteDB.execSQL("DELETE FROM SCHEDULE WHERE CONTENT = '" + content + "'");
                setResult(100);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        CameraPosition cameraPosition = new CameraPosition(new LatLng(lat, lng),16);
        naverMap.setCameraPosition(cameraPosition);

        marker.setPosition(new LatLng(lat, lng));
        marker.setMap(naverMap);
    }
}
