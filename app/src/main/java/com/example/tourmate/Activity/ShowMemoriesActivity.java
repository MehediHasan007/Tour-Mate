package com.example.tourmate.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.example.tourmate.Adapter.ShowExpenseAdapter;
import com.example.tourmate.Adapter.ShowMemoryAdapter;
import com.example.tourmate.Class.Expense;
import com.example.tourmate.Class.Upload;
import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowMemoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewId;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private String currentUserId;
    private String tripId;
    private ShowMemoryAdapter showMemoryAdapter;
    private List<Upload> uploadList;
    private ProgressBar progress_circular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memories);

        recyclerViewId = findViewById(R.id.recyclerViewId);
        uploadList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        progress_circular = findViewById(R.id.progress_circular);

        Intent intent = getIntent();
        tripId = intent.getStringExtra("TripId");

        initRecyclerView();
        getDataFromUpload();
    }

    private void initRecyclerView() {
        recyclerViewId.setLayoutManager(new LinearLayoutManager(ShowMemoriesActivity.this));
        showMemoryAdapter = new ShowMemoryAdapter(uploadList,this,tripId);
        recyclerViewId.setAdapter(showMemoryAdapter);
    }

    private void getDataFromUpload() {
        DatabaseReference reference = firebaseDatabase.getReference().child("UserList").child(currentUserId).child("TripList")
                .child(tripId).child("MemoryList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    uploadList.clear();
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        Upload upload = data.getValue(Upload.class);
                        uploadList.add(upload);
                        showMemoryAdapter.notifyDataSetChanged();
                        progress_circular.setVisibility(recyclerViewId.INVISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progress_circular.setVisibility(recyclerViewId.INVISIBLE);

            }
        });
    }


}
