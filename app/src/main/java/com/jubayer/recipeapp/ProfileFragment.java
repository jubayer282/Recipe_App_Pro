package com.jubayer.recipeapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jubayer.recipeapp.databinding.FragmentProfileBinding;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

private FragmentProfileBinding binding;
private User user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadProfile();
        loadUserRecipes();
        init();
    }

    private void init() {
        binding.imgEditProfile.setOnClickListener( view -> {
            // pick image from gallery and upload it to firebase store
            PickImageDialog.build(new PickSetup()).show(requireActivity()).setOnPickResult(r -> {
               Log.e("ProfileFragment", "onPickResult: " + r.getUri());
               binding.imgProfile.setImageBitmap(r.getBitmap());
                binding.imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                uploadImage(r.getBitmap());
            }).setOnPickCancel(() -> Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show());
        });

        binding.imgEditCover.setOnClickListener(view -> {
            // pick image from gallery and upload it to firebase store
            PickImageDialog.build(new PickSetup()).show(requireActivity()).setOnPickResult(r -> {
                Log.e("ProfileFragment", "onPickResult: " + r.getUri());
                binding.imgCover.setImageBitmap(r.getBitmap());
                binding.imgCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
                uploadCoverImage(r.getBitmap());
            }).setOnPickCancel(() -> Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show());
        });
    }

    private void uploadCoverImage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + FirebaseAuth.getInstance().getUid()+"image.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }

            // Continue with the task to get the download URL
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                // download url in firebase database
                Toast.makeText(requireContext(), "Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                user.setCover(Objects.requireNonNull(downloadUri).toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);
            } else {
                // Handle failures
                Log.e("ProfileFragment", "onComplete: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void uploadImage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/" + FirebaseAuth.getInstance().getUid()+"image.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }

            // Continue with the task to get the download URL
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                // download url in firebase database
                Toast.makeText(requireContext(), "Image uploaded Successfully", Toast.LENGTH_SHORT).show();
                user.setImage(Objects.requireNonNull(downloadUri).toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);
            } else {
                // Handle failures
                Log.e("ProfileFragment", "onComplete: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    private void loadUserRecipes() {
        binding.rvProfile.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvProfile.setAdapter(new RecipeAdapter());
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
       reference.child("Recipes").orderByChild("authorId").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipe> recipes = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Recipe recipe = dataSnapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }
               ((RecipeAdapter) Objects.requireNonNull(binding.rvProfile.getAdapter())).setRecipeList(recipes);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "onCancelled: " + error.getMessage());
           }
       });
    }

    private void loadProfile() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    binding.tvUserName.setText(user.getName());
                    binding.tvEmail.setText(user.getEmail());

                    // we don't need image url, we will directly uploaded image from firebase
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("images/" + FirebaseAuth.getInstance().getUid()+"image.jpg");

                    Glide
                            .with(requireContext())
                            .load(user.getImage())
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher)
                            .into(binding.imgProfile);

                    Glide
                            .with(requireContext())
                            .load(user.getCover())
                            .centerCrop()
                            .placeholder(R.drawable.bg_default_recipe)
                            .into(binding.imgCover);


                } else {
                    Log.e("ProfileFragment", "onDataChange: User is null");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });

        User user = new User(); // load from firebase here for user section
        user.setName("Jubayer Hossain");
        user.setEmail("jubayer.trodev@gmail.com");
        binding.tvUserName.setText(user.getName());
        binding.tvEmail.setText(user.getEmail());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}