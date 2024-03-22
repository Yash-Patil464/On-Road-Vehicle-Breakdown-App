package com.example.orvb;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_cars, container, false);
        String phoneNumber = UserManager.getInstance().getPhoneNumber();
        DatabaseReference db_reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://orvb-sem-proj-default-rtdb.firebaseio.com/");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView show_name = rootView.findViewById(R.id.show_name);
        Button add_car_button = rootView.findViewById(R.id.add_car_button);

        List dataList = new ArrayList<>();
        CarRecycleViewAdapter adapter = new CarRecycleViewAdapter(dataList);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycleView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        dataList.add(new Car("Plate No. of Car", "Gear Type", "Fuel Type"));

        add_car_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new CarDetailsPage())
                        .addToBackStack(null)
                        .commit();
            }
        });

        db_reference.child("Userinfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(phoneNumber)){
                    String getName = snapshot.child(phoneNumber).child("Name").getValue(String.class);
                    getName = extractFirstName(getName);
                    getName = capitalizeFirstLetter(getName);
                    show_name.setText(String.format("Hi, %s", getName));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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