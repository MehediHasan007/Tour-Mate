package com.example.tourmate.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourmate.Class.Expense;
import com.example.tourmate.Class.Upload;
import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowMemoryAdapter extends RecyclerView.Adapter<ShowMemoryAdapter.ViewHolder> {

    private List<Upload> uploadList;
    private Context context;
    private String tripId;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String currentUserId = auth.getCurrentUser().getUid();

    public ShowMemoryAdapter(List<Upload> uploadList, Context context, String tripId) {
        this.uploadList = uploadList;
        this.context = context;
        this.tripId = tripId;
    }

    @NonNull
    @Override
    public ShowMemoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memory_recyclerview_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowMemoryAdapter.ViewHolder viewHolder, final int i) {

        final Upload currentUpload = uploadList.get(i);

        viewHolder.memoriesTitleTV.setText(currentUpload.getImageTitle());
        Picasso.with(context)
                .load(currentUpload.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(viewHolder.memoriesImageIV);

        viewHolder.cardViewId.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
                                .child("TripList").child(tripId).child("MemoryList");
                        reference.child(currentUpload.getUploadId()).removeValue();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView memoriesTitleTV;
        private ImageView memoriesImageIV;
        private CardView cardViewId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            memoriesImageIV = itemView.findViewById(R.id.memoriesImageIV);
            memoriesTitleTV = itemView.findViewById(R.id.memoriesTitleTV);
            cardViewId = itemView.findViewById(R.id.cardViewId);
        }
    }
}
