package com.example.hazardmap;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {
    ListView listView;
    String API_URL = "http://10.0.2.2/serverside/api.php";
    ArrayList<Map<String, String>> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        listView = findViewById(R.id.listViewNews);
        fetchNews();
    }

    private void fetchNews() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            // --- LOGIC START: Determine the Title ---
                            String rawType = obj.getString("hazard_type");
                            String displayTitle = rawType.toUpperCase(); // Default (e.g., "FLOOD")

                            // If the type is "other", try to use the specific details instead
                            if (rawType.equalsIgnoreCase("other")) {
                                // Check if 'other_details' exists and is not empty
                                if (obj.has("other_details") && !obj.isNull("other_details")) {
                                    String details = obj.getString("other_details");
                                    if (!details.trim().isEmpty()) {
                                        displayTitle = details.toUpperCase(); // Use the custom text
                                    }
                                }
                            }
                            // --- LOGIC END ---

                            HashMap<String, String> map = new HashMap<>();


                            map.put("title", "⚠️ " + displayTitle);

                            map.put("location", obj.getString("location_name"));
                            map.put("details", "Date: " + obj.getString("report_date") + "\nReporter: " + obj.getString("reporter_name"));

                            newsList.add(map);
                        }

                        SimpleAdapter adapter = new SimpleAdapter(this, newsList, R.layout.news_item,
                                new String[]{"title", "location", "details"},
                                new int[]{R.id.tvNewsTitle, R.id.tvNewsLocation, R.id.tvNewsDate});
                        listView.setAdapter(adapter);
                    } catch (Exception e) {
                        Toast.makeText(NewsActivity.this, "Error parsing news", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(NewsActivity.this, "Connection Error", Toast.LENGTH_SHORT).show()
        );
        queue.add(stringRequest);
    }
}