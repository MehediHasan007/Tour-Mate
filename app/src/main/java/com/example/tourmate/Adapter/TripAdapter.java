package com.example.tourmate.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.tourmate.Activity.AddExpenseActivity;
import com.example.tourmate.Activity.AddMemoriesActivity;
import com.example.tourmate.Activity.ShowExpenseActivity;
import com.example.tourmate.Activity.ShowMemoriesActivity;
import com.example.tourmate.Activity.UpdateTrip;
import com.example.tourmate.Class.Trip;
import com.example.tourmate.Fragment.BottomSheet;
import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<Trip> tripList;
    private Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String currentUserId = auth.getCurrentUser().getUid();

    public TripAdapter(Context context, List<Trip> tripList) {
        this.tripList = tripList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trips_recyclerview_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Trip currentTrip = tripList.get(i);
        viewHolder.tripNameTV.setText(currentTrip.getTripName());
        viewHolder.tripDescriptionTV.setText(currentTrip.getTripDescription());

        viewHolder.detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString("Trip Name", currentTrip.getTripName());
                bundle.putString("Trip Description", currentTrip.getTripDescription());
                bundle.putString("Trip Budget", String.valueOf(currentTrip.getTripBudget()) + " Taka");
                bundle.putString("Trip Start Date", currentTrip.getTripStartDate());
                bundle.putString("Trip End Date", currentTrip.getTripEndDate());

                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), "");
            }
        });

        //Expense
        viewHolder.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.expenseBtn);
                popupMenu.getMenuInflater().inflate(R.menu.expense_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addExpenseMenu:
                                Intent intent = new Intent(context, AddExpenseActivity.class);
                                intent.putExtra("TripId", currentTrip.getTripId());
                                intent.putExtra("Budget",String.valueOf(currentTrip.getTripBudget()));
                                context.startActivity(intent);
                                return true;

                            case R.id.showExpenseMenu:
                                intent = new Intent(context, ShowExpenseActivity.class);
                                intent.putExtra("TripId", currentTrip.getTripId());
                                context.startActivity(intent);
                                return true;
                        }

                        return false;
                    }
                });
            }
        });

        //Memories
        viewHolder.memoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.memoriesBtn);
                popupMenu.getMenuInflater().inflate(R.menu.memories_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.addMemoriesMenu:
                                Intent intent = new Intent(context, AddMemoriesActivity.class);
                                intent.putExtra("TripId", currentTrip.getTripId());
                                context.startActivity(intent);
                                return true;

                            case R.id.showMemoriesMenu:
                                intent = new Intent(context, ShowMemoriesActivity.class);
                                intent.putExtra("TripId", currentTrip.getTripId());
                                context.startActivity(intent);
                                return true;
                        }

                        return false;
                    }
                });

            }
        });

        viewHolder.popUpMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.popUpMenuIV);
                popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.update_Trips:
                                Intent intent = new Intent(context, UpdateTrip.class);
                                intent.putExtra("Trip Id",currentTrip.getTripId());
                                intent.putExtra("Trip Name",currentTrip.getTripName());
                                intent.putExtra("Trip Description",currentTrip.getTripDescription());
                                intent.putExtra("Trip Budget",String.valueOf(currentTrip.getTripBudget()));
                                intent.putExtra("Trip Start Date",currentTrip.getTripStartDate());
                                intent.putExtra("Trip End Date",currentTrip.getTripEndDate());
                                context.startActivity(intent);
                                return true;

                            case R.id.delete_Trips:
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Delete Alert");
                                alert.setMessage("Are you sure you want to delete it?");
                                alert.setIcon(R.drawable.question);
                                alert.setCancelable(false);
                                //Positive
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserList")
                                                .child(currentUserId).child("TripList");
                                        reference.child(currentTrip.getTripId()).removeValue();
                                    }
                                });
                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                                return true;
                        }

                        return false;
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tripNameTV, tripDescriptionTV;
        private Button detailsBtn, expenseBtn, memoriesBtn;
        private ImageView popUpMenuIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tripNameTV = itemView.findViewById(R.id.tripNameTV);
            tripDescriptionTV = itemView.findViewById(R.id.tripDescriptionTV);

            detailsBtn = itemView.findViewById(R.id.detailsBtn);
            expenseBtn = itemView.findViewById(R.id.expenseBtn);
            memoriesBtn = itemView.findViewById(R.id.memoriesBtn);
            popUpMenuIV = itemView.findViewById(R.id.popUpMenuIV);
        }
    }
}
