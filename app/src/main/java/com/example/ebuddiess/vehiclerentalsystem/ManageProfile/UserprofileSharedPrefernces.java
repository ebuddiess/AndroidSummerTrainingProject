package com.example.ebuddiess.vehiclerentalsystem.ManageProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

public class UserprofileSharedPrefernces {
    Context context;
    String USERUID = "";
    final String PROFILE_PATH = "profilepath";
    SharedPreferences sharedPreferences;

    UserprofileSharedPrefernces(){

    }

    public UserprofileSharedPrefernces(Context context,String uid) {
        this.context = context;
        this.USERUID = uid;
        sharedPreferences = context.getSharedPreferences(USERUID,Context.MODE_PRIVATE);
    }

    void SaveProfilePic(String path){
        sharedPreferences.edit().putString(PROFILE_PATH,path).apply();
    }

    public String getProfilePath(){
         return (sharedPreferences.getString(PROFILE_PATH,""));
    }
}
