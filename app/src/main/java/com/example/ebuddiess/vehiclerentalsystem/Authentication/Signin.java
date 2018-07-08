package com.example.ebuddiess.vehiclerentalsystem.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ebuddiess.vehiclerentalsystem.R;

public class Signin extends AppCompatActivity implements View.OnClickListener {
Button login,signup;
TextView forgetPassword;
EditText email_txt,pwd_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        login= (Button)findViewById(R.id.login_btn_from_sign);
        signup=(Button)findViewById(R.id.signup_btn_from_login);
        forgetPassword = (TextView)findViewById(R.id.forgetpassword);
        email_txt = (EditText)findViewById(R.id.signin_email);
        pwd_txt  = (EditText)findViewById(R.id.signin_pwd);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_btn_from_login:startActivity(new Intent(Signin.this,Signup.class)); finish();
        }
    }
}
