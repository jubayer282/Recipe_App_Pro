package com.jubayer.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jubayer.recipeapp.databinding.ActivityRecipeDetailsBinding;
import com.jubayer.recipeapp.models.Recipe;
import com.jubayer.recipeapp.room.FavouriteRecipe;
import com.jubayer.recipeapp.room.RecipeRepository;


public class RecipeDetailsActivity extends AppCompatActivity {
    ActivityRecipeDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }
    private void init() {
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        binding.tvName.setText(recipe.getName());
        binding.tcCategory.setText(recipe.getCategory());
        binding.tvDescription.setText(recipe.getDescription());
        binding.tvCalories.setText(String.format("%s Calories", recipe.getCalories()));
/*
        binding.tvTime.setText(recipe.getTime());
*/

        Glide
                .with(RecipeDetailsActivity.this)
                .load(recipe.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imgRecipe);

        if (recipe.getAuthorId().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())) {
            binding.imgEdit.setVisibility(View.VISIBLE);
        } else {
            binding.imgEdit.setVisibility(View.GONE);
        }
        binding.imgEdit.setOnClickListener(view -> {
             Intent intent = new Intent(binding.getRoot().getContext(), AddRecipeActivity.class);
             intent.putExtra("recipe", recipe);
             intent.putExtra("isEdit", true);
             binding.getRoot().getContext().startActivity(intent);
         });
        checkFavourite(recipe);
        binding.imgFvrt.setOnClickListener(view -> {
            favouriteRecipe(recipe);
        });
        updateDateWithFireBase(recipe.getId());
    }
    private void checkFavourite(Recipe recipe) {
        RecipeRepository repository = new RecipeRepository(getApplication());
        boolean isFavourite = repository.isFavourite(recipe.getId());
        if (isFavourite) {
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.accent));
        } else {
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.black));
        }
    }
    private void favouriteRecipe(Recipe recipe) {
        RecipeRepository repository = new RecipeRepository(getApplication());
        boolean isFavourite = repository.isFavourite(recipe.getId());
        if (isFavourite) {
            repository.delete(new FavouriteRecipe(recipe.getId()));
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.black));
        } else {
            repository.insert(new FavouriteRecipe(recipe.getId()));
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.accent));
        }
    }

    private void updateDateWithFireBase(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Recipe recipe = snapshot.getValue(Recipe.class);
                binding.tvName.setText(recipe.getName());
                binding.tcCategory.setText(recipe.getCategory());
                binding.tvDescription.setText(recipe.getDescription());
                binding.tvCalories.setText(String.format("%s Calories", recipe.getCalories()));
/*
                binding.tvTime.setText(recipe.getTime());
*/

                Glide
                        .with(RecipeDetailsActivity.this)
                        .load(recipe.getImage())
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(binding.imgRecipe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: ", error.toException());
            }
        });
    }

}