package com.jubayer.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jubayer.recipeapp.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private int splashScreenTime = 3000; // 3 seconds
    private int timeInterval = 100; // 0.1 seconds
    private int progress = 0; // 0 to 100 for progress bar
    private Runnable runnable;
    private Handler handler;

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progressBar.setMax(splashScreenTime); // set max values for progress bar
        binding.progressBar.setProgress(progress); // set initial value for progress bar
        handler = new Handler(Looper.getMainLooper()); // create a handler
        runnable = () -> {

            // check splash screen time
            if (progress < splashScreenTime) {
                progress += timeInterval;
                binding.progressBar.setProgress(progress);
                handler.postDelayed(runnable, timeInterval);
            } else {
                // check user is logged in or not
                FirebaseApp.initializeApp(this);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) { // if user is logged in
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                else { // if user is not logged in
                    startActivity(new Intent(SplashActivity.this, SignIn.class));
                    Toast.makeText(SplashActivity.this, "Welcome to login page", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
         handler.postDelayed(runnable, timeInterval); // start handler
    }
}