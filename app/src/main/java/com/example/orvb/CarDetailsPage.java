package com.example.orvb;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CarDetailsPage extends Fragment {

    int gearType_val, fuelType_val;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car_details_page, container, false);
        String phoneNumber = UserManager.getInstance().getPhoneNumber();
        DatabaseReference db_reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://orvb-sem-proj-default-rtdb.firebaseio.com/");

        ImageButton backButton = rootView.findViewById(R.id.carDetails_BackButton);

        AppCompatEditText et_car_plateNum = rootView.findViewById(R.id.carDetails_PlateNum);

        AppCompatRadioButton rb_auto = rootView.findViewById(R.id.radio_auto);
        AppCompatRadioButton rb_man = rootView.findViewById(R.id.radio_manual);

        AppCompatRadioButton rb_petrol = rootView.findViewById(R.id.radio_petrol);
        AppCompatRadioButton rb_cng = rootView.findViewById(R.id.radio_cng);
        AppCompatRadioButton rb_diesel = rootView.findViewById(R.id.radio_diesel);

        Button submit_button = rootView.findViewById(R.id.submit_car_details);

        if (rb_auto.isChecked()){
            gearType_val = 1;
        }
        if (rb_petrol.isChecked()) {
            fuelType_val = 1;
        }

        //GEAR TYPES
        rb_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_auto.setTextColor(Color.WHITE);
                rb_man.setTextColor(Color.BLACK);
                gearType_val = 1;
            }
        });

        rb_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_man.setTextColor(Color.WHITE);
                rb_auto.setTextColor(Color.BLACK);
                gearType_val = 2;
            }
        });

        // FUEL TYPES
        rb_petrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_petrol.setTextColor(Color.WHITE);
                rb_cng.setTextColor(Color.BLACK);
                rb_diesel.setTextColor(Color.BLACK);
                fuelType_val = 1;
            }
        });

        rb_cng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_petrol.setTextColor(Color.BLACK);
                rb_cng.setTextColor(Color.WHITE);
                rb_diesel.setTextColor(Color.BLACK);
                fuelType_val = 2;
            }
        });

        rb_diesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_petrol.setTextColor(Color.BLACK);
                rb_cng.setTextColor(Color.BLACK);
                rb_diesel.setTextColor(Color.WHITE);
                fuelType_val = 3;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carPlateNum = Objects.requireNonNull(et_car_plateNum.getText()).toString();

                if (carPlateNum.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter the Plate No.", Toast.LENGTH_SHORT).show();
                } else {
                    db_reference.child("Userinfo").child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("CarDetails").hasChild(carPlateNum)){
                                Toast.makeText(requireContext(),"This Plate No. has Already been added", Toast.LENGTH_SHORT).show();
                            } else {
                                db_reference.child("Userinfo").child(phoneNumber).child("CarDetails").child(carPlateNum).child("GearType").setValue(gearType_val);
                                db_reference.child("Userinfo").child(phoneNumber).child("CarDetails").child(carPlateNum).child("FuelType").setValue(fuelType_val);

                                Toast.makeText(requireContext(),"Successfully added", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().popBackStack();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}