package com.example.oh_nat_foods_order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLogin() {
        Intent toLogin = new Intent(this,Login.class);
        startActivity(toLogin);
    }

    public void onRegister() {
        Intent toRegister = new Intent(this, register.class);
        startActivity(toRegister);
    }

    public void onHome() {
        Toast.makeText(this, "You are already in the Home Page!", Toast.LENGTH_SHORT).show();
    }
}