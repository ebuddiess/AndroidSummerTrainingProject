package com.example.ebuddiess.vehiclerentalsystem.ManageCars;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ManageCar extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Button select_car_image, save_car_data;
    Spinner fuelType, car_category;
    EditText carname;
    Uri carimageuri,car_image_download_url;
    String imagename;
    String selectedCarCategory;
    FirebaseAuth currentuser;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String fuel_type_txt;
    ImageView car_image;
    int PRICE_PER_DAY;
    int SEATING_CAPACTIY_CAR;
    TextView car_pricing, seating_capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car);
        select_car_image = (Button) findViewById(R.id.select_car_image);
        save_car_data = (Button) findViewById(R.id.save_car_data);
        fuelType = (Spinner) findViewById(R.id.fuel_type);
        car_category = (Spinner) findViewById(R.id.car_category);
        car_image = (ImageView)findViewById(R.id.car_image);
        car_pricing = (TextView) findViewById(R.id.car_seat_price);
        seating_capacity = (TextView) findViewById(R.id.seating_capactiy);
        carname = (EditText) findViewById(R.id.car_name);
        currentuser = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cars");
        car_category.setOnItemSelectedListener(this);
        fuelType.setOnItemSelectedListener(this);
        select_car_image.setOnClickListener(this);
        save_car_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManageCar.this,"Data Saved Sucessfully",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spinner = (Spinner)adapterView;
        switch (spinner.getId()){
            case R.id.car_category:calculatePricing(i);
            case R.id.fuel_type:getFuelType(i);
        }

    }

    private void getFuelType(int i) {
    if(i==1){
        fuel_type_txt = "Petrol";
    }else if(i==2){
        fuel_type_txt = "Diesel";
    }else if(i==3){
        fuel_type_txt = "CNG";
    }
    }

    private void calculatePricing(int i) {
        if(i==1){
            selectedCarCategory = "Hatchback";
            PRICE_PER_DAY = 2000;
            SEATING_CAPACTIY_CAR = 4;
        }else if(i==2){
            selectedCarCategory = "SUV";
            PRICE_PER_DAY = 3000;
            SEATING_CAPACTIY_CAR = 6;
        }else if(i==3){
            selectedCarCategory = "compact";
            PRICE_PER_DAY = 5000;
            SEATING_CAPACTIY_CAR = 7;
        }
        car_pricing.setText(PRICE_PER_DAY+" PRICE PER DAY");
        seating_capacity.setText(SEATING_CAPACTIY_CAR+" PERSON CAPACITY");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Intent intent  = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select a Car Image"),123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123&&resultCode==RESULT_OK && data!=null &&data.getData()!=null) {
            carimageuri = data.getData();
            imagename = "car"+carname.getText().toString()+System.currentTimeMillis()+ ".jpeg";
            storageReference = FirebaseStorage.getInstance().getReference("CarImages/"+imagename);
            Toast.makeText(ManageCar.this,"Uploading Please wait ......",Toast.LENGTH_LONG).show();
            storageReference.putFile(carimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                      car_image_download_url = uri;
                      String carname_txt = carname.getText().toString();
                      String carid = databaseReference.push().getKey();
                      Car newcar = new Car(carid,carname_txt,car_image_download_url.toString(),selectedCarCategory,fuel_type_txt,SEATING_CAPACTIY_CAR,currentuser.getUid(),PRICE_PER_DAY);
                      databaseReference.child(carid).setValue(newcar);
                      Glide.with(ManageCar.this).load(car_image_download_url).apply(new RequestOptions().centerCrop()).into(car_image);
                      }
                  });
                }
            });
        }
    }
}