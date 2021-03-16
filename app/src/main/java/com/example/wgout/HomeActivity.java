package com.example.wgout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();

        init_view();
        btn_clicked();
        //drawerLayout.addDrawerListener(listener);

        strDate = new SimpleDateFormat("yyyy.MM.dd").format(new Date(System.currentTimeMillis()));
        tv_home_date.setText(strDate);

    }

    private void init_view(){
        btn_drawer = (Button)findViewById(R.id.btn_drawer);
        btn_calender = (Button)findViewById(R.id.btn_calender);
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
                Intent intent = new Intent(HomeActivity.this, CalenderActivity.class);
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
/*
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
 */
}
