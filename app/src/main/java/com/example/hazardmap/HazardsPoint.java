package com.example.hazardmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HazardsPoint {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("location_name")
    @Expose
    private String locationName;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("hazard_type")
    @Expose
    private String hazardType;

    @SerializedName("reporter_name")
    @Expose
    private String reporterName;

    @SerializedName("report_date")
    @Expose
    private String reportDate;

    @SerializedName("other_details")
    @Expose
    private String otherDetails;

    public HazardsPoint() {
    }

    // Getters and Setters
    public String getLocationName() { return locationName; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }
    public String getHazardType() { return hazardType; }
    public String getReporterName() { return reporterName; }
    public String getReportDate() { return reportDate; }
    public String getOtherDetails() { return otherDetails; }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }
}