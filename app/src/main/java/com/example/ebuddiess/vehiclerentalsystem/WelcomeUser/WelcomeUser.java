package com.example.ebuddiess.vehiclerentalsystem.WelcomeUser;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.Authentication.Signin;
import com.example.ebuddiess.vehiclerentalsystem.IntroSlider.MainActivity;
import com.example.ebuddiess.vehiclerentalsystem.ManageCars.ManageCar;
import com.example.ebuddiess.vehiclerentalsystem.ManageProfile.ManageProfile;
import com.example.ebuddiess.vehiclerentalsystem.ManageProfile.UserprofileSharedPrefernces;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.util.Date;

import static android.view.View.INVISIBLE;

public class WelcomeUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
NavigationView user_nav_view;
FirebaseAuth currentUser;
View headerView;
DatePicker datePicker;
TimePicker timePicker;
TextView username;
Dialog selectdateandtime;
String firstname;
Menu menu;
String isAdmin;
Button selectdate,selectTime;
EditText start_time;
DatabaseReference firebaseDatabase;
MenuItem manageCars;
TabHost tabHost;
TabHost.TabSpec spec;
ImageView user_profile_image_drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        user_nav_view = (NavigationView)findViewById(R.id.user_navigation_view);
        selectdateandtime = new Dialog(this);
        selectdateandtime.setContentView(R.layout.custom_datetime_picker);
        tabHost = (TabHost)selectdateandtime.findViewById(R.id.tabHost);
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
        selectdate = (Button)selectdateandtime.findViewById(R.id.select_date_from_datepicker);
        selectTime  = (Button)selectdateandtime.findViewById(R.id.select_time_from_time_picker);
        datePicker  = (DatePicker)selectdateandtime.findViewById(R.id.datepicker);
        timePicker  = (TimePicker)selectdateandtime.findViewById(R.id.timepicker);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        headerView = user_nav_view.inflateHeaderView(R.layout.drawer_header);
        menu = user_nav_view.getMenu();
        username =(TextView) headerView.findViewById(R.id.user_display_name_drawer);
        manageCars = (MenuItem) menu.findItem(R.id.manageCars);
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
        user_profile_image_drawer = (ImageView)headerView.findViewById(R.id.user_profile_image_drawer);
        user_profile_image_drawer.setBackgroundResource(android.R.color.transparent);
        start_time = (EditText)findViewById(R.id.start_time_edtText);
        start_time.setOnClickListener(this);
        selectdate.setOnClickListener(this);
        selectTime.setOnClickListener(this);
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
        String uid = currentUser.getUid();
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstname = dataSnapshot.child("firstname").getValue().toString();
                if(!firstname.isEmpty()){
                    String profileurl= dataSnapshot.child("profileurl").getValue().toString();
                    username.setText("Login as "+firstname);
                    Glide.with(WelcomeUser.this).load(profileurl).apply(new RequestOptions().centerCrop()).into(user_profile_image_drawer);
                }else if(firstname.isEmpty()){
                    username.setText("Log in As User");
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.start_time_edtText:selectdateandtime.show();break;
            case R.id.select_date_from_datepicker:
            case R.id.select_time_from_time_picker:break;
        }
    }
}
