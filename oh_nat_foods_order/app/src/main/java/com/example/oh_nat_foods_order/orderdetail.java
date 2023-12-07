package com.example.oh_nat_foods_order;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class orderdetail extends AppCompatActivity {

    private ImageView productImage;
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView;
    private LinearLayout customOptionsContainer;
    private double basePrice, totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        productImage = findViewById(R.id.orderDetailsImage);
        productNameTextView = findViewById(R.id.orderDetailsTitle);
        productDescriptionTextView = findViewById(R.id.orderDetailsDescription);
        productPriceTextView = findViewById(R.id.addToCartButton);
        customOptionsContainer = findViewById(R.id.customOptionsContainer);

        // Extracting data from intent
        String productName = getIntent().getStringExtra("productName");
        basePrice = getIntent().getDoubleExtra("productPrice", 0.0);
        String productDescription = getIntent().getStringExtra("productDescription");
        String productImageUrl = getIntent().getStringExtra("productImageUrl");
        HashMap<String, Double> customizations = (HashMap<String, Double>) getIntent().getSerializableExtra("productCustomizations");

        // Set values
        Glide.with(this).load(productImageUrl).into(productImage);
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText("Add to Cart $" + basePrice);
        totalPrice = basePrice;

        // Inflate customization options
        if (customizations != null && !customizations.isEmpty()) {
            for (Map.Entry<String, Double> entry : customizations.entrySet()) {
                View customView = LayoutInflater.from(this).inflate(R.layout.custom_option_item, customOptionsContainer, false);
                TextView customName = customView.findViewById(R.id.customOptionName);
                TextView customPrice = customView.findViewById(R.id.customOptionPrice);
                CheckBox customCheckBox = customView.findViewById(R.id.customOptionCheckbox);

                customName.setText(entry.getKey());
                customPrice.setText("$" + entry.getValue());
                customCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        totalPrice += entry.getValue();
                    } else {
                        totalPrice -= entry.getValue();
                    }
                    updateTotalPrice();
                });

                customOptionsContainer.addView(customView);
            }
        }
    }

    private void updateTotalPrice() {
        productPriceTextView.setText("Add to Cart $" + String.format("%.2f", totalPrice));
    }

    // Implement the "Add to Cart" button functionality
    public void onAddToCart(View view) {
        // Add the product to the cart along with the selected customizations
        // Navigate back to the orders page or cart page as required
    }
}
