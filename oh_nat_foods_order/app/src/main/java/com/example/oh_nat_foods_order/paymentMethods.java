package com.example.oh_nat_foods_order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class paymentMethods extends AppCompatActivity {

    private SharedPreferences shared;
    private String uid;
    private DatabaseReference methods;
    private Button addNewMethod;
    private LinearLayout paymentMethodsContainer;

    protected OnCompleteListener<DataSnapshot> onFetched = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (!task.isSuccessful()) {
                Log.e("firebase", "error reading payment methods");
            } else {
                DataSnapshot origin = task.getResult();
                for (DataSnapshot method : origin.getChildren()) {
                    // Get payment method details
                    String cardNumber = method.getKey();

                    // Inflate payment method item view
                    View paymentMethodView = LayoutInflater.from(paymentMethods.this).inflate(R.layout.payment_method_item, paymentMethodsContainer, false);
                    TextView paymentMethodText = paymentMethodView.findViewById(R.id.paymentMethodText);
                    Button deleteButton = paymentMethodView.findViewById(R.id.deletePaymentMethodButton);

                    // Set text and delete button action
                    paymentMethodText.setText("Card **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
                    deleteButton.setOnClickListener(v -> deletePaymentMethod(cardNumber));

                    paymentMethodsContainer.addView(paymentMethodView);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmethods);

        shared = getApplicationContext().getSharedPreferences("mySession", MODE_PRIVATE);
        uid = shared.getString("uid", "");

        methods = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("paymentMethod");

        paymentMethodsContainer = findViewById(R.id.paymentMethodsContainer);

        methods.get().addOnCompleteListener(onFetched);

        addNewMethod = findViewById(R.id.addPaymentMethodButton);
        addNewMethod.setOnClickListener(v -> {
            Intent toAddPayments = new Intent(paymentMethods.this, addPaymentMethod.class);
            startActivity(toAddPayments);
            finish();
        });
    }

    private void deletePaymentMethod(String cardNumber) {
        // Delete payment method from Firebase
        methods.child(cardNumber).removeValue().addOnSuccessListener(aVoid -> {
            Toast.makeText(paymentMethods.this, "Payment Method Deleted", Toast.LENGTH_SHORT).show();
            // Refresh the list
            recreate();
        }).addOnFailureListener(e -> {
            Toast.makeText(paymentMethods.this, "Failed to Delete Payment Method", Toast.LENGTH_SHORT).show();
        });
    }
}
