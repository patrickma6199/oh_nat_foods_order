package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button submit;
    private TextView register;
    private TextView forgotPassword;

    private DatabaseReference usernames;

    protected final OnCompleteListener<DataSnapshot> onLogin = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if(task.getResult().exists() && task.getResult().child("password").getValue().toString().equals(password.getText().toString())) {
                Intent toOrder = new Intent(Login.this,orders.class);
                startActivity(toOrder);
                finish();
            } else {
                Toast.makeText(Login.this, "username/password is not valid, please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize database reference
        usernames = FirebaseDatabase.getInstance().getReference().child("Users");

        // Initialize UI elements
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        submit = findViewById(R.id.login_submit);
        register = findViewById(R.id.login_register);
        forgotPassword = findViewById(R.id.login_forgotPassword);

        //Login button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameSubmitted = username.getText().toString();
                loginUser(usernameSubmitted);
            }
        });

        //Register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser(String username) {
        usernames.child(username).get().addOnCompleteListener(onLogin);
    }
}
