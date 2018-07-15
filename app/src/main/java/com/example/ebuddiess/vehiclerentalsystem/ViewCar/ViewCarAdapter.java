package com.example.ebuddiess.vehiclerentalsystem.ViewCar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.ManageCars.Car;
import com.example.ebuddiess.vehiclerentalsystem.Orders.MyOrderDetailsDatabase;
import com.example.ebuddiess.vehiclerentalsystem.Orders.Orders;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewCarAdapter extends RecyclerView.Adapter<ViewCarAdapter.MyviewCarHolder> {
    List<Car> carList;
    List<String> delhicity ;
    String startdate,starttime,enddate,endtime;
    List<String> mumbaicity ;
    Context context;
    String city;
    String doorpickingstatus;
    int totalprice;
    int doorpickprice;
    RecyclerView recyclerView;
    int noofdays;
    int price;
    DatabaseReference orderDatabase;
    FirebaseAuth currentUser;
    HashMap<String,Integer> cityandprice;
    ArrayAdapter <String>spinnerAdapter;
    public ViewCarAdapter(List<Car> carList, Context context, String city, int noofdays, String startdate, String starttime, String enddate, String endtime,String doorpickingstatus) {
        this.carList = carList;
        this.context = context;
        this.city = city;
        this.startdate = startdate;
        this.starttime = starttime;
        this.enddate  = enddate;
        this.endtime = endtime;
        this.noofdays = noofdays;
        this.doorpickingstatus = doorpickingstatus;
        delhicity = new ArrayList<String>();
        mumbaicity = new ArrayList<String>();
        delhicity.add("SELECT CITY");
        delhicity.add("Kalkaji");
        delhicity.add("Badarpur");
        mumbaicity.add("SELECT CITY");
        mumbaicity.add("Bandra");
        cityandprice = new HashMap<String,Integer>();
        orderDatabase = FirebaseDatabase.getInstance().getReference("Orders");
        currentUser = FirebaseAuth.getInstance();
    }

    void setDoorpickingStatus(String doorpickingstatus){
        this.doorpickingstatus = doorpickingstatus;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        if(!recyclerView.isComputingLayout()){
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyviewCarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_car_layouy,parent,false);
        return  new MyviewCarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewCarHolder holder, final int position) {
     holder.carname.setText(carList.get(position).getCarName());
     holder.car_pricing.setText(carList.get(position).getPricing()+"");
     holder.car_fueltype.setText(carList.get(position).getFuelType()+"");
     holder.car_category.setText(carList.get(position).getCar_category());
     holder.seating_capacity.setText(carList.get(position).getSeating_capcaity()+"");
     Glide.with(context).load(carList.get(position).getCar_image_url()).apply(new RequestOptions().centerCrop()).into(holder.car_image);
//     ---------------------------------------------------------------------------
        holder.choosesitelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//      Toast.makeText(context,city,Toast.LENGTH_LONG).show();
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             String city = adapterView.getItemAtPosition(i).toString();
             if(!city.equals("SELECT CITY")){
                 if(city.equals("Kalkaji")){
                     cityandprice.put("Kalkaji",100);
                     price = 100;
                 }else if(city.equals("Badarpur")){
                     cityandprice.put("Badarpur",200);
                     price = 200;
                 }else if(city.equals("Bandra")){
                     cityandprice.put("Bandra",400);
                     price = 400;
                 }
             }
         }
         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {
         }
     });

     holder.book_car.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Boolean cityselected=false;
             int holderposition = holder.getAdapterPosition();
             if(!(holderposition ==RecyclerView.NO_POSITION)){
             if(doorpickingstatus.equals("true")){
                 doorpickprice = 500;
                 totalprice = (carList.get(position).getPricing()*noofdays)+doorpickprice;
             }else{
                 doorpickprice = 0;
                 String selectedcity = holder.choosesitelocation.getSelectedItem().toString();
                 if(!selectedcity.equals("SELECT CITY")){
                     cityselected = true;
                     totalprice = (carList.get(position).getPricing()*noofdays)+cityandprice.get(selectedcity);
                 }else{
                     cityselected = false;
                     Toast.makeText(context,"Select City First",Toast.LENGTH_LONG).show();}}

              if(cityselected==true || doorpickprice>0){
                final String orderid = orderDatabase.push().getKey();
                 MyOrderDetailsDatabase myorders = new MyOrderDetailsDatabase(orderid,carList.get(position).getCarid(),carList.get(position).getCarName(),carList.get(position).getCar_category(),currentUser.getCurrentUser().getUid(),startdate,starttime,enddate,endtime,noofdays,totalprice,carList.get(position).getPricing(),cityandprice.get(holder.choosesitelocation.getSelectedItem().toString()),doorpickprice,holder.choosesitelocation.getSelectedItem().toString());
                 orderDatabase.child(orderid).setValue(myorders).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                           Toast.makeText(context,"Booking Done ! Showing Details",Toast.LENGTH_LONG).show();
                             context.startActivity(new Intent(context,Orders.class).putExtra("orderid",orderid));
                         }
                     }
                 });
              }
              }
              }
              });

     if(doorpickingstatus.equals("true")){
         holder.choosesitelocation.setVisibility(View.INVISIBLE);
     }else{
         holder.choosesitelocation.setVisibility(View.VISIBLE);
     }
    }
///------------------------------/------------------------
    @Override
    public int getItemCount() {
        return carList.size();
    }

    class MyviewCarHolder extends  RecyclerView.ViewHolder {
        TextView carname,car_category,car_fueltype,car_pricing,seating_capacity;
        ImageView car_image;
        Button book_car;
        Spinner choosesitelocation;

        public MyviewCarHolder(View itemView) {
            super(itemView);
            book_car = (Button)itemView.findViewById(R.id.viewcar_booknow_btn);
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
