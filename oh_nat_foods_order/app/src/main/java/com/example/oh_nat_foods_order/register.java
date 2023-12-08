package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
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
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent toLogin = new Intent(register.this, Login.class);
                        startActivity(toLogin);
                        finish();
                    }
                }, 1500);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        usernames = FirebaseDatabase.getInstance().getReference().child("Users");

        name = findViewById(R.id.register_name);
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        passwordVerify = findViewById(R.id.register_passwordVerify);
        submit = findViewById(R.id.register_submit);
        login = findViewById(R.id.register_login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameSubmitted = username.getText().toString();
                String passwordSubmitted = password.getText().toString();
                String passwordVerifySubmitted = passwordVerify.getText().toString();

                if (!isValidPassword(passwordSubmitted)) {
                    //handled in password verification function
                    return;
                }

                if (passwordSubmitted.equals(passwordVerifySubmitted)) {
                    registerUser(usernameSubmitted);
                } else {
                    Toast.makeText(register.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registerUser(String username) {
        usernames.child(username).get().addOnCompleteListener(onUsernameFetched);
    }

    //password format checker
    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            Toast.makeText(register.this, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(register.this, "Password must contain at least one uppercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(register.this, "Password must contain at least one lowercase letter.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[@#$%^&+=].*")) {
            Toast.makeText(register.this, "Password must contain at least one special character (@#$%^&+=).", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void onBackRegister (View view) {
        Intent toHome = new Intent(register.this,MainActivity.class);
        startActivity(toHome);
        finish();
    }
}
