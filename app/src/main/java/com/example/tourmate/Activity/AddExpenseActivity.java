package com.example.tourmate.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.Class.Expense;
import com.example.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText addExpenseTitleET,addExpenseAmountET;
    private Button addExpenseBtn;
    private TextView tripBudgetTV,tripCurrentExpenseTV;
    private String tripBudget;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private String currentUserId;
    private String tripId;
    private int budget;
    private int totalExpense = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        addExpenseTitleET = findViewById(R.id.addExpenseTitleET);
        addExpenseAmountET = findViewById(R.id.addExpenseAmountET);
        addExpenseBtn = findViewById(R.id.addExpenseBtn);

        tripBudgetTV = findViewById(R.id.tripBudgetTV);
        tripCurrentExpenseTV = findViewById(R.id.tripCurrentExpenseTV);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        tripId = intent.getStringExtra("TripId");
        tripBudget = intent.getStringExtra("Budget");

        onClick();
        checkExpense();

        tripBudgetTV.setText("Budget: "+tripBudget+"Taka");
    }


    private void onClick() {
        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ExpenseTitle = addExpenseTitleET.getText().toString();
                String ExpenseAmount = addExpenseAmountET.getText().toString();

                if(ExpenseTitle.equals("")){
                    Toast.makeText(AddExpenseActivity.this, "Enter Title", Toast.LENGTH_SHORT).show();
                }else if(ExpenseAmount.equals("")){
                    Toast.makeText(AddExpenseActivity.this, "Enter Amount", Toast.LENGTH_SHORT).show();
                }else{
                    int tempExpense = totalExpense;
                    tempExpense = tempExpense+Integer.valueOf(ExpenseAmount);

                    if(tempExpense >= budget){
                        AlertDialog.Builder alert = new AlertDialog.Builder(AddExpenseActivity.this);
                        alert.setTitle("Budget Alert");
                        alert.setMessage("Expense is abundance Budget.");
                        alert.setIcon(R.drawable.question);
                        alert.setCancelable(false);
                        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alert.show();
                    }else {
                        sendExpenseDataToDatabase(new Expense(ExpenseTitle,Integer.valueOf(ExpenseAmount)));
                    }
                }
            }
        });
    }

    private void sendExpenseDataToDatabase(Expense expense) {
        DatabaseReference reference = firebaseDatabase.getReference().child("UserList").child(currentUserId)
                .child("TripList").child(tripId).child("ExpenseList");
        String expenseId = reference.push().getKey();
        expense.setExpenseId(expenseId);
        expense.setTripId(tripId);

        reference.child(expenseId).setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddExpenseActivity.this, "Expense Save Successfully", Toast.LENGTH_SHORT).show();
                    addExpenseTitleET.setText("");
                    addExpenseAmountET.setText("");
                }
            }
        });

    }

    private void checkExpense() {
        DatabaseReference reference = firebaseDatabase.getReference().child("UserList").child(currentUserId)
                                     .child("TripList").child(tripId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                budget = dataSnapshot.child("tripBudget").getValue(Integer.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("ExpenseList").orderByChild("tripId").equalTo(tripId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    totalExpense += data.child("expenseAmount").getValue(Integer.class);

                }
                //Toast.makeText(AddExpenseActivity.this, ""+totalExpense, Toast.LENGTH_SHORT).show();
                tripCurrentExpenseTV.setText("Expense: "+String.valueOf(totalExpense)+"Taka");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
