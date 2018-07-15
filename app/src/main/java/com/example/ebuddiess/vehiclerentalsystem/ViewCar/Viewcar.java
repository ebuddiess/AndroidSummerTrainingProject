package com.example.ebuddiess.vehiclerentalsystem.ViewCar;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.ManageCars.Car;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Viewcar extends AppCompatActivity {
    RecyclerView recyclerView;
    RelativeLayout progressbarLayout;
    DatabaseReference databaseReference;
    List<Car> carlist;
    ViewCarAdapter carAdapter;
    String city;
    Button sitepickup_btn,doorstep_btn;
    String doorpickuppricingstatus;
    String startdate,starttime;
    String enddate,endtime;
    int noofdays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewcar);
        databaseReference = FirebaseDatabase.getInstance().getReference("Cars");
        recyclerView =(RecyclerView) findViewById(R.id.car_recyclerview);
        recyclerView.setHasFixedSize(true);
        progressbarLayout = (RelativeLayout)findViewById(R.id.progressbarLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doorstep_btn = (Button)findViewById(R.id.doorstep_delivery_viewcaractivity);
        sitepickup_btn = (Button)findViewById(R.id.site_pickup_delivery_button);
        city = getIntent().getStringExtra("city");
        noofdays  = getIntent().getIntExtra("noofdays",0);
        startdate = getIntent().getStringExtra("startdate");
        starttime = getIntent().getStringExtra("starttime");
        enddate   = getIntent().getStringExtra("enddate");
        endtime   = getIntent().getStringExtra("endtime");
        System.out.println("startdate"+startdate);
        System.out.println("starttime"+starttime);
        System.out.println("enddate"+enddate);
        System.out.println("endtime"+endtime);
        doorpickuppricingstatus = "false";
        carlist   = new ArrayList<Car>();
        loadCars();
        sitepickup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorpickuppricingstatus= "false";
                Toast.makeText(Viewcar.this,"Choose Suitable Location",Toast.LENGTH_LONG).show();
                carAdapter.setDoorpickingStatus(doorpickuppricingstatus);
                carAdapter.notifyDataSetChanged();
            }
        });
        doorstep_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doorpickuppricingstatus = "true";
                Toast.makeText(Viewcar.this,"500 Rs Charge For Home Deleivery",Toast.LENGTH_LONG).show();
                carAdapter.setDoorpickingStatus(doorpickuppricingstatus);
                carAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadCars() {
         databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              carlist.clear();
                 for (DataSnapshot ds : dataSnapshot.getChildren()) {
                     Car car = ds.getValue(Car.class);
                     carlist.add(car);
                 }
                 carAdapter = new ViewCarAdapter(carlist,getApplicationContext(),city,noofdays,startdate,starttime,enddate,endtime,doorpickuppricingstatus);
                 recyclerView.setAdapter(carAdapter);
                 progressbarLayout.setVisibility(View.GONE);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }
}
