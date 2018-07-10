package com.example.ebuddiess.vehiclerentalsystem.ManageProfile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ebuddiess.vehiclerentalsystem.R;

public class ManageProfile extends AppCompatActivity {
Button edit_profile_open_popup;
Button from_camera,from_gallery,close_popup;
Dialog image_select_dialog;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_profile);
        edit_profile_open_popup = (Button)findViewById(R.id.manage_profile_edit_image_button);
        image_select_dialog = new Dialog(this);
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
        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.CAMERA},123);
        }
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
}
