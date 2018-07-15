package com.example.ebuddiess.vehiclerentalsystem.WelcomeUser;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.Authentication.Signin;
import com.example.ebuddiess.vehiclerentalsystem.ManageCars.ManageCar;
import com.example.ebuddiess.vehiclerentalsystem.ManageProfile.ManageProfile;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.ViewCar.Viewcar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class WelcomeUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
NavigationView user_nav_view;
FirebaseAuth currentUser;
String passablestartdate,passableenddate,passablestarttime,passableendtime;
View headerView;
DatePicker datePicker;
TimePicker timePicker;
TextView username;
Dialog selectdateandtime;
StartTime customPicker;
Spinner choosecityspinner;
String firstname;
Menu menu;
String isAdmin;
Button selectdate,selectTime;
int COUNTER,VALUELOADEDCOUNTER;
EditText start_time,end_time;
DatabaseReference firebaseDatabase;
MenuItem manageCars;
TabHost tabHost;
Button viewcar;
EndTime customEndPicker;
String startimeSelecteddate,starttimeselectedtime;
TabHost.TabSpec spec;
ImageView user_profile_image_drawer;
int noofdays;String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        user_nav_view = findViewById(R.id.user_navigation_view);
        selectdateandtime = new Dialog(this);
        selectdateandtime.setContentView(R.layout.custom_datetime_picker);
        tabHost = selectdateandtime.findViewById(R.id.tabHost);
        tabHost.setup();
//        tab1
        spec = tabHost.newTabSpec("DATE");
        spec.setContent(R.id.tab1);
        spec.setIndicator("DATE");
        tabHost.addTab(spec);
//        tab2
        spec = tabHost.newTabSpec("TIME");
        spec.setContent(R.id.tab2);
        spec.setIndicator("TIME");
        tabHost.addTab(spec);
//        -------------------------------------------------------------------------
        currentUser = FirebaseAuth.getInstance();
        user_nav_view.setNavigationItemSelectedListener(this);
        selectdate = selectdateandtime.findViewById(R.id.select_date_from_datepicker);
        selectTime  = selectdateandtime.findViewById(R.id.select_time_from_time_picker);
        datePicker  = selectdateandtime.findViewById(R.id.datepicker);
        timePicker  = selectdateandtime.findViewById(R.id.timepicker);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        headerView = user_nav_view.inflateHeaderView(R.layout.drawer_header);
        menu = user_nav_view.getMenu();
        username = headerView.findViewById(R.id.user_display_name_drawer);
        manageCars = menu.findItem(R.id.manageCars);
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isAdmin =  dataSnapshot.child("adminPower").getValue().toString();
                if(isAdmin.equals("true")){
//                    Toast.makeText(WelcomeUser.this,isAdmin,Toast.LENGTH_LONG).show();
                    manageCars.setVisible(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        user_profile_image_drawer = headerView.findViewById(R.id.user_profile_image_drawer);
        user_profile_image_drawer.setBackgroundResource(android.R.color.transparent);
        start_time = findViewById(R.id.start_time_edtText);
        end_time = findViewById(R.id.end_time_edtTxt);
        start_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        COUNTER = 0;
        viewcar = findViewById(R.id.viewcar);
        viewcar.setOnClickListener(this);
        VALUELOADEDCOUNTER = 0;
        choosecityspinner = (Spinner)findViewById(R.id.choose_city_spinner);
        noofdays = 0;
        choosecityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              city = choosecityspinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:doLogout();break;
            case R.id.user_EditProfile:openmanageProfile();break;
            case R.id.manageCars:openmanageCars();break;
        }
        return true;
    }

    private void openmanageCars() {
       startActivity(new Intent(WelcomeUser.this,ManageCar.class));
    }

    private void openmanageProfile() {
    startActivity(new Intent(WelcomeUser.this,ManageProfile.class));
    }

    private void doLogout() {
        try{
            finish();
            currentUser.signOut();
            Toast.makeText(WelcomeUser.this,"Logout Sucessfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(WelcomeUser.this, Signin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }catch (Exception e){
            Toast.makeText(WelcomeUser.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       if(currentUser.getCurrentUser()!=null){
           String uid = currentUser.getUid();
           DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
           firebaseDatabase.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   firstname = dataSnapshot.child("firstname").getValue().toString();
                   if(!firstname.isEmpty()){
                       String profileurl= dataSnapshot.child("profileurl").getValue().toString();
                       username.setText("Login as "+firstname);
                       Glide.with(getApplicationContext()).load(profileurl).apply(new RequestOptions().centerCrop()).into(user_profile_image_drawer);
                   }else if(firstname.isEmpty()){
                       username.setText("Log in As User");
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.start_time_edtText:getStartTimeData();break;
            case R.id.end_time_edtTxt:getEndTimeData();break;
            case R.id.viewcar:loadViewcar();
        }
    }

    private void loadViewcar() {
        Intent i1 = new Intent(WelcomeUser.this,Viewcar.class);
        i1.putExtra("city",city);
        i1.putExtra("noofdays",noofdays);
        i1.putExtra("startdate",passablestartdate);
        i1.putExtra("starttime",passablestarttime);
        i1.putExtra("enddate",passableenddate);
        i1.putExtra("endtime",passableendtime);
        startActivity(i1);
    }

    //END TIME CODE ------------------------------------------------------------------------------------------------------------
    private void getEndTimeData() {
        if(COUNTER==0){
            Toast.makeText(WelcomeUser.this,"SELECT START TIME FIRST",Toast.LENGTH_SHORT).show();
        }else {
            selectdateandtime.show();
            selectdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   getEndTimeDate();
                }
            });

            selectTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   boolean status =  getEndTimetime();
                   if(status==true){
                       passableenddate = customEndPicker.getDate();
                       passableendtime = customEndPicker.getTime();
                       end_time.setText(customEndPicker.getFormattedDate());
                       selectdateandtime.dismiss();
                       VALUELOADEDCOUNTER = 2;
                   }
                }
            });
        }
    }

    private void getEndTimeDate() {
        int day,year,month;
        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth();
        year = datePicker.getYear();
        GregorianCalendar calendar = new GregorianCalendar(year,month,day);
        Date d = calendar.getTime();
        customEndPicker = new EndTime();
        customEndPicker.store(day,month,year);
        String date = new SimpleDateFormat("dd-MM-YYYY").format(d).toString();
        customEndPicker.saveDate(date);
        Toast.makeText(WelcomeUser.this,"Date selected "+date,Toast.LENGTH_SHORT).show();
    }

    private boolean getEndTimetime() {
        int hour,minute;
        String endDate;
        endDate = customEndPicker.getDate();
        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        customEndPicker.saveTime(hour,minute);
        String endtime = customEndPicker.getTime();
        boolean status = customEndPicker.validate(endDate,endtime,startimeSelecteddate,starttimeselectedtime,WelcomeUser.this);
        noofdays = customEndPicker.getnoofdays();
        return  status;
    }

    //START TIME CODE ------------------------------------------------------------------------------------------------------------
    private void getStartTimeData() {
        VALUELOADEDCOUNTER =1;
        COUNTER = 1;
    selectdateandtime.show();
    selectdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getDate();
        }
    });

    selectTime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Boolean status = getTime();
            if(status==true){
                startimeSelecteddate = customPicker.getDate();
                starttimeselectedtime = customPicker.getTime();
                passablestartdate = startimeSelecteddate;
                passablestarttime = starttimeselectedtime;
                start_time.setText(customPicker.getFormattedDate());
                selectdateandtime.dismiss();
            }

        }
    });

    }


    private boolean getTime() {
        int hour,minute;
        String selectedDate;
        selectedDate = customPicker.getDate();
        hour = timePicker.getHour();
        minute = timePicker.getMinute();
        customPicker = new StartTime();
        customPicker.saveTime(hour,minute);
        String time = customPicker.getTime();
        Boolean status = customPicker.validate(selectedDate,time,WelcomeUser.this);
        return  status;
    }

    private void getDate() {
    int day,year,month;
    day = datePicker.getDayOfMonth();
    month = datePicker.getMonth();
    year = datePicker.getYear();
    GregorianCalendar calendar = new GregorianCalendar(year,month,day);
    Date d = calendar.getTime();
    customPicker = new StartTime();
    String date = new SimpleDateFormat("dd-MM-YYYY").format(d).toString();
    customPicker.saveDate(date);
    Toast.makeText(WelcomeUser.this,"Date selected "+date,Toast.LENGTH_SHORT).show();
    }
}
