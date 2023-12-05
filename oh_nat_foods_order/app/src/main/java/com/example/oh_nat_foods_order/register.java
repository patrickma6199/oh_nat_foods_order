package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    private EditText name;
    private EditText username;
    private EditText password;
    private EditText passwordVerify;
    private Button submit;
    private TextView login;
    private DatabaseReference usernames;

    protected final OnCompleteListener<DataSnapshot> onUsernameFetched = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if(task.getResult().exists()) {
                Toast.makeText(register.this, "This username is taken. Try again with another one.", Toast.LENGTH_SHORT).show();
            } else {
                usernames.child(username.getText().toString()).child("name").setValue(name.getText().toString());
                usernames.child(username.getText().toString()).child("password").setValue(password.getText().toString());
                usernames.child(username.getText().toString()).child("paymentMethod").setValue("null");
                Toast.makeText(register.this, "Account Creation Successful! Redirecting to Login.", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);     //pause for a moment to read the toast
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Intent toLogin = new Intent(register.this, Login.class);
                startActivity(toLogin);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get reference of firebase root
        usernames = FirebaseDatabase.getInstance().getReference().child("Users");

        // Initialize UI elements
        name = findViewById(R.id.register_name);
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        passwordVerify = findViewById(R.id.register_passwordVerify);
        submit = findViewById(R.id.register_submit);
        login = findViewById(R.id.register_login);

        //Register button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to perform on register button click
                String passwordSubmitted = password.getText().toString();
                String passwordVerifySubmitted = passwordVerify.getText().toString();

                if(passwordSubmitted.trim().equals(passwordVerifySubmitted.trim())){
                    try {
                        registerUser(username.getText().toString());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(register.this, "Please ensure your password matches when retyping it.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Already have an account button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registerUser(String username) throws InterruptedException {
        usernames.child(username).get().addOnCompleteListener(onUsernameFetched);
    }
}
