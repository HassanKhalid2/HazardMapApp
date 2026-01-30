package com.example.hazardmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage;
    TextView profileName, profileEmail;
    Button btnSignOut, btnGoToMap, btnReportHazard, btnNews;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1. Setup Toolbar
        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        // 2. Initialize Views
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);

        btnSignOut = findViewById(R.id.btnSignOut);
        btnGoToMap = findViewById(R.id.btnGoToMap);
        btnReportHazard = findViewById(R.id.btnReportHazard);
        // --- INITIALIZE NEWS BUTTON ---
        btnNews = findViewById(R.id.btnNews);

        // 3. Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 4. CHECK WHO IS LOGGED IN
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleAccount != null) {
            profileName.setText(googleAccount.getDisplayName());
            profileEmail.setText(googleAccount.getEmail());
            if (googleAccount.getPhotoUrl() != null) {
                Glide.with(this).load(googleAccount.getPhotoUrl()).circleCrop().into(profileImage);
            }
        } else {
            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            String name = prefs.getString("fullName", "User");
            String email = prefs.getString("email", "No Email");

            profileName.setText(name);
            profileEmail.setText(email);

            String avatarUrl = "https://ui-avatars.com/api/?name=" + name + "&background=random&color=fff&size=200";
            Glide.with(this).load(avatarUrl).circleCrop().placeholder(android.R.drawable.sym_def_app_icon).into(profileImage);
        }

        // 5. Button Logic
        btnReportHazard.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, AddHazardActivity.class));
        });

        btnGoToMap.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MapsActivity.class));
        });

        // --- CLICK LISTENER FOR NEWS ---
        btnNews.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, NewsActivity.class));
        });

        btnSignOut.setOnClickListener(v -> signOut());
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(ProfileActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (item.getItemId() == R.id.menuAbout) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.menuShare) {

            // --- UPDATED SHARE LOGIC ---
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            // This is the message that will be pasted into WhatsApp/Email/etc.
            String shareMessage = "Check out HazardMap App on GitHub: https://github.com/HassanKhalid2/HazardMapApp";

            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.setType("text/plain");

            // This creates the "Chooser" popup
            Intent shareIntent = Intent.createChooser(sendIntent, "Share HazardMap via");
            startActivity(shareIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}