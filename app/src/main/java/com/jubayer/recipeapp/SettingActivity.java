package com.jubayer.recipeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.jubayer.recipeapp.databinding.ActivitySettingBinding;
public class SettingActivity extends AppCompatActivity {
  ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*getSupportActionBar().hide();*/
        binding.linearLayoutShare.setOnClickListener(view -> shareApp());
        binding.linearLayoutRate.setOnClickListener(view -> rateApp());
        binding.linearLayoutFeedback.setOnClickListener(view -> feedbackApp());
        binding.linearLayoutMore.setOnClickListener(view -> moreApp());
        binding.linearLayoutPolicy.setOnClickListener(view -> privatePolicy());
        binding.btnLogout.setOnClickListener(view -> signOut());
    }

    private void signOut() {
             new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Sign Out", ((dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(SettingActivity.this, SignIn.class));
                    finishAffinity();
                })).setNegativeButton("Cancel", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })).show();
    }

    private void privatePolicy() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("https://www.google.com"));
        startActivity(intent);
    }
    private void moreApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.trodev.trodev&hl=en&gl=US" + getString(R.string.developer_id)));
        startActivity(intent);
    }
    private void feedbackApp() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.developer_email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for " + getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT , "Hi " + getString(R.string.developer_name) + ",");
        startActivity(intent);
    }
    private void rateApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.trodev.trodev&hl=en&gl=US" + getPackageName()));
        startActivity(intent);
    }
    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Recipe App");
        intent.putExtra(Intent.EXTRA_TEXT, "Get " + getString(R.string.app_name) + " to get the best recipes for your phone: https://play.google.com/store/apps/details?id=com.trodev.trodev&hl=en&gl=US" + getPackageName());
        startActivity(intent);
    }
}