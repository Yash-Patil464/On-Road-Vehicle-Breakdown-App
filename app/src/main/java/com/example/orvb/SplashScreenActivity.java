package com.example.orvb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreenActivity extends AppCompatActivity {

    LottieAnimationView splashscreen_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashscreen_animation = findViewById(R.id.ss_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(main);
            }
        },5000);
    }
}