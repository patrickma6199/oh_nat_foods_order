package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class register extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText passwordVerify;
    private Button submit;
    private TextView login;
    private DatabaseReference emails;

    protected final ValueEventListener onRegister = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            // If data exists, toast currently stored value. Otherwise, set value to "Present"
            if (snapshot.exists()) {
                Toast.makeText(register.this, "The email you entered has already been used.", Toast.LENGTH_SHORT).show();
            } else {
                //write the value into database
                snapshot.getRef().child("name").setValue(name.getText().toString());
                snapshot.getRef().child("password").setValue(password.getText().toString());
                snapshot.getRef().child("paymentMethods").setValue("null");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("firebase", error.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get reference of firebase root
        emails = FirebaseDatabase.getInstance().getReference().child("Users");

        // Initialize UI elements
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        passwordVerify = findViewById(R.id.register_passwordVerify);
        submit = findViewById(R.id.register_submit);
        login = findViewById(R.id.register_login);

        //Register button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to perform on register button click
                String emailSubmitted = email.getText().toString();
                String passwordSubmitted = password.getText().toString();
                String passwordVerifySubmitted = passwordVerify.getText().toString();

                if(passwordSubmitted.trim().equals(passwordVerifySubmitted.trim())){
                    registerUser(emailSubmitted);
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

    private void registerUser(String email) {
        emails.child(email).addListenerForSingleValueEvent(onRegister);
    }
}
