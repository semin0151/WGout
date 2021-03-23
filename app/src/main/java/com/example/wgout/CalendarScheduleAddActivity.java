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

import com.example.wgout.Data.Address;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarScheduleAddActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnMapClickListener {
    private MapView mv_calendar_schedule_add;
    private EditText et_calendar_schedule_add;
    private TextView tv_calendar_schedule_add_date;
    private TextView tv_calendar_schedule_add_address;

    private ReverseGeocoderClient reverseGeocoderClient;
    private ReverseGeocoderInterface reverseGeocoderInterface;

    private String key_id = "q618nmd8vn";
    private String key = "DjrtsY4erRXEe41gTfwLZj0dQmldbk7ZhzI4hEVb";
    private String mAddress;

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
        tv_calendar_schedule_add_address = (TextView)findViewById(R.id.tv_calendar_schedule_add_address);
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
        //tv_calendar_schedule_add_address.setText( Double.toString(latLng.latitude) + "/" + Double.toString(latLng.longitude));
        CallRetrofit(latLng);
    }

    public void CallRetrofit(LatLng latlng){
        try {
            reverseGeocoderClient = ReverseGeocoderClient.getInstance();
            reverseGeocoderInterface = ReverseGeocoderClient.getReverseGeocoderInterface();

            reverseGeocoderInterface.getAddress(Double.toString(latlng.longitude) + "," + Double.toString(latlng.latitude),
                    "json",
                    "addr,roadaddr",
                    key_id,
                    key).enqueue(new Callback<Address>() {
                @Override
                public void onResponse(Call<Address> call, Response<Address> response) {
                    //need to set out of country
                    Address address = response.body();
                    mAddress = address.getResults().get(0).getRegion().getArea1().getName() + " " +
                                address.getResults().get(0).getRegion().getArea2().getName() + " " +
                                address.getResults().get(0).getRegion().getArea3().getName();
                    tv_calendar_schedule_add_address.setText(mAddress);

                }

                @Override
                public void onFailure(Call<Address> call, Throwable t) {

                }
            });
        }catch (Exception e){
            tv_calendar_schedule_add_address.setText(e.getMessage());
        }
    }
}
