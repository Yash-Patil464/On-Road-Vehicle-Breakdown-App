package com.example.orvb;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarDetailsPage extends Fragment {

    int gearType_val = 1, fuelType_val = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car_details_page, container, false);
        String phoneNumber = UserManager.getInstance().getPhoneNumber();
        DatabaseReference db_reference = FireBaseManager.getInstance().getDatabaseReference();

        ImageButton backButton = rootView.findViewById(R.id.carDetails_BackButton);
        AppCompatEditText et_car_plateNum = rootView.findViewById(R.id.carDetails_PlateNum);
        AppCompatRadioButton rb_auto = rootView.findViewById(R.id.radio_auto);
        AppCompatRadioButton rb_man = rootView.findViewById(R.id.radio_manual);
        AppCompatRadioButton rb_petrol = rootView.findViewById(R.id.radio_petrol);
        AppCompatRadioButton rb_cng = rootView.findViewById(R.id.radio_cng);
        AppCompatRadioButton rb_diesel = rootView.findViewById(R.id.radio_diesel);
        Button submit_button = rootView.findViewById(R.id.submit_car_details);

        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        rb_auto.setOnClickListener(v -> setGearType(rb_auto, rb_man, 1));
        rb_man.setOnClickListener(v -> setGearType(rb_man, rb_auto, 2));
        rb_petrol.setOnClickListener(v -> setFuelType(rb_petrol, rb_cng, rb_diesel, 1));
        rb_cng.setOnClickListener(v -> setFuelType(rb_cng, rb_petrol, rb_diesel, 2));
        rb_diesel.setOnClickListener(v -> setFuelType(rb_diesel, rb_petrol, rb_cng, 3));

        submit_button.setOnClickListener(v -> {
            String carPlateNum = Objects.requireNonNull(et_car_plateNum.getText()).toString();
            if (carPlateNum.isEmpty()) {
                Toast.makeText(requireContext(), "Enter the Plate No.", Toast.LENGTH_SHORT).show();
            } else if (!isValidPlateNumber(carPlateNum)) {
                Toast.makeText(requireContext(), "Enter a valid Car Plate Number", Toast.LENGTH_SHORT).show();
            } else {
                db_reference.child("Userinfo").child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isAdded()) { // Check if fragment is attached
                            if (snapshot.child("CarDetails").hasChild(carPlateNum)) {
                                Toast.makeText(requireContext(), "This Plate No. has Already been added", Toast.LENGTH_SHORT).show();
                            } else {
                                db_reference.child("Userinfo").child(phoneNumber).child("CarDetails").child(carPlateNum).child("GearType").setValue(gearType_val);
                                db_reference.child("Userinfo").child(phoneNumber).child("CarDetails").child(carPlateNum).child("FuelType").setValue(fuelType_val);
                                Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().popBackStack();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }
        });

        return rootView;
    }

    private void setGearType(AppCompatRadioButton selected, AppCompatRadioButton unselected, int value) {
        selected.setTextColor(Color.WHITE);
        unselected.setTextColor(Color.BLACK);
        gearType_val = value;
    }

    private void setFuelType(AppCompatRadioButton selected, AppCompatRadioButton unselected1, AppCompatRadioButton unselected2, int value) {
        selected.setTextColor(Color.WHITE);
        unselected1.setTextColor(Color.BLACK);
        unselected2.setTextColor(Color.BLACK);
        fuelType_val = value;
    }

    private boolean isValidPlateNumber(String plateNumber) {
        // Regular expression to match Indian car plate numbers (format: AA00AA0000 or AA0000AA00)
        String regex = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$|^[A-Z]{2}[0-9]{4}[A-Z]{2}[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(plateNumber);
        return matcher.matches();
    }
}
