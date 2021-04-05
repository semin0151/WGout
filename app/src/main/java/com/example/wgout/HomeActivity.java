package com.example.wgout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.wgout.Data.Address;
import com.naver.maps.geometry.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private Button btn_drawer;
    private Button btn_calender;
    private Button btn_destination;
    private Button btn_luck;
    private Button btn_weather;

    private TextView tv_home_date;

    private DrawerLayout drawerLayout;
    private View drawerView;
    private String strDate;
    private String maddress;
    private String key_id = "q618nmd8vn";
    private String key = "DjrtsY4erRXEe41gTfwLZj0dQmldbk7ZhzI4hEVb";

    private ReverseGeocoderClient reverseGeocoderClient;
    private ReverseGeocoderInterface reverseGeocoderInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();

        init_view();
        btn_clicked();

        strDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date(System.currentTimeMillis()));
        tv_home_date.setText(strDate);

        try{
            //현재위치 + 주소
            //GpsGetter gpsGetter = new GpsGetter(this);
            //CallRetrofit(new LatLng(gpsGetter.getLatitude(), gpsGetter.getLongitude()));
        }catch (Exception e){
            tv_home_date.setText(e.getMessage());
        }

    }

    private void init_view(){
        btn_drawer = (Button)findViewById(R.id.btn_drawer);
        btn_calender = (Button)findViewById(R.id.btn_calendar);
        btn_destination = (Button)findViewById(R.id.btn_destination);
        btn_luck = (Button)findViewById(R.id.btn_luck);
        btn_weather = (Button)findViewById(R.id.btn_weather);

        tv_home_date = (TextView)findViewById(R.id.tv_home_date);

        drawerLayout = (DrawerLayout)findViewById(R.id.activity_home);
        drawerView = (View)findViewById(R.id.drawer);
    }

    private void btn_clicked(){
        btn_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        btn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
        btn_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(HomeActivity.this, DestinationActivity.class);
                startActivity(intent);
            }
        });
        btn_luck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(HomeActivity.this, LuckActivity.class);
                startActivity(intent);
            }
        });
        btn_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Intent intent = new Intent(HomeActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });
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
                        tv_home_date.setText(maddress);
                    }
                    else{
                        tv_home_date.setText("위치 정보가 없습니다.");
                    }
                }

                @Override
                public void onFailure(Call<Address> call, Throwable t) {
                    tv_home_date.setText(t.getMessage());
                }
            });
        }catch (Exception e){
            tv_home_date.setText(e.getMessage());
        }
    }
}
