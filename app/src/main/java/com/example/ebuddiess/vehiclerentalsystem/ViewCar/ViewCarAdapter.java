package com.example.ebuddiess.vehiclerentalsystem.ViewCar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.ManageCars.Car;
import com.example.ebuddiess.vehiclerentalsystem.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCarAdapter extends RecyclerView.Adapter<ViewCarAdapter.MyviewCarHolder> {
    List<Car> carList;
    List<String> delhicity ;
    List<String> mumbaicity ;
    Context context;
    String city;
    int noofdays;
    ArrayAdapter <String>spinnerAdapter;
    public ViewCarAdapter(List<Car> carList, Context context, String city, int noofdays) {
        this.carList = carList;
        this.context = context;
        this.city = city;
        this.noofdays = noofdays;
        delhicity = new ArrayList<String>();
        mumbaicity = new ArrayList<String>();
        delhicity.add("Old Delhi");
        delhicity.add("Badarpur");
        delhicity.add("Vasantkunj");
        delhicity.add("Canaught Place");
        mumbaicity.add("Bandra");
        mumbaicity.add("Borivali");
        mumbaicity.add("Nalasupara");
    }

    @NonNull
    @Override
    public MyviewCarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_car_layouy,parent,false);
        return  new MyviewCarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewCarHolder holder, int position) {
     holder.carname.setText(carList.get(position).getCarName());
     holder.car_pricing.setText(carList.get(position).getPricing()+"");
     holder.car_fueltype.setText(carList.get(position).getFuelType()+"");
     holder.car_category.setText(carList.get(position).getCar_category());
     holder.seating_capacity.setText(carList.get(position).getSeating_capcaity()+"");
     Glide.with(context).load(carList.get(position).getCar_image_url()).apply(new RequestOptions().centerCrop()).into(holder.car_image);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    class MyviewCarHolder extends  RecyclerView.ViewHolder {
        TextView carname,car_category,car_fueltype,car_pricing,seating_capacity;
        ImageView car_image;
        Spinner choosesitelocation;
        public MyviewCarHolder(View itemView) {
            super(itemView);
            carname =      (TextView)itemView.findViewById(R.id.viewcarcarname_txtview);
            car_category = (TextView)itemView.findViewById(R.id.view_car_category);
            car_fueltype = (TextView)itemView.findViewById(R.id.viewcar_fuel_type);
            car_pricing  = (TextView)itemView.findViewById(R.id.price_per_day);
            seating_capacity = (TextView)itemView.findViewById(R.id.viewcar_seating_capactiy);
            car_image = (ImageView)itemView.findViewById(R.id.car_thumbnail_image_view);
            choosesitelocation = (Spinner)itemView.findViewById(R.id.choose_site_location);
            if(city.equals("Delhi")){
                spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,delhicity);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }else if(city.equals("Mumbai")){
                spinnerAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,mumbaicity);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             }
            choosesitelocation.setAdapter(spinnerAdapter);
        }

    }
}
