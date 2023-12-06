package com.example.oh_nat_foods_order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class paymentMethods extends AppCompatActivity {

    private Button addNewMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmethods);

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
