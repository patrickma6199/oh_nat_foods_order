package com.example.oh_nat_foods_order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class accountsummary extends AppCompatActivity {

    SharedPreferences shared;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsummary);

        shared = getApplicationContext().getSharedPreferences("mySession",MODE_PRIVATE);
        editor = shared.edit();

        Button profileButton = findViewById(R.id.profileButton);
        Button paymentMethodsButton = findViewById(R.id.paymentMethodsButton);
        Button previousOrdersButton = findViewById(R.id.previousOrdersButton);
        Button signOutButton = findViewById(R.id.signOutButton);


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(accountsummary.this, profileSummary.class);
                startActivity(intent);
            }
        });

        paymentMethodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(accountsummary.this, paymentMethods.class);
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //clears session
                editor.clear();
                editor.commit();

                //send intent to logged out home
                Intent logout = new Intent(accountsummary.this,MainActivity.class);
                startActivity(logout);
            }
        });

        View.OnClickListener notImplementedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(accountsummary.this, "Functionality not implemented in this version", Toast.LENGTH_SHORT).show();
            }
        };

        previousOrdersButton.setOnClickListener(notImplementedListener);

    }

    public void onAccountAccount(View view) {
        // Implement navigation to Home activity
        Toast.makeText(this, "You are already in the Accounts Page!", Toast.LENGTH_SHORT).show();
    }

    public void onOrdersAccount(View view) {
        // Implement navigation to Cart activity
        Intent toMyOrders = new Intent(accountsummary.this,myorders.class);
        startActivity(toMyOrders);
        finish();
    }

    public void onHomeAccount(View view) {
        // Implement navigation to Account activity
        Intent toOrders = new Intent(accountsummary.this,orders.class);
        startActivity(toOrders);
        finish();
    }
}
