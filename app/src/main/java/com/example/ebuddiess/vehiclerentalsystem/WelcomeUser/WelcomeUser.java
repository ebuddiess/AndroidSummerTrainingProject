package com.example.ebuddiess.vehiclerentalsystem.WelcomeUser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.Authentication.Signin;
import com.example.ebuddiess.vehiclerentalsystem.ManageProfile.ManageProfile;
import com.example.ebuddiess.vehiclerentalsystem.ManageProfile.UserprofileSharedPrefernces;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
NavigationView user_nav_view;
FirebaseAuth currentUser;
ImageView user_profile_image_drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        user_nav_view = (NavigationView)findViewById(R.id.user_navigation_view);
        currentUser = FirebaseAuth.getInstance();
        user_nav_view.setNavigationItemSelectedListener(this);
        user_profile_image_drawer = (ImageView)findViewById(R.id.user_profile_image_drawer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String path = new UserprofileSharedPrefernces(this).getProfilePath();
        if(path!=""){
            Glide.with(WelcomeUser.this).load(R.drawable.active_dots).apply(new RequestOptions().centerCrop()).into(user_profile_image_drawer);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:doLogout();
            case R.id.user_EditProfile:openmanageProfile();
        }
        return true;
    }

    private void openmanageProfile() {
    startActivity(new Intent(WelcomeUser.this,ManageProfile.class));
    }

    private void doLogout() {
        Toast.makeText(WelcomeUser.this,"Logout Sucessfully",Toast.LENGTH_LONG).show();
        currentUser.signOut();
        finish();
        startActivity(new Intent(WelcomeUser.this, Signin.class));
    }


}
