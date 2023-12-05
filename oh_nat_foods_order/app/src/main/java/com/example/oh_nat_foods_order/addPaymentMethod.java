package com.example.oh_nat_foods_order; // Replace with your actual package name

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class addPaymentMethod extends AppCompatActivity {

    private EditText cardNumberEditText, expiryDateEditText, cvvEditText, postalCodeEditText;
    private Spinner countrySpinner;
    private Button submitPaymentMethodButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpaymentmethod);

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

        String formattedCardNumber = formatCardNumber(cardNumber);
        String formattedExpiryDate = formatExpiryDate(expiryDate);
        String formattedPostalCode = formatPostalCode(postalCode);

        // TODO: Implement call to Firebase or other backend service with formatted data
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
