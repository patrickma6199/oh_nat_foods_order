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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText passwordVerify;
    private Button submit;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                String nameSubmitted = name.getText().toString();
                String emailSubmitted = email.getText().toString();
                String passwordSubmitted = password.getText().toString();
                String passwordVerifySubmitted = passwordVerify.getText().toString();

                if(passwordSubmitted.trim().equals(passwordVerifySubmitted.trim())){
                    registerUser(nameSubmitted, emailSubmitted, passwordSubmitted, passwordVerifySubmitted);
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

    private void registerUser(String name, String email, String password, String passwordVerify) {
        // Implement backend here
    }
}
