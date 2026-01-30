package com.example.hazardmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide the top toolbar/actionbar for a clean look
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Wait 2 seconds, then go to Login Activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Change LoginActivity.class to wherever you want to go first
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Prevents user from going back to splash screen
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}