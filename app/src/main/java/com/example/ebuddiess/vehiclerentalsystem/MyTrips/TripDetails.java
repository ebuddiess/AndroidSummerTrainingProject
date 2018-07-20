package com.example.ebuddiess.vehiclerentalsystem.MyTrips;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.Orders.Orders;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.WelcomeUser.WelcomeUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TripDetails extends AppCompatActivity {
String orderid;
String carimageurl;
DatabaseReference tripdetails;
ImageView carimage;
Button canceltrip;
TextView carname,carcategory,startdate,enddate,noofdays,totalprice,carperdayprice,locationprice,doorpickingprice,carcity,picklocation,tripstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        orderid = getIntent().getStringExtra("orderid");
        carimageurl = getIntent().getStringExtra("carimage");
        carname = (TextView)findViewById(R.id.mytrip_carname);
        carimage = (ImageView)findViewById(R.id.tripdetailsimageview);
        carcategory = (TextView)findViewById(R.id.mytrip_carcategory);
        startdate = (TextView)findViewById(R.id.mytrip_startdate);
        enddate = (TextView)findViewById(R.id.mytrip_enddate);
        noofdays = (TextView)findViewById(R.id.mytrip_noofdays);
        totalprice = (TextView)findViewById(R.id.mytrip_totalprice);
        carperdayprice = (TextView)findViewById(R.id.mytrip_carperdayprice);
        locationprice = (TextView)findViewById(R.id.mytrip_locationprice);
        doorpickingprice = (TextView)findViewById(R.id.mytrip_doorpickingprice);
        carcity = (TextView)findViewById(R.id.mytrip_carcity);
        picklocation = (TextView)findViewById(R.id.mytrip_selectedcity);
        tripstatus = (TextView)findViewById(R.id.mytrip_status);
        tripdetails = FirebaseDatabase.getInstance().getReference("Orders").child(orderid);
        getTripdetails();
        Glide.with(TripDetails.this).load(carimageurl).apply(new RequestOptions().centerCrop()).into(carimage);
        canceltrip = (Button)findViewById(R.id.mytrip_Canceltrip);
        canceltrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripdetails.removeValue();
                finish();
                Toast.makeText(TripDetails.this,"Booking Cancelled",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TripDetails.this, WelcomeUser.class));
            }
        });
    }

    private void getTripdetails() {
    tripdetails.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists() && dataSnapshot!=null){
                String startdatetxt,starttimetxt,enddatetxt,endttimetxt;
                carname.setText(dataSnapshot.child("carname").getValue().toString());
                startdatetxt    = dataSnapshot.child("startdate").getValue().toString();
                starttimetxt    = dataSnapshot.child("starttime").getValue().toString();
                enddatetxt      = dataSnapshot.child("enddate").getValue().toString();
                endttimetxt     = dataSnapshot.child("endttime").getValue().toString();
                carcategory.setText(dataSnapshot.child("carcategory").getValue().toString());
                startdate.setText(dataSnapshot.child("startdate").getValue().toString()+":"+dataSnapshot.child("starttime").getValue().toString());
                enddate.setText(dataSnapshot.child("enddate").getValue().toString()+":"+dataSnapshot.child("endttime").getValue().toString());
                noofdays.setText(dataSnapshot.child("noofdays").getValue().toString());
                totalprice.setText(dataSnapshot.child("totalprice").getValue().toString());
                carperdayprice.setText(dataSnapshot.child("carperdayprice").getValue().toString());
                if(dataSnapshot.child("locationprice").exists()){
                    picklocation.setText(dataSnapshot.child("selectedcity").getValue().toString());
                    locationprice.setText(dataSnapshot.child("locationprice").getValue().toString());
                }else{
                    picklocation.setText("NONE");
                    locationprice.setText("0");
                }
                doorpickingprice.setText(dataSnapshot.child("doorpickingprice").getValue().toString());
                carcity.setText(dataSnapshot.child("carcity").getValue().toString());
                String status = getStatus(startdatetxt,starttimetxt,enddatetxt,endttimetxt);
                tripstatus.setText(status);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    });
    }

    private String  getStatus(String startdatetxt, String starttimetxt, String enddatetxt, String endttimetxt) {
        String status = "";
        Date startdateformat,enddateformat,starttimeformat,endtimeformat;
        SimpleDateFormat dateFormat,hourformat;
        Calendar currentcalendar;
        currentcalendar = Calendar.getInstance();
        Date currentDateObject,currentTimeObject;
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        hourformat = new SimpleDateFormat("HH:mm");
        String currentDatetxt =  dateFormat.format(currentcalendar.getTime());
        String currentTimetxt = hourformat.format(currentcalendar.getTime());
        try {
            currentDateObject = new SimpleDateFormat("dd-MM-yyyy").parse(currentDatetxt.toString());
            currentTimeObject = new SimpleDateFormat("HH:mm").parse(currentTimetxt.toString());
            startdateformat = new SimpleDateFormat("dd-MM-yyyy").parse(startdatetxt);
            enddateformat =   new SimpleDateFormat("dd-MM-yyyy").parse(enddatetxt);
            starttimeformat = new SimpleDateFormat("HH:mm").parse(starttimetxt);
            endtimeformat =   new SimpleDateFormat("HH:mm").parse(endttimetxt);
            if((currentDateObject.equals(startdateformat)||currentDateObject.after(startdateformat))&&currentDateObject.before(enddateformat)){
                status = "RUNNING";
            }else if(currentDateObject.after(enddateformat)&& currentTimeObject.after(endtimeformat)){
                status = "Completed";
            }else if(currentDateObject.before(startdateformat)&& currentTimeObject.before(starttimeformat)){
                status = "Not Started";
            }else if((currentDateObject.after(startdateformat))&&(currentDateObject.before(enddateformat)&&currentTimeObject.before(endtimeformat))){
                status = "RUNNING";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return status;
    }

}
