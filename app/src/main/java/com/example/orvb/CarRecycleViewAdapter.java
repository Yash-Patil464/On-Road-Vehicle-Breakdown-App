package com.example.orvb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

public class CarRecycleViewAdapter extends  RecyclerView.Adapter<CarRecycleViewAdapter.ViewHolder> {
    String tag = "CarRecycleViewAdapter";
    List<Car> dataList;

    public CarRecycleViewAdapter(List<Car> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(tag, "onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Car car = dataList.get(position);
        holder.carPlate.setText(car.getPlateNumber());
        holder.carGearType.setText(car.getGearType());
        holder.carFuelType.setText(car.getFuelType());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView carImage;
        TextView carPlate, carGearType, carFuelType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carImage = itemView.findViewById(R.id.carAddImage);
            carPlate = itemView.findViewById(R.id.carNumberTextView);
            carGearType = itemView.findViewById(R.id.carGearTextView);
            carFuelType = itemView.findViewById(R.id.carFuelTextView);

        }
    }
}
