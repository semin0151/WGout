package com.example.wgout;

import android.content.Intent;
import android.database.Cursor;
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

public class DestinationActivity extends AppCompatActivity {
    private Button btn_destination_add;
    private TextView tv_destination_no;

    private RecyclerView rv_destination;

    private DestinationRecyclerAdapter adapter_destination;

    private static final int CODE_RESULT = 100;

    private SQLiteDatabase sqliteDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        init_intent();
        init_view();
        sqliteDB = init_DB();
        init_tables();
        init_rv();


        btn_clicked();
        load_values();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == CODE_RESULT){
            init_rv();
            load_values();
        }
    }

    private void init_view(){
        btn_destination_add = (Button)findViewById(R.id.btn_destination_add);
        tv_destination_no = (TextView)findViewById(R.id.tv_destination_no);
        rv_destination = (RecyclerView)findViewById(R.id.rv_destination);
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

    private void btn_clicked(){
        btn_destination_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinationActivity.this, DestinaitonAddActivity.class);
                startActivityForResult(intent,0);
            }
        });
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

    private void init_rv(){
        rv_destination.setLayoutManager(new LinearLayoutManager(this));

        adapter_destination = new DestinationRecyclerAdapter();

        rv_destination.setAdapter(adapter_destination);
    }

    private void load_values(){
        if(sqliteDB != null){
            Cursor cursor_destination = null;
            cursor_destination = sqliteDB.rawQuery("SELECT * FROM DESTINATION", null);

            while(cursor_destination.moveToNext()){
                adapter_destination.addItem(cursor_destination.getString(0),cursor_destination.getDouble(1), cursor_destination.getDouble(2));
            }
        }
        if (adapter_destination.getItemCount()==0) {
            rv_destination.setVisibility(View.GONE);
            tv_destination_no.setVisibility(View.VISIBLE);
        }
    }
}
