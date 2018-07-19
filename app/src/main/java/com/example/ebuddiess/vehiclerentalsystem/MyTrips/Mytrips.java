package com.example.ebuddiess.vehiclerentalsystem.MyTrips;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.ManageCars.Car;
import com.example.ebuddiess.vehiclerentalsystem.Orders.MyOrderDetailsDatabase;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Mytrips extends AppCompatActivity {
RecyclerView mytriprecyclerView;
FirebaseAuth currentUser;
DatabaseReference mytripsdatabaserefrence;
List<MyOrderDetailsDatabase> orderlist;
MyTripsAdapter myTripsAdapter;
String isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);
        isAdmin = getIntent().getStringExtra("isAdmin");
        mytriprecyclerView = (RecyclerView)findViewById(R.id.mytrips_recyclerview);
        mytripsdatabaserefrence = FirebaseDatabase.getInstance().getReference("Orders");
        mytriprecyclerView.setHasFixedSize(true);
        currentUser = FirebaseAuth.getInstance();
        mytriprecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderlist = new ArrayList<MyOrderDetailsDatabase>();
        loadOrders();
    }

    private void loadOrders() {
    mytripsdatabaserefrence.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            orderlist.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
              if(isAdmin.equals("false")){
                  if(ds.getValue().toString().contains(currentUser.getUid())){
                      MyOrderDetailsDatabase order = ds.getValue(MyOrderDetailsDatabase.class);
                      orderlist.add(order);
                  }
              }else{
                  MyOrderDetailsDatabase order = ds.getValue(MyOrderDetailsDatabase.class);
                  orderlist.add(order);
              }
            }
            myTripsAdapter = new MyTripsAdapter(orderlist,Mytrips.this);
            mytriprecyclerView.setAdapter(myTripsAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}
