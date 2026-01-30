package com.example.hazardmap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddHazardActivity extends AppCompatActivity {

    private EditText etLocation, etLat, etLng, etReporter, etDesc;
    private Spinner spType;
    private Button btnSubmit, btnGps;
    private LinearLayout layoutOtherDetails;

    private static final String API_URL = "http://10.0.2.2/serverside/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hazard);

        // ----TOOLBAR ---
        Toolbar toolbar = findViewById(R.id.add_hazard_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Initialize Views
        etLocation = findViewById(R.id.etLocationName);
        etLat = findViewById(R.id.etLatitude);
        etLng = findViewById(R.id.etLongitude);
        etReporter = findViewById(R.id.etReporterName);
        etDesc = findViewById(R.id.etDescription);
        spType = findViewById(R.id.spHazardType);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnGps = findViewById(R.id.btnGetGps);
        layoutOtherDetails = findViewById(R.id.layoutOtherDetails);


        // Setup Spinner
        String[] types = {"Flood", "Landslide", "Fire", "Accident", "Construction", "Road Closure", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spType.setAdapter(adapter);

        // Toggle "Other Details" visibility
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = types[position];
                if (selected.equals("Other")) {
                    layoutOtherDetails.setVisibility(View.VISIBLE);
                } else {
                    layoutOtherDetails.setVisibility(View.GONE);
                    etDesc.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // GPS Button Logic
        btnGps.setOnClickListener(v -> {
            Intent intent = new Intent(AddHazardActivity.this, PickLocationActivity.class);
            pickLocationLauncher.launch(intent);
        });

        // Submit Logic
        btnSubmit.setOnClickListener(v -> submitHazard());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // --- HELPER: Handle GPS Result ---
    ActivityResultLauncher<Intent> pickLocationLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        double lat = data.getDoubleExtra("picked_lat", 0);
                        double lng = data.getDoubleExtra("picked_lng", 0);
                        etLat.setText(String.valueOf(lat));
                        etLng.setText(String.valueOf(lng));

                        // Convert coords to address
                        autoDetectAddress(lat, lng);
                    }
                }
            });

    // --- HELPER: Reverse Geocoding ---
    private void autoDetectAddress(double lat, double lng) {
        etLocation.setText("Detecting address...");
        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(AddHazardActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String addressText = address.getAddressLine(0);
                    runOnUiThread(() -> etLocation.setText(addressText));
                } else {
                    runOnUiThread(() -> etLocation.setText("Unknown Location"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    etLocation.setText("");
                    etLocation.setHint("Could not detect. Please type name.");
                });
            }
        }).start();
    }

    private void submitHazard() {
        String locName = etLocation.getText().toString().trim();
        String lat = etLat.getText().toString().trim();
        String lng = etLng.getText().toString().trim();
        String reporter = etReporter.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String type = spType.getSelectedItem().toString().toLowerCase().replace(" ", "_");

        // --- VALIDATION 1: Basic Fields ---
        if (lat.isEmpty() || lng.isEmpty() || reporter.isEmpty()) {
            Toast.makeText(this, "Coordinates and Reporter are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- VALIDATION 2: Check "Other" Details ---
        if (type.equals("other") && desc.isEmpty()) {
            // Show error message
            Toast.makeText(this, "Please specify details for 'Other' hazard type", Toast.LENGTH_LONG).show();
            // Optional: Set focus to the description box so keyboard opens
            etDesc.requestFocus();
            // STOP here. Do not send to server.
            return;
        }

        // Get User ID from Session (Invisible to User)
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("location_name", locName.isEmpty() ? "Unknown" : locName)
                .add("latitude", lat)
                .add("longitude", lng)
                .add("hazard_type", type)
                .add("reporter_name", reporter)
                .add("other_details", desc)
                .add("user_id", String.valueOf(userId))
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(AddHazardActivity.this, "Network Error", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(AddHazardActivity.this, "Report Submitted!", Toast.LENGTH_LONG).show();
                        finish();
                    });
                }
            }
        });
    }
}