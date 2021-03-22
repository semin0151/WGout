package com.example.wgout;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.naver.maps.map.overlay.Overlay;

public class CalendarScheduleAddActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnMapClickListener {
    private MapView mv_calendar_schedule_add;
    private EditText et_calendar_schedule_add;
    private TextView tv_calendar_schedule_add_date;
    private Marker marker = new Marker();

    private static NaverMap naverMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule_add);

        Intent intent = getIntent();

        mv_calendar_schedule_add = (MapView)findViewById(R.id.mv_calendar_schedule_add);
        et_calendar_schedule_add = (EditText)findViewById(R.id.et_calendar_schedule_add);
        tv_calendar_schedule_add_date = (TextView)findViewById(R.id.tv_calendar_schedule_add_date);
        mv_calendar_schedule_add.onCreate(savedInstanceState);
        mv_calendar_schedule_add.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        CameraPosition cameraPosition = new CameraPosition(new LatLng(37.3399, 126.733),16);

        naverMap.setCameraPosition(cameraPosition);

        naverMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        marker.setMap(null);
        marker.setPosition(latLng);
        marker.setMap(naverMap);
    }
}
