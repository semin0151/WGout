package com.example.wgout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wgout.Data.Address;
import com.example.wgout.Data.Navigation;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationSearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView tv_destination_search_start;
    private TextView tv_destination_search_finish;
    private TextView tv_destination_search_information;

    private MapView mv_destination_search;

    private String location;
    private Double s_lat, s_lng, f_lat, f_lng;
    private GpsGetter gpsGetter;

    private ReverseGeocoderClient reverseGeocoderClient;
    private ReverseGeocoderInterface reverseGeocoderInterface;

    private NavigationClient navigationClient;
    private NavigationInterface navigationInterface;

    private String key_id = "q618nmd8vn";
    private String key = "CgbDcAxj3Fz6yjNZvqhTbJRn5tjaG9N5TBTCnUPr";
    private String maddress;

    private NaverMap naverMap;

    private Marker marker_start = new Marker();
    private Marker marker_finish = new Marker();
    private PathOverlay path = new PathOverlay();


    private List<LatLng> list = new ArrayList<>();

    private double minLat, maxLat, minLng, maxLng;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_search);

        init_view();
        init_intent();

        mv_destination_search.onCreate(savedInstanceState);
        mv_destination_search.getMapAsync(this);
        gpsGetter = new GpsGetter(this);

        s_lat = gpsGetter.getLatitude();
        s_lng = gpsGetter.getLongitude();

        //
        tv_destination_search_start.setText(Double.toString(s_lat) + " / " + Double.toString(s_lng) + "\n" + Double.toString(f_lat) + " / " + Double.toString(f_lng));
        callReverseGeocoder(s_lat, s_lng, tv_destination_search_start);
        callReverseGeocoder(f_lat, f_lng, tv_destination_search_finish);
        callNavigation();
    }

    private void init_view(){
        tv_destination_search_start = (TextView)findViewById(R.id.tv_destination_search_start);
        tv_destination_search_finish = (TextView)findViewById(R.id.tv_destination_search_finish);
        tv_destination_search_information = (TextView)findViewById(R.id.tv_destination_search_information);

        mv_destination_search = (MapView)findViewById(R.id.mv_destination_search);
    }

    private void init_intent(){
        Intent intent = getIntent();

        location = intent.getStringExtra("location");
        f_lat = intent.getDoubleExtra("lat",1);
        f_lng = intent.getDoubleExtra("lng",1);
    }

    @Override
    public void onMapReady(@NonNull @NotNull NaverMap naverMap) {
        this.naverMap = naverMap;
        gpsGetter = new GpsGetter(this);
        LatLng mlatlng = new LatLng(gpsGetter.getLatitude(), gpsGetter.getLongitude());
        CameraPosition cameraPosition = new CameraPosition( new LatLng( 37.34567, 126.8364), 10);
        naverMap.setCameraPosition(cameraPosition);

        marker_finish.setIconTintColor(0xff0000);
        marker_start.setPosition(mlatlng);
        marker_start.setMap(naverMap);
        marker_finish.setPosition(new LatLng(f_lat,f_lng));
        marker_finish.setMap(naverMap);

        try {
            minLat = naverMap.getContentBounds().getSouthWest().latitude;
            minLng = naverMap.getContentBounds().getSouthWest().longitude;
            maxLat = naverMap.getContentBounds().getNorthEast().latitude;
            maxLng = naverMap.getContentBounds().getNorthEast().longitude;
        }
        catch (Exception e){

        }
    }

    private void callReverseGeocoder(Double lat, Double lng, TextView tv){
        try {
            reverseGeocoderClient = ReverseGeocoderClient.getInstance();
            reverseGeocoderInterface = ReverseGeocoderClient.getReverseGeocoderInterface();

            //mlatlng = latlng;
            reverseGeocoderInterface.getAddress(Double.toString(lng) + "," + Double.toString(lat),
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

                        tv.setText(maddress);
                    }
                    else{
                        tv.setText("위치 정보가 없습니다.");
                    }

                }

                @Override
                public void onFailure(Call<Address> call, Throwable t) {

                }
            });
        }catch (Exception e){
            tv_destination_search_start.setText(e.getMessage());
        }
    }

    private void callNavigation(){
        try{
            navigationClient = NavigationClient.getInstance();
            navigationInterface = NavigationClient.getNavigationInterface();

            navigationInterface.getNavigation(Double.toString(s_lng)+","+Double.toString(s_lat),
                    Double.toString(f_lng)+","+Double.toString(f_lat),
                    "q618nmd8vn",
                    "CgbDcAxj3Fz6yjNZvqhTbJRn5tjaG9N5TBTCnUPr").enqueue(new Callback<Navigation>() {
                @Override
                public void onResponse(Call<Navigation> call, Response<Navigation> response) {
                    Navigation navigation = response.body();

                    int size = navigation.getRoute().getTraoptimal().get(0).getPath().size();
                    String str = "";

                    LatLng mlatlng = new LatLng((s_lat+f_lat) / 2, (s_lng + f_lng) / 2);
                    setCameraPosition();

                    list.clear();
                    for(int i = 0; i < size; i++){
                        list.add(new LatLng( navigation.getRoute().getTraoptimal().get(0).getPath().get(i).get(1), navigation.getRoute().getTraoptimal().get(0).getPath().get(i).get(0)));
                    }
                    path.setCoords(list);
                    path.setColor(0xff000fff);
                    path.setMap(naverMap);

                    tv_destination_search_information.setText(Integer.toString(navigation.getRoute().getTraoptimal().get(0).getSummary().getDuration() / 1000) + "sec\n" +
                            Integer.toString(navigation.getRoute().getTraoptimal().get(0).getSummary().getDistance()) + "m");
                }

                @Override
                public void onFailure(Call<Navigation> call, Throwable t) {
                    tv_destination_search_information.setText(t.getMessage());
                }
            });

        }
        catch (Exception e){
            tv_destination_search_information.setText(e.getMessage());
        }
    }

    private void setCameraPosition(){
        double zoom = 16;
        LatLng mlatlng = new LatLng((s_lat+f_lat) / 2, (s_lng + f_lng) / 2);
        CameraPosition cameraPosition = new CameraPosition(mlatlng,zoom);
        //naverMap.setCameraPosition(cameraPosition);
        //zoom 올려가면서 조절

        for(; zoom >= 1.0; zoom-=0.5){
            cameraPosition = new CameraPosition(mlatlng, zoom);
            naverMap.setCameraPosition(cameraPosition);

            minLat = naverMap.getContentBounds().getSouthWest().latitude;
            minLng = naverMap.getContentBounds().getSouthWest().longitude;
            maxLat = naverMap.getContentBounds().getNorthEast().latitude;
            maxLng = naverMap.getContentBounds().getNorthEast().longitude;

            if(s_lat > minLat && s_lat < maxLat &&
                    s_lng > minLng && s_lng < maxLng &&
                    f_lat > minLat && f_lat < maxLat &&
                    f_lng > minLng && f_lng < maxLng){
                naverMap.setCameraPosition(cameraPosition);
                return;
            }

            zoom -= 1.0;
        }
    }

}
