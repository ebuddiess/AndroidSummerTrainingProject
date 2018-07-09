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
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.Authentication.Signin;
import com.example.ebuddiess.vehiclerentalsystem.ManageProfile.ManageProfile;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
NavigationView user_nav_view;
FirebaseAuth currentUser;
CardView manageProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);
        user_nav_view = (NavigationView)findViewById(R.id.user_navigation_view);
        currentUser = FirebaseAuth.getInstance();
        manageProfile = (CardView)findViewById(R.id.manager_profile_cardView);
        manageProfile.setOnClickListener(this);
        user_nav_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:doLogout();
        }
        return true;
    }

    private void doLogout() {
        Toast.makeText(WelcomeUser.this,"Logout Sucessfully",Toast.LENGTH_LONG).show();
        currentUser.signOut();
        finish();
        startActivity(new Intent(WelcomeUser.this, Signin.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.manager_profile_cardView:startActivity(new Intent(WelcomeUser.this,ManageProfile.class));
        }
    }
}
