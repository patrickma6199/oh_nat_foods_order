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
        Button loyaltyProgramButton = findViewById(R.id.loyaltyProgramButton);
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
            }
        });

        View.OnClickListener notImplementedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(accountsummary.this, "Functionality not implemented in this version", Toast.LENGTH_SHORT).show();
            }
        };

        previousOrdersButton.setOnClickListener(notImplementedListener);
        loyaltyProgramButton.setOnClickListener(notImplementedListener);

        //Implement signout
        signOutButton.setOnClickListener(notImplementedListener);
    }
}
