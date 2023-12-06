package com.example.oh_nat_foods_order; // Replace with your actual package name

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addPaymentMethod extends AppCompatActivity {

    private EditText cardNumberEditText, expiryDateEditText, cvvEditText, postalCodeEditText;
    private Spinner countrySpinner;
    private Button submitPaymentMethodButton;

    private DatabaseReference user;

    private SharedPreferences shared;
    private String uid;
    private String formattedCardNumber, formattedExpiryDate, formattedPostalCode;

    protected final OnCompleteListener<DataSnapshot> onFetched = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            //entering payment info to database
            if(task.getResult().child("paymentMethod").child(formattedCardNumber).exists()) {
                Toast.makeText(addPaymentMethod.this,"You already have this payment method registered.",Toast.LENGTH_SHORT).show();
            } else {
                user.child("paymentMethod").child(formattedCardNumber).child("expiryDate").setValue(formattedExpiryDate);
                user.child("paymentMethod").child(formattedCardNumber).child("CVV").setValue(cvvEditText.getText().toString());
                user.child("paymentMethod").child(formattedCardNumber).child("postalCode").setValue(formattedPostalCode);
                user.child("paymentMethod").child(formattedCardNumber).child("country").setValue(countrySpinner.getSelectedItem().toString());
                Toast.makeText(addPaymentMethod.this,"Payment Method Added Successfully!",Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Go to paymentMethods page after 1.5 seconds
                        Intent toPaymentMethods = new Intent(addPaymentMethod.this,paymentMethods.class);
                        startActivity(toPaymentMethods);
                        finish();
                    }
                }, 1500);
            }
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpaymentmethod);

        // get uid session variable
        shared = getApplicationContext().getSharedPreferences("mySession", MODE_PRIVATE);
        uid = shared.getString("uid","");

        //get firebase reference
        user = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        postalCodeEditText = findViewById(R.id.postalCodeEditText);
        countrySpinner = findViewById(R.id.countrySpinner);
        submitPaymentMethodButton = findViewById(R.id.submitPaymentMethodButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        submitPaymentMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPaymentMethod();
            }
        });
    }

    private void submitPaymentMethod() {
        String cardNumber = cardNumberEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();
        String cvv = cvvEditText.getText().toString();
        String postalCode = postalCodeEditText.getText().toString();

        if (!validateCardNumber(cardNumber)) {
            Toast.makeText(this, "Invalid Card Number", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validateExpiryDate(expiryDate)) {
            Toast.makeText(this, "Invalid Expiry Date", Toast.LENGTH_LONG).show();
            return;
        }

        if (cvv.length() != 3) {
            Toast.makeText(this, "Invalid CVV", Toast.LENGTH_LONG).show();
            return;
        }

        if (!validatePostalCode(postalCode)) {
            Toast.makeText(this, "Invalid Postal Code", Toast.LENGTH_LONG).show();
            return;
        }

        formattedCardNumber = formatCardNumber(cardNumber);
        formattedExpiryDate = formatExpiryDate(expiryDate);
        formattedPostalCode = formatPostalCode(postalCode);

        // TODO: Implement call to Firebase or other backend service with formatted data
        user.get().addOnCompleteListener(onFetched);
    }

    private boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private boolean validateExpiryDate(String expiryDate) {
        if (expiryDate.length() != 4) {
            return false;
        }

        int month;
        int year;
        try {
            month = Integer.parseInt(expiryDate.substring(0, 2));
            year = Integer.parseInt(expiryDate.substring(2, 4));
        } catch (NumberFormatException e) {
            return false;
        }

        return month >= 1 && month <= 12 && year >= 23;
    }

    private boolean validatePostalCode(String postalCode) {
        if (postalCode.length() != 6) {
            return false;
        }

        return postalCode.matches("[A-Za-z]\\d[A-Za-z]\\d[A-Za-z]\\d");
    }

    private String formatCardNumber(String cardNumber) {
        return cardNumber.replaceAll(".{4}(?!$)", "$0 ");
    }

    private String formatExpiryDate(String expiryDate) {
        return expiryDate.substring(0, 2) + "/" + expiryDate.substring(2);
    }

    private String formatPostalCode(String postalCode) {
        return (postalCode.substring(0, 3) + " " + postalCode.substring(3)).toUpperCase();
    }
}
