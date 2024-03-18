package com.example.orvb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.orvb.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new MyCarsFragment());
        binding.bottomNavView.setBackground(null);

        int selectedColor = 0xFFEABE6C;
        int defaultColor = Color.BLACK;
        ColorStateList colors = new ColorStateList(new int[][]{{android.R.attr.state_checked}, {}},
                new int[]{selectedColor, defaultColor});
        binding.bottomNavView.setItemIconTintList(colors);

        binding.bottomNavView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.car) {
                replaceFragment(new MyCarsFragment());
                binding.bottomNavView.setItemIconTintList(colors);
            }

            if (menuItem.getItemId() == R.id.services) {
                replaceFragment(new ServicesFragment());
                binding.bottomNavView.setItemIconTintList(colors);
            }

            if (menuItem.getItemId() == R.id.orders) {
                replaceFragment(new OrdersFragment());
                binding.bottomNavView.setItemIconTintList(colors);
            }

            return true;
        });

    }

    void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}