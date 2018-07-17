package com.example.ebuddiess.vehiclerentalsystem.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.IntroSlider.MainActivity;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.WelcomeUser.WelcomeUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_LONG;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Button go_back,signup;
    User newuser;
    EditText email_txt,pwd_txt,secretcode,mobile_no;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        go_back= (Button)findViewById(R.id.go_bck_btn);
        secretcode = (EditText)findViewById(R.id.secret_code);
        mobile_no = (EditText)findViewById(R.id.signup_mobileno);
        signup=(Button)findViewById(R.id.signup_btn_from_signup);
        email_txt = (EditText)findViewById(R.id.signup_email);
        pwd_txt  = (EditText)findViewById(R.id.signup_pwd);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();
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
        String emailAdresstxt = email_txt.getText().toString();
        String passwordtxt = pwd_txt.getText().toString();
        String phoneno_txt = mobile_no.getText().toString();
        String secretcode_txt = secretcode.getText().toString();
        int pwdlength = passwordtxt.length();
        String isAdmin = "false";
        /* checking if values are empty or not */
         if(secretcode_txt.equals("123")){
            isAdmin = "true";
         }

         if(emailAdresstxt.isEmpty()){
                email_txt.setError("Email Adress is Empty");
            }else if(passwordtxt.isEmpty()){
                pwd_txt.setError("Please Fill Password");
            }else if(phoneno_txt.isEmpty()){
                mobile_no.setError("Enter Mobile No");
            } else if(phoneno_txt.length()!=10){
            mobile_no.setError("Invalid Mobile Number");
            }else if(pwdlength<=6){
                pwd_txt.setError("Password Must be 7 chracter Long");
            } else {
                createNewUser(emailAdresstxt,passwordtxt,phoneno_txt,isAdmin);
            }


    }

    private void createNewUser(String emailAdresstxt, String passwordtxt, final String phoneno_text, final String isAdmin_txt) {
      firebaseAuth.createUserWithEmailAndPassword(emailAdresstxt,passwordtxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String userid = user.getUid();
                newuser = new User(phoneno_text,user.getEmail(),userid,isAdmin_txt);
                databaseReference.child(userid).setValue(newuser);
                Toast.makeText(Signup.this,"Registered Sucesfully",LENGTH_LONG).show();
                finish();
                startActivity(new Intent(Signup.this,Signin.class).putExtra("isAdmin",isAdmin_txt));
            }else{
                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(Signup.this,"Already Registered", LENGTH_LONG).show();
                }else {
                    Toast.makeText(Signup.this,task.getException().getMessage().toString(), LENGTH_LONG).show();
                }
            }
          }
      });
    }
}
