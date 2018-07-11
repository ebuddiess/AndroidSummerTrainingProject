package com.example.ebuddiess.vehiclerentalsystem.ManageProfile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.Authentication.User;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.WelcomeUser.WelcomeUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class ManageProfile extends AppCompatActivity {
Button edit_profile_open_popup;
final String PROFILE_PIC_SECRET_FILE="userprofilepic";
ImageView manage_profile_image_view;
Button from_camera,from_gallery,close_popup;
Dialog image_select_dialog;
ProgressBar progressBar;
EditText firstname_manageproofile,lastName_manageProfile,display_name_manageprofile,mobile_no_manage_profile;
Uri image_uri_data,image_download_url_uri;
File fileName,foldername;
File user_profile_image_secret;
TextView progressBarstatus;
int Camera_code;
String imageName;
String uid;
Button saveButton;
DatabaseReference databaseReference; StorageReference storageReference; FirebaseAuth currentUser;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        StrictMode.VmPolicy.Builder builder =new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        edit_profile_open_popup = (Button)findViewById(R.id.manage_profile_edit_image_button);
        image_select_dialog = new Dialog(this);
        saveButton = (Button)findViewById(R.id.manage_profile_save_btn);
        progressBarstatus = (TextView)findViewById(R.id.progressbarStatus_manage_profile);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_manage_profile);
        imageName = "img"+System.currentTimeMillis()+".jpeg";
        foldername = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/Pictures" );
        foldername.mkdir();
        fileName = new File(foldername,imageName);
        manage_profile_image_view = (ImageView)findViewById(R.id.manage_profile_imageview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("Images/"+imageName);
        firstname_manageproofile = (EditText)findViewById(R.id.manageProfilefirstname);
        lastName_manageProfile = (EditText)findViewById(R.id.manageprofilelast_name);
        display_name_manageprofile=(EditText)findViewById(R.id.manageprofiledisplay_name);
        mobile_no_manage_profile = (EditText)findViewById(R.id.manageprofilemobile_no);
        currentUser = FirebaseAuth.getInstance();
        uid = currentUser.getUid();
        // ------------------------------------------------------------------ //
        edit_profile_open_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_select_dialog.setContentView(R.layout.edit_custom_popup);
                from_camera = (Button) image_select_dialog.findViewById(R.id.select_from_camera_btn);
                from_gallery = (Button) image_select_dialog.findViewById(R.id.select_from_gallery_btn);
                close_popup = (Button) image_select_dialog.findViewById(R.id.close_pop_up);
                close_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(image_uri_data==null){
                            image_select_dialog.dismiss();
                        }else{
                        image_select_dialog.dismiss();
                        uploadFile();
                    }}
                });

                image_select_dialog.show();
                from_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFromCamera();
                    }
                });

                from_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFromGallery();
                    }
                });
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

    }

    private void saveUserInformation() {
    String firstname = firstname_manageproofile.getText().toString();
    String lastname =  lastName_manageProfile.getText().toString();
    String displayname = display_name_manageprofile.getText().toString();
    String mobileno = mobile_no_manage_profile.getText().toString();
    String uid = currentUser.getUid();

        if(!firstname.isEmpty()){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put("firstname",firstname);
        databaseReference.child(uid).updateChildren(taskMap);
        }
        if(!lastname.isEmpty()){
        Map<String,Object> taskMap = new HashMap<String,Object>();
        taskMap.put("lastname",lastname);
        databaseReference.child(uid).updateChildren(taskMap);
       }
        if(!displayname.isEmpty()){
            Map<String,Object> taskMap = new HashMap<String,Object>();
            taskMap.put("displayname",displayname);
            databaseReference.child(uid).updateChildren(taskMap);
        }
        if(!mobileno.isEmpty()){
                Map<String,Object> taskMap = new HashMap<String,Object>();
                taskMap.put("mobileno",mobileno);
                databaseReference.child(uid).updateChildren(taskMap);
            }
        Toast.makeText(this, "Saved Sucesfully", Toast.LENGTH_SHORT).show();
    }

    private void getFromGallery() {
        Toast.makeText(this,"Opening Gallery",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"CHOOSE A PROFILE PIC"),123);
    }

    private void getFromCamera() {
        Camera_code = 1;
        try{
            Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            image_uri_data = Uri.fromFile(fileName);
            capture.putExtra(MediaStore.EXTRA_OUTPUT,image_uri_data);
            startActivityForResult(capture,Camera_code);
        }   catch (Exception e){
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
}

    @Override
    protected void onStart() {
        super.onStart();
        String path = new UserprofileSharedPrefernces(this,currentUser.getUid()).getProfilePath();
        if(path!=""){
            Glide.with(ManageProfile.this).load(path).apply(new RequestOptions().centerCrop()).into(manage_profile_image_view);
        }

           DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
           firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            firstname_manageproofile.setText(dataSnapshot.child("firstname").getValue().toString());
            lastName_manageProfile.setText(dataSnapshot.child("lastname").getValue().toString());
            mobile_no_manage_profile.setText(dataSnapshot.child("mobileno").getValue().toString());
            display_name_manageprofile.setText(dataSnapshot.child("displayname").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Camera_code&&resultCode==RESULT_OK && data!=null &&data.getData()!=null) {

        }else if(requestCode==123&&resultCode==RESULT_OK && data!=null &&data.getData()!=null) {
            image_uri_data = data.getData();
        }
    }

    private void uploadFile() {
    storageReference.putFile(image_uri_data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Toast.makeText(ManageProfile.this,"Uploading is Done",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            progressBarstatus.setVisibility(View.INVISIBLE);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                image_download_url_uri = uri;
                final String uid = currentUser.getUid();
                databaseReference.child(uid).child("profileurl").setValue(image_download_url_uri.toString());
                try{
                   user_profile_image_secret = File.createTempFile(PROFILE_PIC_SECRET_FILE,"jpg");
                   storageReference.getFile(user_profile_image_secret).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                       UserprofileSharedPrefernces userprofileSharedPrefernces = new UserprofileSharedPrefernces(ManageProfile.this,currentUser.getUid());
                       userprofileSharedPrefernces.SaveProfilePic(user_profile_image_secret.getPath());
                       String path = userprofileSharedPrefernces.getProfilePath();
                       Glide.with(ManageProfile.this).load(path).apply(new RequestOptions().centerCrop()).into(manage_profile_image_view);
                       }
                   });
                }catch (Exception e){
                    Toast.makeText(ManageProfile.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(ManageProfile.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            progressBarstatus.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            progressBarstatus.setText(progress + "%");
        }
    });

    }
}
