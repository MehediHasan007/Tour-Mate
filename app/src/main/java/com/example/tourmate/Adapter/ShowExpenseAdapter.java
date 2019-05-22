package com.example.tourmate.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourmate.Activity.AddExpenseActivity;
import com.example.tourmate.Class.Expense;
import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShowExpenseAdapter extends RecyclerView.Adapter<ShowExpenseAdapter.ViewHolder> {

    private List<Expense> expenseList;
    private Context context;
    private String tripId;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String currentUserId = auth.getCurrentUser().getUid();

    public ShowExpenseAdapter( Context context,List<Expense> expenseList,String tripId) {
        this.expenseList = expenseList;
        this.context = context;
        this.tripId = tripId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_recyclerview_item,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final Expense currentExpense = expenseList.get(i);

        viewHolder.expenseTitleTV.setText(currentExpense.getExpenseTitle());
        viewHolder.expenseAmountTV.setText(String.valueOf(currentExpense.getExpenseAmount())+" Taka");

        viewHolder.deletExpenseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Alert");
                alert.setMessage("Are you sure you want to delete it?");
                alert.setIcon(R.drawable.question);
                alert.setCancelable(false);
                //Positive
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentUserId)
                                .child("TripList").child(tripId).child("ExpenseList");
                        reference.child(currentExpense.getExpenseId()).removeValue();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView expenseTitleTV,expenseAmountTV;
        private ImageView deletExpenseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expenseTitleTV = itemView.findViewById(R.id.expenseTitleTV);
            expenseAmountTV = itemView.findViewById(R.id.expenseAmountTV);
            deletExpenseIV = itemView.findViewById(R.id.deletExpenseIV);
        }
    }
}
