package com.jubayer.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jubayer.recipeapp.databinding.ActivityAllRecipesBinding;

public class AllRecipesActivity extends AppCompatActivity {
    ActivityAllRecipesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllRecipesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("category")) {
            filterByCategory();
        } else if (type.equalsIgnoreCase("recipe")) {
            loadByRecipes();
        } else {
            loadAllRecipes();
        }
    }

    private void loadAllRecipes() {
    }

    private void loadByRecipes() {
        
    }

    private void filterByCategory() {

    }
}