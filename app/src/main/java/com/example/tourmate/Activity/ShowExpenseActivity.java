package com.example.tourmate.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.Adapter.ShowExpenseAdapter;
import com.example.tourmate.Class.Expense;
import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowExpenseActivity extends AppCompatActivity {

    private RecyclerView recyclerViewId;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private String currentUserId;
    private String tripId;
    private ShowExpenseAdapter showExpenseAdapter;
    private List<Expense> expenseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        recyclerViewId = findViewById(R.id.recyclerViewId);
        expenseList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();


        Intent intent = getIntent();
        tripId = intent.getStringExtra("TripId");


        initRecyclerView();
        getDataFromExpense();

    }


    private void initRecyclerView() {
        recyclerViewId.setLayoutManager(new LinearLayoutManager(ShowExpenseActivity.this));
        showExpenseAdapter = new ShowExpenseAdapter(this,expenseList,tripId);
        recyclerViewId.setAdapter(showExpenseAdapter);
    }

    private void getDataFromExpense() {
        DatabaseReference reference = firebaseDatabase.getReference().child("UserList").child(currentUserId).child("TripList")
                                      .child(tripId).child("ExpenseList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    expenseList.clear();
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                         Expense expense = data.getValue(Expense.class);
                         expenseList.add(expense);
                         showExpenseAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
