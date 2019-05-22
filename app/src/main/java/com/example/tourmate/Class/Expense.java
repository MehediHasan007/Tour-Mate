package com.example.tourmate.Class;

public class Expense {
    private String expenseId;
    private String tripId;
    private String expenseTitle;
    private int expenseAmount;

    public Expense() {
    }

    public Expense(String expenseTitle, int expenseAmount) {
        this.expenseTitle = expenseTitle;
        this.expenseAmount = expenseAmount;
    }

    public Expense(String expenseId, String tripId, String expenseTitle, int expenseAmount) {
        this.expenseId = expenseId;
        this.tripId = tripId;
        this.expenseTitle = expenseTitle;
        this.expenseAmount = expenseAmount;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }
}
