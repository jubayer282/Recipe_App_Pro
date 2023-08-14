package com.jubayer.recipeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jubayer.recipeapp.databinding.ActivityForgotPasswordBinding;

;

public class ForgotPassword extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Forgot Password");


        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(ForgotPassword.this);
        dialog.setCancelable(false);
        dialog.setMessage("Loading....");

        binding.forgotBtn.setOnClickListener(view -> {
            forgotPassword();

        });
    }

    private Boolean validateEmail() {
        String val = binding.email.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]";

        if (val.isEmpty()) {
            binding.email.setError("Field cannot be Empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            return false;
        } else {
            binding.email.setError(null);
            return true;
        }
    }


    private void forgotPassword() {

        if (!validateEmail())
        {
            dialog.show();

        }

        //dialog.show();

        auth.sendPasswordResetEmail(binding.email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isSuccessful())
                {
                    startActivity(new Intent(ForgotPassword.this, SignIn.class));
                    finish();

                    Toast.makeText(ForgotPassword.this, "Please Check Your Email Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ForgotPassword.this, "Enter correct email ID", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}