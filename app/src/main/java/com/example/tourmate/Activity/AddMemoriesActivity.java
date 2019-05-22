package com.example.tourmate.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tourmate.Class.Upload;
import com.example.tourmate.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddMemoriesActivity extends AppCompatActivity {

    private ImageView imageViewId;
    private EditText imageTitleET;
    private ProgressBar progressBarId;
    private Button uploadImageBtn;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private String currentUserId;
    private String tripId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memories);

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        Intent intent = getIntent();
        tripId = intent.getStringExtra("TripId");

        storageReference = FirebaseStorage.getInstance().getReference("memories");
        reference = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentUserId)
                .child("TripList").child(tripId).child("MemoryList");

        imageViewId = findViewById(R.id.imageViewId);
        imageTitleET = findViewById(R.id.imageTitleET);
        progressBarId = findViewById(R.id.progressBarId);
        uploadImageBtn = findViewById(R.id.uploadImageBtn);

        imageViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChooser();
            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(AddMemoriesActivity.this, "Upload In Progress", Toast.LENGTH_SHORT).show();
                } else {
                    UploadImageWithTitle();
                }
            }
        });
    }

    //Method
    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageViewId);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UploadImageWithTitle() {
       final String Title = imageTitleET.getText().toString();

        if(Title.equals("")){
            Toast.makeText(this, "Enter Title Field", Toast.LENGTH_SHORT).show();
        }else if(imageUri !=null){

            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBarId.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(AddMemoriesActivity.this, "Upload successfull", Toast.LENGTH_SHORT).show();

                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    Upload upload = new Upload(downloadUrl.toString(), Title);
                                    String uploadId = reference.push().getKey();
                                    upload.setUploadId(uploadId);
                                    reference.child(uploadId).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                imageTitleET.setText("");
                                                imageViewId.setImageResource(R.drawable.memories);
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AddMemoriesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBarId.setProgress((int) progress);

                        }
                    });

        }else {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

}
