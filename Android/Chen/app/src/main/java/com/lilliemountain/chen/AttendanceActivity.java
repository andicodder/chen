package com.lilliemountain.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AttendanceActivity extends AppCompatActivity {
    String emailAddress;
    List<String> monthYearList=new ArrayList<>();
    List<Double> totalLecturesList=new ArrayList<>();
    Map<String,Boolean> attendanceMap=new HashMap<>();
    RecyclerView rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        rec=findViewById(R.id.rec);
        rec.setLayoutManager(new LinearLayoutManager(this));
        emailAddress=getIntent().getStringExtra("emailAddress");
        FirebaseDatabase.getInstance().getReference("lilliemountain-college").child("attendance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                monthYearList.clear();
                totalLecturesList.clear();
                attendanceMap.clear();
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    monthYearList.add(ds.getKey());
                    Log.e( "monthYearList: ",ds.getKey() );
                    totalLecturesList.add(Double.parseDouble(ds.child("totalLectures").getValue()+""));
                    for (DataSnapshot ds1:
                            ds.child(emailAddress).getChildren()) {
                        attendanceMap.put(ds1.getKey(), (Boolean) ds1.getValue());
                        Log.e( "getKey: ",ds1.getKey() );
                        Log.e( "getValue: ",ds1.getValue()+"" );
                    }
                }
                rec.setAdapter(new AttendanceAdapter(monthYearList,attendanceMap,totalLecturesList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
