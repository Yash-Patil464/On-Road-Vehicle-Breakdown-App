package com.example.orvb;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarRecycleViewAdapter {
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton carImage;
        TextView carPlat, carGearType, carFuelType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
