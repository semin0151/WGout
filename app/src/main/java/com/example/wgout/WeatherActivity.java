package com.example.wgout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgout.Data.UltraSrtFcst;
import com.example.wgout.Data.VilageFcst;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    private RecyclerView rv_weather;

    private TextView tv_test1;
    private TextView tv_test2;

    private WeatherAdapter adapter_weather;

    private LinearLayoutManager manager_vilage;

    private WeatherUltraSrtFcstClient ultraSrtFcstClient;
    private WeatherUltraSrtFcstInterface ultraSrtFcstInterface;

    private WeatherVilageFcstClient vilageFcstClient;
    private WeatherVilageFcstInterface vilageFcstInterface;

    private String serviceKey = "J1uWhMdL85rUtm1/ogRUgAehBm4pAZ2QJBmZ8ytsj9q5VPkKoPyTuUWItYfi5LAXgTA+mn2ERVnNZDQsfwYxpg==";
    private String strDate = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
    private String strDate2;
    private String strTime, strTime2;
    private String strCheckDate = "1001";
    private String strCompareDate = "";

    private static final int LGT = 0, PTY = 1, RN1 = 2, SKY = 3, T1H = 4, REH = 5, UUU = 6, VVV = 7, VEC = 8, WSD = 9, ultra = 100, vilage = 200;

    private String vilage_date = "", vilage_time = "", vilage_POP = "", vilage_PTY = "", vilage_REH = "", vilage_SKY = "", vilage_T3H = "";

    private int size;

    private int check;
    private double lat, lng;
    private int x, y;
    private int checkDate;
    private String checkTime;

    double RE = 6371.00877; // 지구 반경(km)
    double GRID = 5.0; // 격자 간격(km)
    double SLAT1 = 30.0; // 투영 위도1(degree)
    double SLAT2 = 60.0; // 투영 위도2(degree)
    double OLON = 126.0; // 기준점 경도(degree)
    double OLAT = 38.0; // 기준점 위도(degree)
    double XO = 43; // 기준점 X좌표(GRID)
    double YO = 136; // 기1준점 Y좌표(GRID)

    double DEGRAD = Math.PI / 180.0;
    double RADDEG = 180.0 / Math.PI;

    double re = RE / GRID;
    double slat1 = SLAT1 * DEGRAD;
    double slat2 = SLAT2 * DEGRAD;
    double olon = OLON * DEGRAD;
    double olat = OLAT * DEGRAD;

    double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
    double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();

        init_view();
        init_recycler();
        getGps();
        //tv_test1.setText(Double.toString(lat));
        //tv_test1.setText(Double.toString(lng));
        //tv_test1.setText(Integer.toString(x));
        //tv_test2.setText(Integer.toString(y));
        callUltraSrtFcst();
        callVilageFcst();

    }

    private void init_view(){
        rv_weather = (RecyclerView)findViewById(R.id.rv_weather);

        tv_test1 = (TextView)findViewById(R.id.tv_test1);
        tv_test2 = (TextView)findViewById(R.id.tv_test2);
    }

    private void init_recycler(){
        manager_vilage = new LinearLayoutManager(this);
        manager_vilage.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_weather.setLayoutManager(manager_vilage);
        adapter_weather = new WeatherAdapter();
        rv_weather.setAdapter(adapter_weather);
    }

    private void getGps() {
        GpsGetter gpsGetter = new GpsGetter(this);

        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        ro = re * sf / Math.pow(ro, sn);

        lat = gpsGetter.getLatitude();
        lng = gpsGetter.getLongitude();

        double ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra,sn);
        double theta = lng * DEGRAD - olon;
        if(theta > Math.PI) theta -= 2.0 * Math.PI;
        if(theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        x = (int)Math.floor(ra * Math.sin(theta) + XO +0.5);
        y = (int)Math.floor(ro - ra * Math.cos(theta) + YO +0.5);
    }

    private void callUltraSrtFcst(){
        try{
            ultraSrtFcstClient = WeatherUltraSrtFcstClient.getInstance();
            ultraSrtFcstInterface = WeatherUltraSrtFcstClient.getUltraSrtFcstInterface();

            strTime = new SimpleDateFormat("HHmm").format(new Date(System.currentTimeMillis()));

            ultraSrtFcstInterface.getData(serviceKey, 10000, "JSON", strDate, strTime, x, y).
                    enqueue(new Callback<UltraSrtFcst>() {
                        @Override
                        public void onResponse(Call<UltraSrtFcst> call, Response<UltraSrtFcst> response) {
                            UltraSrtFcst ultraSrtFcst = response.body();
                            // 정상적인 결과값 출력될 때

                            if (ultraSrtFcst.getResponse().getHeader().getResultCode().equals("00")) {

                                size = ultraSrtFcst.getResponse().getBody().getItems().getItem().size() / 10;

                                for (int i = 0; i < size; i++) {
                                    adapter_weather.addItem(ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstDate(),
                                            ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstTime(),
                                            ultra,
                                            "0",
                                            ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstValue(),
                                            ultraSrtFcst.getResponse().getBody().getItems().getItem().get(RN1 * size + i).getFcstValue(),
                                            ultraSrtFcst.getResponse().getBody().getItems().getItem().get(SKY * size + i).getFcstValue(),
                                            ultraSrtFcst.getResponse().getBody().getItems().getItem().get(T1H * size + i).getFcstValue(),
                                            ultraSrtFcst.getResponse().getBody().getItems().getItem().get(REH * size + i).getFcstValue());
                                    checkDate = Integer.parseInt(ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstDate());
                                    checkTime = "";
                                    for (int j = 0; j < 4; j++) {
                                        checkTime += ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstTime().charAt(j);
                                    }
                                }

                                rv_weather.setAdapter(adapter_weather);

                            }

                            // API 업데이트 안되었을 때
                            else {
                                //여기에서 00시일때 설정

                                strTime = new SimpleDateFormat("HHmm").format(new Date(System.currentTimeMillis() - (3600 * 1000)));
                                ultraSrtFcstInterface.getData(serviceKey, 10000, "JSON", strDate, strTime, x, y).enqueue(new Callback<UltraSrtFcst>() {
                                    @Override
                                    public void onResponse(Call<UltraSrtFcst> call, Response<UltraSrtFcst> response) {
                                        UltraSrtFcst ultraSrtFcst = response.body();

                                        size = ultraSrtFcst.getResponse().getBody().getItems().getItem().size() / 10;
                                        for (int i = 0; i < size; i++) {

                                            adapter_weather.addItem(ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstDate(),
                                                    ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstTime(),
                                                    ultra,
                                                    "0",
                                                    ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstValue(),
                                                    ultraSrtFcst.getResponse().getBody().getItems().getItem().get(RN1 * size + i).getFcstValue(),
                                                    ultraSrtFcst.getResponse().getBody().getItems().getItem().get(SKY * size + i).getFcstValue(),
                                                    ultraSrtFcst.getResponse().getBody().getItems().getItem().get(T1H * size + i).getFcstValue(),
                                                    ultraSrtFcst.getResponse().getBody().getItems().getItem().get(REH * size + i).getFcstValue());
                                            checkDate = Integer.parseInt(ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstDate());
                                            checkTime = "";
                                            for (int j = 0; j < 4; j++) {
                                                checkTime += ultraSrtFcst.getResponse().getBody().getItems().getItem().get(PTY * size + i).getFcstTime().charAt(j);
                                            }
                                        }
                                        rv_weather.setAdapter(adapter_weather);

                                    }

                                    @Override
                                    public void onFailure(Call<UltraSrtFcst> call, Throwable t) {

                                    }
                                });

                            }
                            //됐다가 안됐다가 함. 일단 됨
                            strCheckDate = Integer.toString(checkDate) + checkTime;
                            strCheckDate = strCheckDate.substring(2, 10);
                            tv_test1.setText(strCheckDate);
                        }

                        @Override
                        public void onFailure(Call<UltraSrtFcst> call, Throwable t) {

                        }
                    });
        }
        catch (Exception e){

        }
    }

    private void callVilageFcst(){
        try{
            vilageFcstClient = WeatherVilageFcstClient.getInstance();
            vilageFcstInterface = WeatherVilageFcstClient.getVilageFcstInterface();
            //여기서 00시일때 설정

            strDate2 = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis() - (1000 * 7200)));
            strTime2 = new SimpleDateFormat("HH").format(new Date(System.currentTimeMillis() - (1000 * 7200)));
            check = ((Integer.parseInt(strTime2) / 3) * 3) + 2;
            strTime2 = Integer.toString(check) + "00";
            tv_test2.setText(strDate2 + strTime2);
            vilageFcstInterface.getData(serviceKey, 10000, "JSON", strDate2, strTime2, x, y).
                    enqueue(new Callback<VilageFcst>() {
                        @Override
                        public void onResponse(Call<VilageFcst> call, Response<VilageFcst> response) {
                            VilageFcst vilageFcst = response.body();

                            if (vilageFcst.getResponse().getHeader().getResultCode().equals("00")) {

                                for (int i = 0; i < vilageFcst.getResponse().getBody().getItems().getItem().size(); i++) {
                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("POP")) {
                                        vilage_date = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstDate();
                                        vilage_time = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstTime();
                                        vilage_POP = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                    }

                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("PTY")) {
                                        vilage_PTY = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                    }

                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("REH")) {
                                        vilage_REH = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                    }

                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("SKY")) {
                                        vilage_SKY = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                    }

                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("T3H")) {
                                        vilage_T3H = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";

                                        strCompareDate = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstDate() +
                                               vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstTime();
                                        strCompareDate = strCompareDate.substring(2, 10);
                                        tv_test2.setText(strCompareDate);
                                        if (Integer.parseInt(strCompareDate) > Integer.parseInt(strCheckDate)) {
                                            adapter_weather.addItem(vilage_date, vilage_time, vilage, vilage_POP, vilage_PTY, "0", vilage_SKY, vilage_T3H, vilage_REH);
                                        }
                                    }
                                    rv_weather.setAdapter(adapter_weather);
                                }

                            } else {
                                strDate2 = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis() - (1000 * 10800)));
                                strTime2 = new SimpleDateFormat("HHmm").format(new Date(System.currentTimeMillis() - (1000 * 10800)));

                                vilageFcstInterface.getData(serviceKey, 10000, "JSON", strDate2, strTime2, x, y).
                                        enqueue(new Callback<VilageFcst>() {
                                            @Override
                                            public void onResponse(Call<VilageFcst> call, Response<VilageFcst> response) {
                                                VilageFcst vilageFcst = response.body();

                                                for (int i = 0; i < vilageFcst.getResponse().getBody().getItems().getItem().size(); i++) {
                                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("POP")) {
                                                        vilage_date = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstDate();
                                                        vilage_time = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstTime();
                                                        vilage_POP = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                                    }

                                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("PTY")) {
                                                        vilage_PTY = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                                    }

                                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("REH")) {
                                                        vilage_REH = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                                    }

                                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("SKY")) {
                                                        vilage_SKY = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";
                                                    }

                                                    if (vilageFcst.getResponse().getBody().getItems().getItem().get(i).getCategory().equals("T3H")) {
                                                        vilage_T3H = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstValue() + " ";

                                                        strCompareDate = vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstDate() +
                                                                vilageFcst.getResponse().getBody().getItems().getItem().get(i).getFcstTime();
                                                        strCompareDate = strCompareDate.substring(2, 10);
                                                        tv_test2.setText(strCompareDate);
                                                        if (Integer.parseInt(strCompareDate) > Integer.parseInt(strCheckDate)) {
                                                            adapter_weather.addItem(vilage_date, vilage_time, vilage, vilage_POP, "0", vilage_PTY, vilage_SKY, vilage_T3H, vilage_REH);
                                                        }
                                                    }
                                                    rv_weather.setAdapter(adapter_weather);

                                                }


                                            }

                                            @Override
                                            public void onFailure(Call<VilageFcst> call, Throwable throwable) {

                                            }
                                        });

                            }
                        }

                        @Override
                        public void onFailure(Call<VilageFcst> call, Throwable t) {

                        }
                    });

        }
        catch (Exception e){

        }
    }
}
