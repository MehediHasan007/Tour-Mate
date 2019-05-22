package com.example.tourmate.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignUp extends AppCompatActivity {

    private CircleImageView imageViewId;
    private TextView userNameET, userEmailET, userPasswordET, userConfirmPasswordET;
    private Button signUpBtn;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        imageViewId = findViewById(R.id.imageViewId);
        userNameET = findViewById(R.id.userNameET);
        userEmailET = findViewById(R.id.userEmailET);
        userPasswordET = findViewById(R.id.userPasswordET);
        userConfirmPasswordET = findViewById(R.id.userConfirmPasswordET);
        signUpBtn = findViewById(R.id.signUpBtn);

        imageViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChooser();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userNameET.getText().toString();
                String email = userEmailET.getText().toString();
                String password = userPasswordET.getText().toString();
                String confirmPassword = userConfirmPasswordET.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (name.equals("")) {
                    Toast.makeText(SignUp.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(SignUp.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    Toast.makeText(SignUp.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                } else {
                    signUpWithEmailAndPassword(name,email,password);
                }
                ImageSelection(imageUri);
            }
        });
    }

    private void signUpWithEmailAndPassword(String name, String email, String password) {
        final Map<String,Object> userMap = new HashMap<>();
        userMap.put("Name",name);
        userMap.put("E-mail",email);

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    String currentUserId = auth.getCurrentUser().getUid();
                    userMap.put("userId",currentUserId);
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("UserList").child(currentUserId);
                    databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Data Save Successfull", Toast.LENGTH_SHORT).show();
                                imageViewId.setImageResource(R.drawable.ic_image);
                                userNameET.setText("");
                                userEmailET.setText("");
                                userPasswordET.setText("");
                                userConfirmPasswordET.setText("");
                            }
                        }
                    });
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

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void ImageSelection(Uri imageUri) {
        if(imageUri !=null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SignUp.this, "Image Saved", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageViewId);
        }
    }

    public void SignInOnclick(View view) {
        onBackPressed();
    }
}
