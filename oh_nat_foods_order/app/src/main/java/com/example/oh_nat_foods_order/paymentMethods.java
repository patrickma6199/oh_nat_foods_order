package com.example.oh_nat_foods_order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
    private Button lastClickedDeleteButton = null;

    protected OnCompleteListener<DataSnapshot> onFetched = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (!task.isSuccessful()) {
                Log.e("firebase", "error reading payment methods");
            } else {
                DataSnapshot origin = task.getResult();
                for (DataSnapshot method : origin.getChildren()) {
                    String cardNumber = method.getKey();

                    View paymentMethodView = LayoutInflater.from(paymentMethods.this).inflate(R.layout.payment_method_item, paymentMethodsContainer, false);
                    TextView paymentMethodText = paymentMethodView.findViewById(R.id.paymentMethodText);
                    Button deleteButton = paymentMethodView.findViewById(R.id.deletePaymentMethodButton);

                    paymentMethodText.setText("Card **** **** **** " + cardNumber.substring(cardNumber.length() - 4));
                    deleteButton.setOnClickListener(v -> {
                        if (lastClickedDeleteButton != null && lastClickedDeleteButton != deleteButton) {
                            lastClickedDeleteButton.setText("Delete");
                            lastClickedDeleteButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, lastClickedDeleteButton.getTextSize());
                        }
                        if (deleteButton.getText().toString().equals("Confirm?")) {
                            deletePaymentMethod(cardNumber);
                        } else {
                            deleteButton.setText("Confirm?");
                            deleteButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, deleteButton.getTextSize() - getResources().getDisplayMetrics().scaledDensity);
                            lastClickedDeleteButton = deleteButton;
                        }
                    });

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
        methods.child(cardNumber).removeValue().addOnSuccessListener(aVoid -> {
            Toast.makeText(paymentMethods.this, "Payment Method Deleted", Toast.LENGTH_SHORT).show();
            recreate();
        }).addOnFailureListener(e -> {
            Toast.makeText(paymentMethods.this, "Failed to Delete Payment Method", Toast.LENGTH_SHORT).show();
        });
    }

    public void onHomePay(View view) {
        Intent toHome = new Intent(paymentMethods.this,orders.class);
        startActivity(toHome);
        finish();
    }

    public void onOrdersPay(View view) {
        Intent toMyOrders = new Intent(paymentMethods.this,myorders.class);
        startActivity(toMyOrders);
        finish();
    }

    public void onAccountPay(View view) {
        Intent toMyAccounts = new Intent(paymentMethods.this,accountsummary.class);
        startActivity(toMyAccounts);
        finish();
    }
}
