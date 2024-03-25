package com.example.orvb;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MyCarsFragment extends Fragment implements CarRecycleViewAdapter.OnItemClickListener {
    private List<Car> dataList = new ArrayList<>();
    private DatabaseReference db_reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_cars, container, false);
        String phoneNumber = UserManager.getInstance().getPhoneNumber();
        db_reference = FireBaseManager.getInstance().getDatabaseReference();

        TextView show_name = rootView.findViewById(R.id.show_name);
        Button add_car_button = rootView.findViewById(R.id.add_car_button);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycleView);
        CarRecycleViewAdapter adapter = new CarRecycleViewAdapter(dataList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        add_car_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new CarDetailsPage());
            }
        });

        db_reference.child("Userinfo").child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();

                if (snapshot.exists()) {
                    String name = snapshot.child("Name").getValue(String.class);
                    if (name != null) {
                        name = capitalizeAndExtractFirstName(name);
                        show_name.setText(String.format("Hi, %s", name));
                    }

                    for (DataSnapshot carSnapshot : snapshot.child("CarDetails").getChildren()) {
                        if (carSnapshot.hasChild("FuelType") && carSnapshot.hasChild("GearType")) {
                            int gearType = carSnapshot.child("GearType").getValue(Integer.class);
                            int fuelType = carSnapshot.child("FuelType").getValue(Integer.class);
                            String gearTypeStr = (gearType == 1) ? "Automatic" : "Manual";
                            String fuelTypeStr = getFuelTypeString(fuelType);
                            String plateNumber = carSnapshot.getKey();
                            dataList.add(new Car(plateNumber, gearTypeStr, fuelTypeStr));
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("MyCarsFragment", "User with phone number " + phoneNumber + " does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyCarsFragment", "Error: " + error.getMessage());
            }
        });

        return rootView;
    }

    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private String capitalizeAndExtractFirstName(String fullName) {
        String[] parts = fullName.trim().split("\\s+");
        return parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1);
    }

    private String getFuelTypeString(int fuelType) {
        switch (fuelType) {
            case 1:
                return "Petrol";
            case 2:
                return "CNG";
            case 3:
                return "Diesel";
            default:
                return "Unknown";
        }
    }

    @Override
    public void onItemClick(String plateNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("plateNumber", plateNumber);
        EditCarInfo editCarInfoFragment = new EditCarInfo();
        editCarInfoFragment.setArguments(bundle);
        replaceFragment(editCarInfoFragment);
    }
}
