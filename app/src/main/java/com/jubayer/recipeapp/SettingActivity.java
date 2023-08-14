package com.jubayer.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jubayer.recipeapp.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
  ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
    }
}