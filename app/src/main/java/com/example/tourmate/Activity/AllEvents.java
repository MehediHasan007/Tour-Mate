package com.example.tourmate.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.tourmate.Adapter.TripAdapter;
import com.example.tourmate.Class.Trip;
import com.example.tourmate.MapsActivity;
import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllEvents extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    private FloatingActionButton floatingActionButtonId;
    private RecyclerView recyclerViewId;
    private TripAdapter tripAdapter;
    private List<Trip> tripList;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        recyclerViewId = findViewById(R.id.recyclerViewId);
        tripList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        initRecyclerView();
        getDataFromTrip();

        //FloatingActionButton
        floatingActionButtonId = findViewById(R.id.floatingActionId);
        floatingActionButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllEvents.this, AddTrip.class));
            }
        });

        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_Id);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_weather) {
            startActivity(new Intent(AllEvents.this, WeatherActivity.class));
        } else if (menuItem.getItemId() == R.id.nav_nearby) {
            startActivity(new Intent(AllEvents.this, MapsActivity.class));
        } else if (menuItem.getItemId() == R.id.menuLogout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(AllEvents.this, MainActivity.class));
            return true;
        }
        return false;
    }

    private void getDataFromTrip() {
        DatabaseReference reference = firebaseDatabase.getReference().child("UserList").child(currentUserId).child("TripList");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    tripList.clear();
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        Trip trip = data.getValue(Trip.class);
                        tripList.add(trip);
                        tripAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecyclerView() {
        recyclerViewId.setLayoutManager(new LinearLayoutManager(AllEvents.this));
        tripAdapter = new TripAdapter(this,tripList);
        recyclerViewId.setAdapter(tripAdapter);

    }
}
