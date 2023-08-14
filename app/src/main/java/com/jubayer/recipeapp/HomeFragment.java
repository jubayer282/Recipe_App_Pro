package com.jubayer.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jubayer.recipeapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

     private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.floatingActionButton.setOnClickListener(view ->
                startActivity(new Intent(getContext(), AddRecipeActivity.class)));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRecipes();
    }

    private void loadRecipes() {
        // we wil load recipes from our database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
       /* reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                loadPopularRecipes(recipes);
                loadFavouriteRecipes(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });*/

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
                loadPopularRecipes(recipes);
                loadFavouriteRecipes(recipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });
    }

    private void loadPopularRecipes(List<Recipe> recipes) {
        List<Recipe> popularRecipes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * recipes.size());
            popularRecipes.add(recipes.get(random));
        }
        binding.rvPopulars.setAdapter(new HorizontalRecipeAdapter());
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvPopulars.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(popularRecipes);
        }
    }
    private void loadFavouriteRecipes(List<Recipe> recipes) {
        List<Recipe> favouriteRecipes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int random = (int) (Math.random() * recipes.size());
            favouriteRecipes.add(recipes.get(random));
        }
        binding.rvFavoriteMeal.setAdapter(new HorizontalRecipeAdapter());
        HorizontalRecipeAdapter adapter = (HorizontalRecipeAdapter) binding.rvFavoriteMeal.getAdapter();
        if (adapter != null) {
            adapter.setRecipeList(favouriteRecipes);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}