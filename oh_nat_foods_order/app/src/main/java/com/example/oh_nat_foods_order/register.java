package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText passwordVerify;
    private Button submit;
    private TextView login;
    private DatabaseReference emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emails = FirebaseDatabase.getInstance().getReference().child("Users");

        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        passwordVerify = findViewById(R.id.register_passwordVerify);
        submit = findViewById(R.id.register_submit);
        login = findViewById(R.id.register_login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailSubmitted = email.getText().toString();
                String passwordSubmitted = password.getText().toString();
                String passwordVerifySubmitted = passwordVerify.getText().toString();

                if (!isValidEmail(emailSubmitted)) {
                    Toast.makeText(register.this, "Invalid email format.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPassword(passwordSubmitted)) {
                    return;
                }

                if (passwordSubmitted.equals(passwordVerifySubmitted)) {
                    registerUser(emailSubmitted, passwordSubmitted);
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

    private void registerUser(String email, String password) {
        String encodedEmail = encodeEmail(email);

        emails.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(register.this, "The email you entered has already been used.", Toast.LENGTH_SHORT).show();
                } else {
                    snapshot.getRef().child("name").setValue(name.getText().toString());
                    snapshot.getRef().child("password").setValue(password);
                    snapshot.getRef().child("paymentMethods").setValue("null");
                    Toast.makeText(register.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error: " + error.getMessage());
                Toast.makeText(register.this, "Failed to register: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encodeEmail(String email) {
        return Base64.encodeToString(email.getBytes(), Base64.NO_WRAP).replace("=", "");
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

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
}
