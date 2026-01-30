package com.example.hazardmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Volley Imports
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

// Google Imports
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // UI Elements
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegisterLink;
    SignInButton googleSignInBtn;

    // Google Variables
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    // --- CHANGE 1: Point to the correct file ---
    String URL_LOGIN = "http://10.0.2.2/serverside/mobile_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is ALREADY logged in
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        boolean isGoogleLoggedIn = GoogleSignIn.getLastSignedInAccount(this) != null;

        if (isLoggedIn || isGoogleLoggedIn) {
            goToProfileActivity();
        }

        setContentView(R.layout.activity_login);

        // Initialize UI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);
        googleSignInBtn = findViewById(R.id.sign_in_button);

        // onfigure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInBtn.setSize(SignInButton.SIZE_WIDE);

        // --- BUTTON CLICKS ---
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                loginWithPHP(email, password);
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });

        googleSignInBtn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        tvRegisterLink.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, "Google Login Success: " + account.getEmail(), Toast.LENGTH_SHORT).show();

            // Note: Google logins don't give us a MySQL ID easily, so we use 0 or -1 as a placeholder
            saveSession(0, account.getDisplayName(), account.getEmail());
            goToProfileActivity();
        } catch (ApiException e) {
            Toast.makeText(this, "Google Sign In Failed. Error: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
        }
    }

    // --- Updated Parsing Logic for mobile_login.php ---
    private void loginWithPHP(String email, String password) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("success")) {
                            // mobile_login.php sends data FLAT (no nested "user" object)
                            // We now grab the ID directly
                            int userId = jsonObject.getInt("user_id");
                            String fullName = jsonObject.getString("full_name");
                            String userEmail = jsonObject.getString("email");

                            // Save session with ID
                            saveSession(userId, fullName, userEmail);

                            Toast.makeText(LoginActivity.this, "Welcome " + fullName, Toast.LENGTH_SHORT).show();
                            goToProfileActivity();
                        } else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(LoginActivity.this, "Login Failed: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "Server Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(LoginActivity.this, "Connection Error. Check internet.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // ---Save the User ID (For Reporting) ---
    private void saveSession(int id, String name, String email) {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("isLoggedIn", true);
        editor.putInt("userId", id);     // <--- Saving the ID now!
        editor.putString("fullName", name);
        editor.putString("email", email);

        editor.apply();
    }

    private void goToProfileActivity() {
        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
        finish();
    }
}