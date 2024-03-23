package com.example.orvb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyCarsFragment extends Fragment {
    List dataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_cars, container, false);
        String phoneNumber = UserManager.getInstance().getPhoneNumber();
        DatabaseReference db_reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://orvb-sem-proj-default-rtdb.firebaseio.com/");

        TextView show_name = rootView.findViewById(R.id.show_name);
        Button add_car_button = rootView.findViewById(R.id.add_car_button);
        ImageButton edit_CarInfo = rootView.findViewById(R.id.btn_EditCarInfo);
        ImageButton delete_CarInfo = rootView.findViewById(R.id.btn_DeleteCarInfo);

        CarRecycleViewAdapter adapter = new CarRecycleViewAdapter(dataList);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycleView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        add_car_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new CarDetailsPage())
                        .addToBackStack(null)
                        .commit();
            }
        });

        db_reference.child("Userinfo").child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();

                String getName = snapshot.child("Name").getValue(String.class);
                getName = extractFirstName(getName);
                getName = capitalizeFirstLetter(getName);
                show_name.setText(String.format("Hi, %s", getName));

                if (snapshot.exists()) {
                    for (DataSnapshot carSnapshot : snapshot.child("CarDetails").getChildren()) {
                        String plateNumber = carSnapshot.getKey();
                        int gearType = carSnapshot.child("GearType").getValue(Integer.class);
                        int fuelType = carSnapshot.child("FuelType").getValue(Integer.class);

                        String Geartype_str;
                        if (gearType == 1) {
                            Geartype_str = "Automatic";
                        } else {
                            Geartype_str = "Manual";
                        }

                        String Fueltype_str;
                        if (fuelType == 1) {
                            Fueltype_str = "Petrol";
                        } else if (fuelType == 2) {
                            Fueltype_str = "CNG";
                        } else {
                            Fueltype_str = "Diesel";
                        }

                        Car car = new Car(plateNumber, Geartype_str, Fueltype_str);

                        dataList.add(car);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle the case where the user has no car details
                    Log.d("CarDetails", "No car details found for this user.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edit_CarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new EditCarInfo())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }

    public static String extractFirstName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return fullName;
        }
        String[] parts = fullName.trim().split("\\s+");
        return parts[0];
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}