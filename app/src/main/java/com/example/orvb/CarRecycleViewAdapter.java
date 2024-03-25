package com.example.orvb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CarRecycleViewAdapter extends RecyclerView.Adapter<CarRecycleViewAdapter.ViewHolder> {
    private List<Car> dataList;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(String plateNumber);
    }

    public CarRecycleViewAdapter(List<Car> dataList) {
        this.dataList = dataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView carPlate, carGearType, carFuelType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carPlate = itemView.findViewById(R.id.carNumberTextView);
            carGearType = itemView.findViewById(R.id.carGearTextView);
            carFuelType = itemView.findViewById(R.id.carFuelTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(Car car) {
            carPlate.setText(car.getPlateNumber());
            carGearType.setText(car.getGearType());
            carFuelType.setText(car.getFuelType());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && clickListener != null) {
                clickListener.onItemClick(dataList.get(position).getPlateNumber());
            }
        }
    }
}
