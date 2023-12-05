package com.example.oh_nat_foods_order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
    }

    public void onLogin(View view) {
        //should be logout button, we can implement dynamically
    }

    public void onHome(View view) {
        Toast.makeText(this,"You're already in the home orders page!", Toast.LENGTH_SHORT).show();
    }

    public void onAccount(View view) {
        Intent toSummary = new Intent(this,accountsummary.class);
        startActivity(toSummary);
        finish();
    }
}