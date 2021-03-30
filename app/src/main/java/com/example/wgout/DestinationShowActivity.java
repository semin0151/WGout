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

import com.example.wgout.Data.Address;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mv_destination_show;
    private Button btn_destination_show_delete;
    private TextView tv_destination_show_location;
    private TextView tv_destination_show_gps;

    private String location;
    private double lat, lng;

    private SQLiteDatabase sqliteDB;

    private NaverMap naverMap;

    private Marker marker = new Marker();

    private String key_id = "q618nmd8vn";
    private String key = "DjrtsY4erRXEe41gTfwLZj0dQmldbk7ZhzI4hEVb";
    private String maddress;

    private ReverseGeocoderClient reverseGeocoderClient;
    private ReverseGeocoderInterface reverseGeocoderInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_show);

        init_view();
        init_intent();

        sqliteDB = init_DB();
        init_tables();
        delete_values();

        tv_destination_show_location.setText(location);
        mv_destination_show.onCreate(savedInstanceState);
        mv_destination_show.getMapAsync(this);

    }

    private void init_view(){
        mv_destination_show = (MapView)findViewById(R.id.mv_destination_show);
        btn_destination_show_delete = (Button)findViewById(R.id.btn_destination_show_delete);
        tv_destination_show_location = (TextView)findViewById(R.id.tv_destination_show_location);
        tv_destination_show_gps = (TextView)findViewById(R.id.tv_destination_show_gps);
    }

    private void init_intent(){
        Intent intent = getIntent();

        location = intent.getStringExtra("location");
        lat = intent.getDoubleExtra("lat",1);
        lng = intent.getDoubleExtra("lng",1);
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
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS DESTINATION (" +
                    "LOCATION " + "TEXT," +
                    "LAT " + "DOUBLE," +
                    "LNG " + "DOUBLE" +
                    ")";
            sqliteDB.execSQL(sqlCreateTbl);
        }
    }

    private void delete_values(){
        btn_destination_show_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqliteDB.execSQL("DELETE FROM DESTINATION WHERE LOCATION = '" + location + "'");
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
        CallRetrofit(new LatLng(lat, lng));
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
                    Address address = response.body();
                    if(address.getStatus().getCode()==0) {
                        maddress = address.getResults().get(0).getRegion().getArea1().getName() + " " +
                                address.getResults().get(0).getRegion().getArea2().getName() + " " +
                                address.getResults().get(0).getRegion().getArea3().getName();

                        if(address.getResults().size() == 1) {
                            maddress += " " + address.getResults().get(0).getLand().getNumber1();
                            if(address.getResults().get(0).getLand().getNumber2().length() != 0)
                                maddress += "-" + address.getResults().get(0).getLand().getNumber2();
                        }else{
                            maddress += " " + address.getResults().get(0).getLand().getNumber1();
                            if(address.getResults().get(0).getLand().getNumber2().length() != 0)
                                maddress += "-" + address.getResults().get(0).getLand().getNumber2();
                            maddress += "\n" + address.getResults().get(1).getLand().getAddition0().getValue();
                        }

                        //tv_calendar_schedule_add_address.setText(Integer.toString(address.getResults().size()));
                        tv_destination_show_gps.setText(maddress);
                    }
                    else{
                        tv_destination_show_gps.setText("위치 정보가 없습니다.");
                    }
                }

                @Override
                public void onFailure(Call<Address> call, Throwable t) {
                    tv_destination_show_gps.setText(t.getMessage());
                }
            });
        }catch (Exception e){
            tv_destination_show_gps.setText(e.getMessage());
        }
    }
}
