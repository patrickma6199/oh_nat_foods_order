package com.example.oh_nat_foods_order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class paymentMethods extends AppCompatActivity {

    private SharedPreferences shared;
    private String uid;

    private DatabaseReference methods;

    private Button addNewMethod;

    protected OnCompleteListener<DataSnapshot> onFetched = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if(!task.isSuccessful()) {
                Log.e("firebase","error reading payment methods");
            } else {
                DataSnapshot origin = task.getResult();
                for(DataSnapshot method: origin.getChildren()) {
                    cardNumber = method.getKey();
                    cvv = method.child("CVV").getValue().toString();
                    postalCode = method.child("postalCode").getValue().toString();
                    expiryDate = method.child("expiryDate").getValue().toString();
                    country = method.child("country").getValue().toString();
                }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmethods);

        shared = getApplicationContext().getSharedPreferences("mySession",MODE_PRIVATE);
        uid = shared.getString("uid","");

        methods = FirebaseDatabase.getInstance().getReference().child(uid).child("paymentMethod");

        //can fetch user's payment info here with:
        methods.get().addOnCompleteListener(onFetched);

        addNewMethod = (Button) findViewById(R.id.addPaymentMethodButton);

        addNewMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAddPayments = new Intent(paymentMethods.this,addPaymentMethod.class);
                startActivity(toAddPayments);
                finish();
            }
        });
    }
}
