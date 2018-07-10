package com.example.ebuddiess.vehiclerentalsystem.ManageProfile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;


public class ManageProfile extends AppCompatActivity {
Button edit_profile_open_popup;
final String PROFILE_PIC_SECRET_FILE="userprofilepic";
ImageView manage_profile_image_view;
Button from_camera,from_gallery,close_popup;
Dialog image_select_dialog;
ProgressBar progressBar;
Uri image_uri_data,image_download_url_uri;
File fileName,foldername;
File user_profile_image_secret;
TextView progressBarstatus;
RequestOptions requestOptions;
int Camera_code;
String imageName;
DatabaseReference databaseReference; StorageReference storageReference; FirebaseAuth currentUser;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        StrictMode.VmPolicy.Builder builder =new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        edit_profile_open_popup = (Button)findViewById(R.id.manage_profile_edit_image_button);
        image_select_dialog = new Dialog(this);
        currentUser = FirebaseAuth.getInstance();
        progressBarstatus = (TextView)findViewById(R.id.progressbarStatus_manage_profile);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_manage_profile);
        imageName = "img"+System.currentTimeMillis()+".jpeg";
        foldername = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/Pictures" );
        foldername.mkdir();
        fileName = new File(foldername,imageName);
        manage_profile_image_view = (ImageView)findViewById(R.id.manage_profile_imageview);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("Images/"+imageName);
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
                        image_select_dialog.dismiss();
                        uploadFile();
                    }
                });

                image_select_dialog.show();
                from_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFromCamera();
                    }
                });
            }
        });

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
        String path = new UserprofileSharedPrefernces(this).getProfilePath();
        if(path!=""){
            Glide.with(ManageProfile.this).load(path).apply(new RequestOptions().centerCrop()).into(manage_profile_image_view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Camera_code&&resultCode==RESULT_OK && data!=null &&data.getData()!=null) {

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
                String uid = currentUser.getUid();
                databaseReference.child(uid).child("profileurl").setValue(image_download_url_uri.toString());
                try{
                   user_profile_image_secret = File.createTempFile(PROFILE_PIC_SECRET_FILE,"jpg");
                   storageReference.getFile(user_profile_image_secret).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                       UserprofileSharedPrefernces userprofileSharedPrefernces = new UserprofileSharedPrefernces(ManageProfile.this);
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
