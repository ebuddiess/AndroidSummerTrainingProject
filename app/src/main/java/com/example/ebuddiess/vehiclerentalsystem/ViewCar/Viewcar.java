package com.example.ebuddiess.vehiclerentalsystem.ViewCar;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.ManageCars.Car;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Viewcar extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    Button orderby,filter;
    RelativeLayout progressbarLayout;
    DatabaseReference databaseReference;
    List<Car> carlist;
    Dialog orderdialog,filterdialog;
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
        orderby = (Button)findViewById(R.id.car_orderby_btn);
        filter =(Button)findViewById(R.id.car_filter_btn);
        orderdialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        orderdialog.setContentView(R.layout.car_order_by_dialog);
        orderby.setOnClickListener(this);
        filter.setOnClickListener(this);
    }

    private void loadCars() {
         databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              carlist.clear();
                 for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getValue().toString().contains(city)){
                        Car car = ds.getValue(Car.class);
                        carlist.add(car);
                    }
                 }
                 carAdapter = new ViewCarAdapter(carlist,Viewcar.this,city,noofdays,startdate,starttime,enddate,endtime,doorpickuppricingstatus);
                 recyclerView.setAdapter(carAdapter);
                 progressbarLayout.setVisibility(View.GONE);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.car_orderby_btn:openOrderDialog();break;
            case R.id.car_filter_btn:openFilterDialog();break;
        }
    }

    private void openFilterDialog() {
        filterdialog = new Dialog(this);
        filterdialog.setContentView(R.layout.carfilterdialog);
        Button close = filterdialog.findViewById(R.id.filter_closebtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterdialog.dismiss();
            }
        });
        filterdialog.show();
        String carcategory;
        RadioGroup carcategoryradiogroup = filterdialog.findViewById(R.id.filterradiogroupcarcategory);
        carcategoryradiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.filter_carcompact:filterCar("compact");break;
                    case R.id.filter_carhatchback:filterCar("Hatchback");break;
                    case R.id.filter_carsuv:filterCar("SUV");break;
                }
            }
        });
    }

    private void filterCar(final String carcategory) {
     Query query = databaseReference.orderByChild("carcategory");
     query.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             carlist.clear();
             for (DataSnapshot ds : dataSnapshot.getChildren()) {
                 if(ds.getValue().toString().contains(city) && ds.getValue().toString().contains(carcategory)){
                     Car car = ds.getValue(Car.class);
                     carlist.add(car);
                 }
             }
             carAdapter = new ViewCarAdapter(carlist,Viewcar.this,city,noofdays,startdate,starttime,enddate,endtime,doorpickuppricingstatus);
             recyclerView.setAdapter(carAdapter);
             carAdapter.notifyDataSetChanged();
             filterdialog.dismiss();
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });
    }

    private void openOrderDialog() {
     orderdialog.show();
     Button close = (Button)orderdialog.findViewById(R.id.orderbyclosebtn);
     Button orderbycarname = (Button)orderdialog.findViewById(R.id.orderbycarnamebtn);
     Button orderbypricing = (Button)orderdialog.findViewById(R.id.orderbypricingbtn);
     orderbypricing.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Query orderbycarpricing = databaseReference.orderByChild("pricing");
             orderbycarpricing.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     carlist.clear();
                     for (DataSnapshot ds : dataSnapshot.getChildren()) {
                         if(ds.getValue().toString().contains(city)){
                             Car car = ds.getValue(Car.class);
                             carlist.add(car);
                         }
                     }
                     carAdapter = new ViewCarAdapter(carlist,Viewcar.this,city,noofdays,startdate,starttime,enddate,endtime,doorpickuppricingstatus);
                     recyclerView.setAdapter(carAdapter);
                     carAdapter.notifyDataSetChanged();
                     orderdialog.dismiss();
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
         }
     });

     close.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             orderdialog.dismiss();
         }
     });

     orderbycarname.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Query orderbycarname = databaseReference.orderByChild("carName");
             orderbycarname.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     carlist.clear();
                     for (DataSnapshot ds : dataSnapshot.getChildren()) {
                         if(ds.getValue().toString().contains(city)){
                             Car car = ds.getValue(Car.class);
                             carlist.add(car);
                         }
                     }
                     carAdapter = new ViewCarAdapter(carlist,Viewcar.this,city,noofdays,startdate,starttime,enddate,endtime,doorpickuppricingstatus);
                     recyclerView.setAdapter(carAdapter);
                     carAdapter.notifyDataSetChanged();
                     orderdialog.dismiss();
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });
         }
     });

    }
}
