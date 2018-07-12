package com.example.ebuddiess.vehiclerentalsystem.Authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.WelcomeUser.WelcomeUser;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class Signin extends AppCompatActivity implements View.OnClickListener {
Button login,signup;
TextView forgetPassword;
FirebaseAuth Authentication;
EditText email_txt,pwd_txt;
ProgressBar progressBar;
DatabaseReference databaseReference;
FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        login= (Button)findViewById(R.id.login_btn_from_sign);
        signup=(Button)findViewById(R.id.signup_btn_from_login);
        Authentication = FirebaseAuth.getInstance();
        forgetPassword = (TextView)findViewById(R.id.forgetpassword);
        email_txt = (EditText)findViewById(R.id.signin_email);
        pwd_txt  = (EditText)findViewById(R.id.signin_pwd);
        currentUser = FirebaseAuth.getInstance();
        signup.setOnClickListener(this);
        login.setOnClickListener(this);

        if(currentUser.getCurrentUser()!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String isAdmin =  dataSnapshot.child("adminPower").getValue().toString();
                    if(isAdmin==null){
                        String isAdminfromIntent = getIntent().getStringExtra("isAdmin");
                        Map<String,Object> taskMap = new HashMap<String,Object>();
                        taskMap.put("adminPower",isAdminfromIntent);
                        databaseReference.updateChildren(taskMap);
                        Toast.makeText(Signin.this,"Admin Created", LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(Signin.this,WelcomeUser.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_btn_from_login:startActivity(new Intent(Signin.this,Signup.class)); finish(); break;
            case R.id.login_btn_from_sign:loginUser();
        }
    }

    private void loginUser() {
      String logintxt= email_txt.getText().toString();
      String password_txt = pwd_txt.getText().toString();
             if(logintxt.isEmpty()){
                email_txt.setError("Empty Email Adresss");
            }else if(password_txt.isEmpty()){
                pwd_txt.setError("Empty Password");
            }else{
                 progressBar.setVisibility(View.VISIBLE);
                 Authentication.signInWithEmailAndPassword(logintxt,password_txt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            Intent i1 = new Intent(Signin.this, WelcomeUser.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i1);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Signin.this,"Login Sucessfully", LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Signin.this,task.getException().getMessage().toString(), LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                     }
                 });
             }

    }
}
