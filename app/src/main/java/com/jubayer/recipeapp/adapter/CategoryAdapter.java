package com.jubayer.recipeapp.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jubayer.recipeapp.AllRecipesActivity;
import com.jubayer.recipeapp.R;
import com.jubayer.recipeapp.databinding.ItemCategoryBinding;
import com.jubayer.recipeapp.models.Category;

import java.util.ArrayList;
import java.util.List;



public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    List<Category> categoryList = new ArrayList<>();

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.onBind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;

        public CategoryHolder(@NonNull ItemCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }


        public void onBind(Category category) {

            binding.tvName.setText(category.getName());
            Glide
                    .with(binding.getRoot())
                    .load(category.getImage())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(binding.imgBgCategory);

            binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(binding.getRoot().getContext(), AllRecipesActivity.class);
                intent.putExtra("type", "category");
                intent.putExtra("category", category.getName());
                binding.getRoot().getContext().startActivity(intent);
            });
        }
    }
}
