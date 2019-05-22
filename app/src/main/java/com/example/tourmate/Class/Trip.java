package com.example.tourmate.Class;

public class Trip {
    private String tripId;
    private String tripName;
    private String tripDescription;
    private int tripBudget;
    private String tripStartDate;
    private String tripEndDate;

    public Trip() {
    }

    public Trip(String tripName, String tripDescription, int tripBudget, String tripStartDate, String tripEndDate) {
        this.tripName = tripName;
        this.tripDescription = tripDescription;
        this.tripBudget = tripBudget;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
    }

    public Trip(String tripId, String tripName, String tripDescription, int tripBudget, String tripStartDate, String tripEndDate) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.tripDescription = tripDescription;
        this.tripBudget = tripBudget;
        this.tripStartDate = tripStartDate;
        this.tripEndDate = tripEndDate;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public int getTripBudget() {
        return tripBudget;
    }

    public String getTripStartDate() {
        return tripStartDate;
    }

    public String getTripEndDate() {
        return tripEndDate;
    }
}
