package com.example.wgout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class DestinaitonAddActivity extends AppCompatActivity implements OnMapReadyCallback, NaverMap.OnMapClickListener {
    private Button btn_destination_add_ok;
    private EditText et_destination_add;
    private TextView tv_destination_add_address;

    private MapView mv_destination_add;

    private ReverseGeocoderClient reverseGeocoderClient;
    private ReverseGeocoderInterface reverseGeocoderInterface;

    private String key_id = "q618nmd8vn";
    private String key = "DjrtsY4erRXEe41gTfwLZj0dQmldbk7ZhzI4hEVb";

    private NaverMap naverMap;

    private SQLiteDatabase sqliteDB;

    private String maddress;

    private Marker marker = new Marker();
    private LatLng mlatlng = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_add);

        init_view();
        init_intent();

        sqliteDB = init_DB();
        init_tables();
        save_values();

        mv_destination_add.onCreate(savedInstanceState);
        mv_destination_add.getMapAsync(this);
    }

    private void init_view(){
        btn_destination_add_ok = (Button)findViewById(R.id.btn_destination_add_ok);
        et_destination_add = (EditText)findViewById(R.id.et_destination_add);
        tv_destination_add_address = (TextView)findViewById(R.id.tv_destination_add_address);
        mv_destination_add = (MapView)findViewById(R.id.mv_destination_add);
    }

    private void init_intent(){
        Intent intent = getIntent();
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

    //need to save to DB
    private void save_values(){
        btn_destination_add_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sqlInsert = "INSERT INTO DESTINATION" +
                            "(LOCATION, LAT, LNG) VALUES (" +
                            "'" + et_destination_add.getText().toString() + "'" + "," +
                            mlatlng.latitude + "," +
                            mlatlng.longitude + ")";
                    sqliteDB.execSQL(sqlInsert);

                    et_destination_add.setText("");
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    setResult(100);
                    finish();
                }catch (Exception e){
                    et_destination_add.setText(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        //map 초기 위치 현재위치로
        mlatlng = new LatLng(37.3399, 126.733);

        CameraPosition cameraPosition = new CameraPosition(mlatlng,16);

        naverMap.setCameraPosition(cameraPosition);

        naverMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        marker.setMap(null);
        marker.setPosition(latLng);
        marker.setMap(naverMap);

        CallRetrofit(latLng);
    }

    public void CallRetrofit(LatLng latlng){
        try {
            reverseGeocoderClient = ReverseGeocoderClient.getInstance();
            reverseGeocoderInterface = ReverseGeocoderClient.getReverseGeocoderInterface();

            mlatlng = latlng;
            reverseGeocoderInterface.getAddress(Double.toString(latlng.longitude) + "," + Double.toString(latlng.latitude),
                    "json",
                    "addr,roadaddr",
                    key_id,
                    key).enqueue(new Callback<Address>() {
                @Override
                public void onResponse(Call<Address> call, Response<Address> response) {
                    //need to set out of country
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
                        tv_destination_add_address.setText(maddress);
                    }
                    else{
                        tv_destination_add_address.setText("위치 정보가 없습니다.");
                    }

                }

                @Override
                public void onFailure(Call<Address> call, Throwable t) {

                }
            });
        }catch (Exception e){
            tv_destination_add_address.setText(e.getMessage());
        }
    }
}
