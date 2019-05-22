package com.example.tourmate.Class;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class RealTimeDB extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
