package com.example.orvb;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment implements CustomSpinner.OnSpinnerEventsListener {
    private CustomSpinner platenum_spinner;
    private List<String> plateNumbers;
    private ArrayAdapter<String> adapter;
    String phoneNumber = UserManager.getInstance().getPhoneNumber();
    private DatabaseReference db_reference = FireBaseManager.getInstance().getDatabaseReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_services, container, false);

        RadioButton rb_instantrepair = rootView.findViewById(R.id.rb_InstantRepair);
        RadioButton rb_outoffuel = rootView.findViewById(R.id.rb_OutofFuel);
        RadioButton rb_tow = rootView.findViewById(R.id.rb_tow);

        platenum_spinner = rootView.findViewById(R.id.platenum_spinner);
        plateNumbers = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), R.layout.custom_spinner_dropdown_item, plateNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        platenum_spinner.setAdapter(adapter);

        platenum_spinner.setSpinnerEventsListener(this);
        platenum_spinner.setDropDownVerticalOffset(100);

        Drawable icon_instantrepair = getResources().getDrawable(R.drawable.icon_carrepair);
        Drawable icon_outoffuel = getResources().getDrawable(R.drawable.icon_gas);
        Drawable icon_tow = getResources().getDrawable(R.drawable.icon_towtruck);

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked }, // checked
                new int[] { -android.R.attr.state_checked } // unchecked
        };

        int[] colors = new int[] {
                Color.parseColor("#FFAF45"), // color when checked
                Color.BLACK // color when unchecked
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        icon_instantrepair.setTintList(colorStateList);
        icon_outoffuel.setTintList(colorStateList);
        icon_tow.setTintList(colorStateList);

        fetchPlateNumbersFromFirebase();


        return rootView;
    }

    private void fetchPlateNumbersFromFirebase(){
        db_reference.child("Userinfo").child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plateNumbers.clear();
                for (DataSnapshot carSnapshot : snapshot.child("CarDetails").getChildren()) {
                    String plateNumber = carSnapshot.getKey();
                    plateNumbers.add(plateNumber);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        platenum_spinner.setBackground(getResources().getDrawable(R.drawable.dropdown_up_style));
    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        platenum_spinner.setBackground(getResources().getDrawable(R.drawable.dropdown_style));
    }
}