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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditCarInfo extends Fragment {
    DatabaseReference db_reference;
    String phoneNumber, platenum;
    int gearType_val, fuelType_val;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_car_info, container, false);
        phoneNumber = UserManager.getInstance().getPhoneNumber();
        db_reference = FireBaseManager.getInstance().getDatabaseReference();

        ImageButton backButton = rootView.findViewById(R.id.edit_backBtn);
        AppCompatEditText et_car_plateNum = rootView.findViewById(R.id.edit_carDetails_PlateNum);
        AppCompatRadioButton rb_auto = rootView.findViewById(R.id.edit_radio_auto);
        AppCompatRadioButton rb_man = rootView.findViewById(R.id.edit_radio_manual);
        AppCompatRadioButton rb_petrol = rootView.findViewById(R.id.edit_radio_petrol);
        AppCompatRadioButton rb_cng = rootView.findViewById(R.id.edit_radio_cng);
        AppCompatRadioButton rb_diesel = rootView.findViewById(R.id.edit_radio_diesel);
        Button submit_button = rootView.findViewById(R.id.edit_submit_car_details);
        ImageButton delete_button = rootView.findViewById(R.id.btn_DeleteCarInfo);

        Bundle args = getArguments();
        if (args != null) {
            platenum = args.getString("plateNumber");
            et_car_plateNum.setText(platenum);
        }

        DatabaseReference carRef = db_reference.child("Userinfo").child(phoneNumber).child("CarDetails").child(platenum);
        carRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get car details
                    int gearType = snapshot.child("GearType").getValue(Integer.class);
                    int fuelType = snapshot.child("FuelType").getValue(Integer.class);

                    // Set plate number EditText
                    et_car_plateNum.setText(platenum);

                    // Check appropriate radio buttons based on gear type
                    if (gearType == 1) {
                        setGearType(rb_auto, rb_man, 1);
                    } else {
                        setGearType(rb_man, rb_auto, 2);
                    }

                    // Check appropriate radio buttons based on fuel type
                    switch (fuelType) {
                        case 1:
                            setFuelType(rb_petrol, rb_cng, rb_diesel, 1);
                            break;
                        case 2:
                            setFuelType(rb_cng, rb_petrol, rb_diesel, 2);
                            break;
                        case 3:
                            setFuelType(rb_diesel, rb_petrol, rb_cng, 3);
                            break;
                    }
                } else {
                    // Handle case where car details not found
                    Toast.makeText(requireContext(), "Car details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        rb_auto.setOnClickListener(v -> setGearType(rb_auto, rb_man, 1));
        rb_man.setOnClickListener(v -> setGearType(rb_man, rb_auto, 2));
        rb_petrol.setOnClickListener(v -> setFuelType(rb_petrol, rb_cng, rb_diesel, 1));
        rb_cng.setOnClickListener(v -> setFuelType(rb_cng, rb_petrol, rb_diesel, 2));
        rb_diesel.setOnClickListener(v -> setFuelType(rb_diesel, rb_petrol, rb_cng, 3));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference carRef = db_reference.child("Userinfo").child(phoneNumber).child("CarDetails");
                carRef.child(platenum).removeValue();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPlateNumber = platenum;
                String newPlateNumber = et_car_plateNum.getText().toString();

                if (newPlateNumber.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter the Plate No.", Toast.LENGTH_SHORT).show();
                } else if (!isValidPlateNumber(newPlateNumber)) {
                    Toast.makeText(requireContext(), "Enter a valid Car Plate Number", Toast.LENGTH_SHORT).show();
                } else {
                    updateCarDetails(oldPlateNumber, newPlateNumber, gearType_val, fuelType_val);
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        return rootView;
    }

    private void setGearType(AppCompatRadioButton selected, AppCompatRadioButton unselected, int value) {
        selected.setTextColor(Color.BLACK); // Set selected radio button text color to black
        selected.setBackgroundColor(Color.WHITE); // Set selected radio button background color to white for better visibility
        unselected.setTextColor(Color.GRAY); // Set unselected radio button text color to gray
        unselected.setBackgroundColor(Color.TRANSPARENT); // Set unselected radio button background color to transparent
        gearType_val = value;
    }

    private void setFuelType(AppCompatRadioButton selected, AppCompatRadioButton unselected1, AppCompatRadioButton unselected2, int value) {
        selected.setTextColor(Color.BLACK); // Set selected radio button text color to black
        selected.setBackgroundColor(Color.WHITE); // Set selected radio button background color to white for better visibility
        unselected1.setTextColor(Color.GRAY); // Set unselected radio button text color to gray
        unselected1.setBackgroundColor(Color.TRANSPARENT); // Set unselected radio button background color to transparent
        unselected2.setTextColor(Color.GRAY); // Set unselected radio button text color to gray
        unselected2.setBackgroundColor(Color.TRANSPARENT); // Set unselected radio button background color to transparent
        fuelType_val = value;
    }


    private void updateCarDetails(String oldPlateNumber, String newPlateNumber, int gearType, int fuelType) {
        // Update car details in Firebase
        DatabaseReference carRef = db_reference.child("Userinfo").child(phoneNumber).child("CarDetails");
        carRef.child(oldPlateNumber).removeValue(); // Remove old entry
        carRef.child(newPlateNumber).child("GearType").setValue(gearType);
        carRef.child(newPlateNumber).child("FuelType").setValue(fuelType);
    }

    private boolean isValidPlateNumber(String plateNumber) {
        // Regular expression to match Indian car plate numbers (format: AA00AA0000 or AA0000AA00)
        String regex = "^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$|^[A-Z]{2}[0-9]{4}[A-Z]{2}[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(plateNumber);
        return matcher.matches();
    }
}