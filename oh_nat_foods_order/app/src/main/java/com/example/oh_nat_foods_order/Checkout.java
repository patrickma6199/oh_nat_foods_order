package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Checkout extends AppCompatActivity {

    private SharedPreferences shared;
    private String uid;
    private DatabaseReference methods;
    private LinearLayout paymentMethodsContainer;
    private Button lastClickedSelectButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        shared = getApplicationContext().getSharedPreferences("mySession", MODE_PRIVATE);
        uid = shared.getString("uid", "");

        methods = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("paymentMethod");
        paymentMethodsContainer = findViewById(R.id.paymentMethodsContainer);

        methods.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error reading payment methods");
                } else {
                    DataSnapshot origin = task.getResult();
                    for (DataSnapshot method : origin.getChildren()) {
                        String cardNumber = method.getKey();

                        View paymentMethodView = LayoutInflater.from(Checkout.this).inflate(R.layout.payment_method_item, paymentMethodsContainer, false);
                        TextView paymentMethodText = paymentMethodView.findViewById(R.id.paymentMethodText);
                        Button selectButton = paymentMethodView.findViewById(R.id.deletePaymentMethodButton);

                        paymentMethodText.setText("Card **** **** **** " + cardNumber.substring(cardNumber.length() - 4));

                        // Change button text and appearance
                        selectButton.setText("Select");
                        selectButton.setBackgroundColor(Color.WHITE);
                        selectButton.setTextColor(Color.parseColor("#046305"));

                        selectButton.setOnClickListener(v -> {
                            if (lastClickedSelectButton != null && lastClickedSelectButton != selectButton) {
                                lastClickedSelectButton.setBackgroundColor(Color.WHITE);
                                lastClickedSelectButton.setTextColor(Color.parseColor("#046305"));

                                Toast.makeText(Checkout.this, "Success: Payment Method Selected", Toast.LENGTH_SHORT).show();

                            }
                            if (selectButton.getCurrentTextColor() == Color.WHITE) {
                                selectButton.setBackgroundColor(Color.parseColor("#046305"));
                                selectButton.setTextColor(Color.WHITE);
                                lastClickedSelectButton = selectButton;

                                Toast.makeText(Checkout.this, "Success: Payment Method Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                selectButton.setBackgroundColor(Color.WHITE);
                                selectButton.setTextColor(Color.parseColor("#046305"));

                                Toast.makeText(Checkout.this, "Success: Payment Method Selected", Toast.LENGTH_SHORT).show();
                            }
                        });

                        paymentMethodsContainer.addView(paymentMethodView);
                    }
                }
            }
        });
    }

    public void onBackCart(View view) {
        Intent intent = new Intent(this,orders.class);
        startActivity(intent);
        finish();
    }

    public void toOrders(View view) {
        Toast.makeText(Checkout.this,"Order Successfully Placed!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,myorders.class);
        startActivity(intent);
        finish();
    }


    // Additional methods and functionality for the Checkout activity
}
