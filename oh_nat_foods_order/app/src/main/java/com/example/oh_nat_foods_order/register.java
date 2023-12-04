package com.example.oh_nat_foods_order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {

    private ImageView logoImageView;
    private TextView titleTextView;
    private EditText nameEditText;
    private EditText registerEmailEditText;
    private EditText registerPasswordEditText;
    private Button registerButton;
    private TextView alreadyHaveAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI elements
        logoImageView = findViewById(R.id.logoImageView);
        titleTextView = findViewById(R.id.titleTextView);
        nameEditText = findViewById(R.id.nameEditText);
        registerEmailEditText = findViewById(R.id.registerEmailEditText);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        alreadyHaveAccountText = findViewById(R.id.alreadyHaveAccountText);

        //Register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to perform on register button click
                String name = nameEditText.getText().toString();
                String email = registerEmailEditText.getText().toString();
                String password = registerPasswordEditText.getText().toString();
                registerUser(name, email, password);
            }
        });
        //Already have an account button
        alreadyHaveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void registerUser(String name, String email, String password) {
        // Implement backend here
    }
}
