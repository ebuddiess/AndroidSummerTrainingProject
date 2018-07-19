package com.example.ebuddiess.vehiclerentalsystem.MyTrips;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebuddiess.vehiclerentalsystem.Orders.MyOrderDetailsDatabase;
import com.example.ebuddiess.vehiclerentalsystem.Orders.Orders;
import com.example.ebuddiess.vehiclerentalsystem.R;
import com.example.ebuddiess.vehiclerentalsystem.ViewCar.ViewCarAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class MyTripsAdapter extends RecyclerView.Adapter<MyTripsAdapter.MyTripHolder> {
    List<MyOrderDetailsDatabase> myorderlist;
    Context context;
    DatabaseReference currentUserRefrence;
    public MyTripsAdapter(List<MyOrderDetailsDatabase> myorderlist, Context context) {
        this.myorderlist = myorderlist;
        this.context = context;
        currentUserRefrence = FirebaseDatabase.getInstance().getReference("Users");
    }

    @NonNull
    @Override
    public MyTripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mytrips_layout,parent,false);
        return  new MyTripHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyTripHolder holder, final int position) {
        holder.carname.setText(myorderlist.get(position).getCarname());
        holder.startdate.setText(myorderlist.get(position).getStartdate());
        holder.enddate.setText(myorderlist.get(position).getEnddate());
        holder.carcity.setText(myorderlist.get(position).getCarcity());
        Glide.with(context).load(myorderlist.get(position).getCarurl()).apply(new RequestOptions().centerCrop()).into(holder.mytripcarimageview);
        holder.viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,TripDetails.class).putExtra("orderid",myorderlist.get(position).getOrderid()).putExtra("carimage",myorderlist.get(position).getCarurl().toString()));
            }
        });
        currentUserRefrence.child(myorderlist.get(position).getUserid()).child("firstname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.bookedby.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return myorderlist.size();
    }

    public class MyTripHolder extends  RecyclerView.ViewHolder {
        TextView carname,startdate,enddate,carcity,bookedby;
        ImageView mytripcarimageview;
        Button viewmore;
        public MyTripHolder(View itemView) {
            super(itemView);
            bookedby = (TextView)itemView.findViewById(R.id.mytripbookedbytxtview);
            mytripcarimageview = (ImageView)itemView.findViewById(R.id.mytrip_image_view);
            carcity = (TextView)itemView.findViewById(R.id.mytripcarcity_txtview);
            carname = (TextView)itemView.findViewById(R.id.mytripcarname_txtview);
            startdate = (TextView)itemView.findViewById(R.id.mytripstartdate_txtview);
            enddate = (TextView)itemView.findViewById(R.id.mytripenddate_txtview);
            viewmore = (Button)itemView.findViewById(R.id.mytrip_showmore);
        }
    }
}
