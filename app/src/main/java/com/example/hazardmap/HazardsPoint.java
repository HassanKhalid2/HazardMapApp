package com.example.hazardmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//---Jsonschema2pojo letak sini---
public class HazardsPoint {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("location_name")
    @Expose
    private String locationName;

    @SerializedName("latitude")
    @Expose
    private Double latitude; // Changed to Double for Map compatibility

    @SerializedName("longitude")
    @Expose
    private Double longitude; // Changed to Double for Map compatibility

    @SerializedName("hazard_type")
    @Expose
    private String hazardType;

    @SerializedName("reporter_name")
    @Expose
    private String reporterName;

    @SerializedName("report_date")
    @Expose
    private String reportDate;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("user_agent")
    @Expose
    private String userAgent;

    @SerializedName("other_details")
    @Expose
    private String otherDetails;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    // --- CONSTRUCTORS ---
    public HazardsPoint() {
    }

    // --- GETTERS ---
    public Integer getId() { return id; }
    public String getLocationName() { return locationName; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getHazardType() { return hazardType; }
    public String getReporterName() { return reporterName; }
    public String getReportDate() { return reportDate; }
    public String getCreatedAt() { return createdAt; }
    public String getUserAgent() { return userAgent; }
    public String getOtherDetails() { return otherDetails; }
    public Integer getUserId() { return userId; }

    // --- SETTERS ---
    public void setId(Integer id) { this.id = id; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setHazardType(String hazardType) { this.hazardType = hazardType; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public void setOtherDetails(String otherDetails) { this.otherDetails = otherDetails; }
    public void setUserId(Integer userId) { this.userId = userId; }
}