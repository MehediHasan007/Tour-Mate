package com.example.tourmate.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText userEmailET,userPasswordET;
    private Button signInBtn;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        userEmailET = findViewById(R.id.userEmailET);
        userPasswordET = findViewById(R.id.userPasswordET);
        signInBtn = findViewById(R.id.signInBtn);

        if(auth.getCurrentUser() !=null){
            Intent intent = new Intent(MainActivity.this,AllEvents.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmailET.getText().toString();
                String password = userPasswordET.getText().toString();
                if (email.equals("") || password.equals("")){
                    Toast.makeText(MainActivity.this, "Please fill the all fields", Toast.LENGTH_SHORT).show();
                } else {
                    loginWithEmailAndPassword(email,password);
                }
            }
        });
    }

    private void loginWithEmailAndPassword(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this,AllEvents.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    userEmailET.setText("");
                    userPasswordET.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect email or password or check network",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SignUpOnClick(View view) {
        startActivity(new Intent(MainActivity.this,SignUp.class));
    }

}
