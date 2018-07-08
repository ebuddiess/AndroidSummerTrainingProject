package com.example.ebuddiess.vehiclerentalsystem.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.WelcomeUser.WelcomeUser;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Button go_back,signup;
    EditText email_txt,pwd_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        go_back= (Button)findViewById(R.id.go_bck_btn);
        signup=(Button)findViewById(R.id.signup_btn_from_signup);
        email_txt = (EditText)findViewById(R.id.signin_email);
        pwd_txt  = (EditText)findViewById(R.id.signin_pwd);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this,Signin.class));
                finish();
            }
        });
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
