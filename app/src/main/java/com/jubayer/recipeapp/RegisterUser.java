package com.jubayer.recipeapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jubayer.recipeapp.databinding.ActivityRegisterUserBinding;


import java.util.HashMap;

public class RegisterUser extends AppCompatActivity {
    ActivityRegisterUserBinding binding;
    private String emailStr, passStr, confirmPassStr, nameStr, phoneStr;
    ProgressDialog dialog;
    private String deviceID;
    FirebaseAuth auth;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Registration Page");

        getSupportActionBar().hide();

        deviceID = Settings.Secure.getString(RegisterUser.this.getContentResolver(), Settings.Secure.ANDROID_ID);

        auth = FirebaseAuth.getInstance();


        dialog = new ProgressDialog(RegisterUser.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");

        binding.registerBtn.setOnClickListener(view -> {
            registerUer();
            if (binding.mobileNo.length()>11)
            {
                binding.mobileNo.setError("Mobile No is Invalid !");
            }
        });

        binding.loginNowBtn.setOnClickListener(v -> {

            startActivity(new Intent(RegisterUser.this, SignIn.class));
            finishAffinity();
            Toast.makeText(RegisterUser.this, "Welcome Login now", Toast.LENGTH_SHORT).show();
        });

    }

    private Boolean validateEmail(){
        String val = binding.email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            binding.email.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)){
            binding.email.setError("Invalid email address");
            return false;
        } else {
            binding.email.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo(){
        String val = binding.mobileNo.getText().toString();
        if (val.isEmpty()){
            binding.mobileNo.setError("Field cannot be Empty");
            return false;
        } else {
            binding.mobileNo.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = binding.password.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";

        if (val.isEmpty()) {
            binding.password.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            binding.password.setError("Password is too weak");
            return false;
        } else {
            binding.password.setError(null);
            return true;
        }

    }

    private Boolean validateConfirmPassword(){
        String val = binding.confirmPassword.getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +
                "(?=.*[@#$%^&+=])" +
                "(?=\\S+$)" +
                ".{4,}" +
                "$";

        if (val.isEmpty()) {
            binding.confirmPassword.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            binding.confirmPassword.setError("Password is too weak");
            return false;
        } else {
            binding.confirmPassword.setError(null);
            return true;
        }

    }

    private void registerUer() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

        final FirebaseUser user = auth.getCurrentUser();

        if (!validateEmail() || !validatePhoneNo() || !validatePassword() || !validateConfirmPassword())
        {
            return;
        }

        nameStr = binding.name.getEditableText().toString();
        emailStr = binding.email.getText().toString();
        phoneStr = binding.mobileNo.getText().toString();
        passStr = binding.password.getText().toString();
        confirmPassStr = binding.confirmPassword.getText().toString();

        Query query = reference.orderByChild("deviceID").equalTo(deviceID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    Toast.makeText(RegisterUser.this, "Device already registered with Us.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (validate())
                    {
                        signUpUser(user, nameStr, emailStr, passStr);
                        dialog.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void signUpUser(FirebaseUser user, String nameStr, String emailStr, String passStr) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
       auth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(task -> {
           if (task.isSuccessful())
           {
               dialog.dismiss();
               String userId = auth.getCurrentUser().getUid();

               HashMap<String , Object> map = new HashMap<>();

               map.put("Email", emailStr);
               map.put("name", nameStr);
               map.put("password", passStr);
               map.put("Mobile", phoneStr);
               map.put("userId", userId);

               reference.child(userId).setValue(map).addOnCompleteListener(task1 -> {

                   Toast.makeText(RegisterUser.this, "Register Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterUser.this, SignIn.class));
               }).addOnFailureListener(e -> {
                   Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   dialog.dismiss();
               });

           }
       });

    }

    private boolean validate() {
        if (passStr.compareTo(confirmPassStr) !=0)
        {
            Toast.makeText(RegisterUser.this, "Password not same.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}