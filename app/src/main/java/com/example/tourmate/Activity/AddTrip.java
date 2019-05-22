package com.example.tourmate.Activity;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.Class.Trip;
import com.example.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTrip extends AppCompatActivity {

    private SimpleDateFormat dateAndTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");

    private TextView tripStartDateTV,tripEndDateTV;
    private EditText tripNameET,tripDescriptionET,tripBudgetET;
    private Button addTripBtn;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        tripStartDateTV = findViewById(R.id.tripStartDateTV);
        tripEndDateTV = findViewById(R.id.tripEndDateTV);
        tripNameET = findViewById(R.id.tripNameET);
        tripDescriptionET = findViewById(R.id.tripDescriptionET);
        tripBudgetET = findViewById(R.id.tripBudgetET);
        addTripBtn = findViewById(R.id.addTripBtn);


        tripStartDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String selectedDate = year+"/"+month+"/"+day+" 00:00:00";
                        //e.g- 2019/3/9 00:00:00
                        Date date = null;

                        try {
                            date = dateAndTimeSDF.parse(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long seletedDateinMS = date.getTime();
                        tripStartDateTV.setText(dateSDF.format(date));

                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTrip.this,dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });

        tripEndDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String selectedDate = year+"/"+month+"/"+day+" 00:00:00";
                        //e.g- 2019/3/9 00:00:00
                        Date date = null;

                        try {
                            date = dateAndTimeSDF.parse(selectedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long seletedDateinMS = date.getTime();
                        tripEndDateTV.setText(dateSDF.format(date));

                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTrip.this,dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });

       addTripBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addTrip();
           }
       });
    }

    private void addTrip() {
        String TripName = tripNameET.getText().toString();
        String TripDescription = tripDescriptionET.getText().toString();
        String TripBudget = tripBudgetET.getText().toString();
        String TripStartDate = tripStartDateTV.getText().toString();
        String TripEndDate = tripEndDateTV.getText().toString();

        if(TripName.equals("")){
            Toast.makeText(this, "Enter Trip Name", Toast.LENGTH_SHORT).show();
        }else if(TripDescription.equals("")){
            Toast.makeText(this, "Enter Trip Description", Toast.LENGTH_SHORT).show();
        }else if(TripBudget.equals("")){
            Toast.makeText(this, "Enter Trip Budget", Toast.LENGTH_SHORT).show();
        }else if(TripStartDate.equals("")){
            Toast.makeText(this, "Enter Trip Start Date", Toast.LENGTH_SHORT).show();
        }else if(TripEndDate.equals("")){
            Toast.makeText(this, "Enter Trip End Date", Toast.LENGTH_SHORT).show();
        }else{
            SaveToDB(new Trip(TripName,TripDescription,Integer.valueOf(TripBudget),TripStartDate,TripEndDate));
        }
    }

    private void SaveToDB(Trip trip) {

        DatabaseReference reference = firebaseDatabase.getReference().child("UserList").child(currentUserId).child("TripList");

        String tripID = reference.push().getKey();
        trip.setTripId(tripID);

        reference.child(tripID).setValue(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddTrip.this, "Save data successful", Toast.LENGTH_SHORT).show();
                    tripNameET.setText("");
                    tripDescriptionET.setText("");
                    tripBudgetET.setText("");
                    tripStartDateTV.setText("");
                    tripEndDateTV.setText("");
                }

            }
        });
    }
}
