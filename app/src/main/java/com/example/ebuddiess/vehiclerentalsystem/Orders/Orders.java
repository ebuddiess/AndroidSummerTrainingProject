package com.example.ebuddiess.vehiclerentalsystem.Orders;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.ManageCars.Car;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.ViewCar.Viewcar;
import com.example.ebuddiess.vehiclerentalsystem.WelcomeUser.WelcomeUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {
TextView order_carname,order_carcategory,order_startdate,order_enddate,order_noofdays,order_totalprice,order_carpriceperday,order_servicefare;
Button order_confirm,order_thinkagain;
DatabaseReference myorders;
String orderid ;
FirebaseAuth currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        order_carname = (TextView)findViewById(R.id.orderpage_carname);
        order_carcategory = (TextView)findViewById(R.id.orderpage_carcategory);
        order_startdate = (TextView)findViewById(R.id.orderpage_startdate);
        order_enddate = (TextView)findViewById(R.id.orderpage_enddate);
        order_noofdays = (TextView)findViewById(R.id.orderpage_noofdays);
        order_totalprice = (TextView)findViewById(R.id.orderpage_totalprice);
        order_carpriceperday = (TextView)findViewById(R.id.orderpage_carperdayprice);
        order_servicefare = (TextView)findViewById(R.id.orderpage_servicefare);
        order_confirm = (Button)findViewById(R.id.confirm_order);
        order_thinkagain = (Button)findViewById(R.id.goback_from_orderpage);
        orderid = getIntent().getStringExtra("orderid");
        currentUser = FirebaseAuth.getInstance();
        myorders = FirebaseDatabase.getInstance().getReference("Orders").child(orderid);
        order_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Orders.this,"Booked Sucesfully ",Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(Orders.this, WelcomeUser.class));
            }
        });

        order_thinkagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myorders.removeValue();
                finish();
                Toast.makeText(Orders.this,"Booking Cancelled",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Orders.this, WelcomeUser.class));
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(orderid!=null && currentUser.getCurrentUser()!=null){
            myorders.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot!=null && dataSnapshot.exists()){
                   order_carname.setText("Carname: "+dataSnapshot.child("carname").getValue().toString());
                   order_carcategory.setText("Car category: "+dataSnapshot.child("carcategory").getValue().toString());
                   order_startdate.setText("Start Date: "+dataSnapshot.child("startdate").getValue().toString()+":"+dataSnapshot.child("starttime").getValue().toString());
                   order_enddate.setText("End date: "+dataSnapshot.child("enddate").getValue().toString()+":"+dataSnapshot.child("endttime").getValue().toString());
                   order_noofdays.setText("Booked For "+dataSnapshot.child("noofdays").getValue().toString()+" days");
                   order_totalprice.setText("Total price : "+dataSnapshot.child("totalprice").getValue().toString());
                   order_carpriceperday.setText("Car per Day price : "+dataSnapshot.child("carperdayprice").getValue().toString());
                   if(dataSnapshot.child("doorpickingprice").getValue().toString().equals("0")){
                       order_servicefare.setText("Service fare : "+dataSnapshot.child("locationprice").getValue().toString());
                   }else if(!dataSnapshot.child("doorpickingprice").getValue().toString().equals("0")){
                       order_servicefare.setText("Service fare : "+dataSnapshot.child("doorpickingprice").getValue().toString());
                   }
                   }else{
                   return;
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
