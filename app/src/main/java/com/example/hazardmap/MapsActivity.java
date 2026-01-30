package com.example.hazardmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // Add these inside the class, near private GoogleMap mMap;
// Put this inside your class, just like before
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable refreshRunnable;
    private final int REFRESH_DELAY = 10000; // 10 seconds (better for battery)
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    // NOTE: Use 10.0.2.2 for Emulator, or your PC IP for real device
    private static final String API_URL = "http://10.0.2.2/serverside/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // 1. Setup Toolbar
        Toolbar toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // 2. Setup Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 3. Setup Report Button
        FloatingActionButton fab = findViewById(R.id.fabReport);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(MapsActivity.this, AddHazardActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoRefresh();
        if (mMap != null) {
            fetchHazards();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopAutoRefresh(); // Stop the timer when app is not in focus
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableUserLocation();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 15));
                } else {
                    LatLng arau = new LatLng(6.4, 100.29);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arau, 10));
                }
            });
        } else {
            LatLng arau = new LatLng(6.4, 100.29);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arau, 10));
        }

        fetchHazards();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                onMapReady(mMap);
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchHazards() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL,
                response -> {
                    try {
                        JSONArray hazards = new JSONArray(response);
                        mMap.clear();

                        for (int i = 0; i < hazards.length(); i++) {
                            JSONObject obj = hazards.getJSONObject(i);

                            double lat = obj.getDouble("latitude");
                            double lng = obj.getDouble("longitude");
                            String hazardType = obj.getString("hazard_type");
                            String locationName = obj.optString("location_name", "Unknown Location");
                            String reporter = obj.optString("reporter_name", "Anonymous");
                            String date = obj.optString("report_date", "");
                            String otherDetails = obj.optString("other_details", "");

                            String displayTitle = hazardType;
                            if ("other".equalsIgnoreCase(hazardType) && !otherDetails.isEmpty()) {
                                displayTitle = otherDetails;
                            }
                            if (displayTitle.length() > 0) {
                                displayTitle = displayTitle.substring(0, 1).toUpperCase() +
                                        displayTitle.substring(1).toLowerCase();
                            }

                            String fullSnippet = displayTitle + " | Rep: " + reporter + " | " + date;

                            // --- CHANGED LOGIC HERE ---
                            // Get the correct image ID based on type
                            int iconResId = getHazardIcon(hazardType);

                            LatLng position = new LatLng(lat, lng);
                            MarkerOptions options = new MarkerOptions()
                                    .position(position)
                                    .title(locationName)
                                    .snippet(fullSnippet)
                                    .icon(BitmapDescriptorFactory.fromResource(iconResId));

                            mMap.addMarker(options);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MapsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(MapsActivity.this, "Cannot connect to server", Toast.LENGTH_SHORT).show();
                    Log.e("VolleyError", error.toString());
                });

        queue.add(stringRequest);
    }

    // --- Logic to assign Custom Icons ---
    private int getHazardIcon(String hazardType) {
        if (hazardType == null) return R.drawable.ic_other;
        String type = hazardType.toLowerCase();

        if (type.contains("flood")) {
            return R.drawable.ic_flood;
        } else if (type.contains("landslide")) {
            return R.drawable.ic_landslide;
        } else if (type.contains("fire")) {
            return R.drawable.ic_fire;
        } else if (type.contains("construction")) {
            return R.drawable.ic_construction;
        } else if (type.contains("accident")) {
            return R.drawable.ic_accident;
        } else if (type.contains("road")) { // Matches "road_closure"
            return R.drawable.ic_road_closure;
        } else {
            return R.drawable.ic_other; // Default for "Other" or unknown types
        }
    }
    private void startAutoRefresh() {
        // If a timer is already running, cancel it first to avoid duplicates
        stopAutoRefresh();

        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                fetchHazards(); // Load data
                // Schedule the next run
                handler.postDelayed(this, REFRESH_DELAY);
            }
        };
        // Run immediately the first time
        handler.post(refreshRunnable);
    }

    private void stopAutoRefresh() {
        if (handler != null && refreshRunnable != null) {
            handler.removeCallbacks(refreshRunnable);
        }
    }
}