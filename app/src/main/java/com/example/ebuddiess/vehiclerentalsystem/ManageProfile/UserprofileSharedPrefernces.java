package com.example.ebuddiess.vehiclerentalsystem.ManageProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

public class UserprofileSharedPrefernces {
    Context context;
    final String IMAGEURL = "profilepic_url";
    final String PROFILE_PATH = "profilepath";
    SharedPreferences sharedPreferences;

    UserprofileSharedPrefernces(){

    }

    public UserprofileSharedPrefernces(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(IMAGEURL,Context.MODE_PRIVATE);
    }

    void SaveProfilePic(String path){
        sharedPreferences.edit().putString(PROFILE_PATH,path).apply();
    }

    public String getProfilePath(){
         return (sharedPreferences.getString(PROFILE_PATH,""));
    }
}
