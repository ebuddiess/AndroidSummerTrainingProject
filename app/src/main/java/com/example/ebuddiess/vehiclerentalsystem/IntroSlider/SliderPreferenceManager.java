package com.example.ebuddiess.vehiclerentalsystem.IntroSlider;

import android.content.Context;
import android.content.SharedPreferences;

public class SliderPreferenceManager {
    Context context;
    SharedPreferences sharedPreferences;
    SliderPreferenceManager(Context context){
        this.context  = context;
        getSharedPreference();
    }
    private  void getSharedPreference(){
        sharedPreferences = context.getSharedPreferences("Slider",context.MODE_PRIVATE);
    }
    public void WritePrefrence(){
        sharedPreferences.edit().putString("sliderpassed","ok").commit();
    }
    public boolean checkPrefrence(){
        boolean status =false;
        if(sharedPreferences.getString("Slider",null).equals(null)){
            status = false;
        }else{
            status = true;
        }
        return status;
    }

    void clearPreference (){
        sharedPreferences.edit().clear().commit();
    }
}
