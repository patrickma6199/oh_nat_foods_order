package com.example.oh_nat_foods_order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orderdetail extends AppCompatActivity {

    private ImageView productImage;
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView;
    private LinearLayout customOptionsContainer;
    private double basePrice, totalPrice;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    private List<Product> items;

    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        shared = getApplicationContext().getSharedPreferences("mySession",MODE_PRIVATE);
        editor = shared.edit();
        items = loadCartData();

        productImage = findViewById(R.id.orderDetailsImage);
        productNameTextView = findViewById(R.id.orderDetailsTitle);
        productDescriptionTextView = findViewById(R.id.orderDetailsDescription);
        productPriceTextView = findViewById(R.id.addToCartButton);
        customOptionsContainer = findViewById(R.id.customOptionsContainer);

        productName = getIntent().getStringExtra("productName");
        basePrice = getIntent().getDoubleExtra("productPrice", 0.0);
        String productDescription = getIntent().getStringExtra("productDescription");
        String productImageUrl = getIntent().getStringExtra("productImageUrl");
        // We wanted to be able get the selected customizations from the orders page and
        // use it to calculate the total price when sent via a bundle to the cart page.
        // However, we did not have time to implement this feature in cart.
        HashMap<String, Double> customizations = (HashMap<String, Double>) getIntent().getSerializableExtra("productCustomizations");


        Glide.with(this).load(productImageUrl).into(productImage);
        productNameTextView.setText(productName);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText("Add to Cart $" + basePrice);
        totalPrice = basePrice;

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

    public void onAddToCart(View view) {

        boolean exists = false;
        for(Product p: items) {
            if(p.getProductName().equals(productName)){
                exists = true;
                p.setQuantity(p.getQuantity() + 1);
                break;
            }
        }
        if(!exists) {
            Product product = new Product(productName,1);
            items.add(product);
        }
        Toast.makeText(orderdetail.this,"Item Added to Cart",Toast.LENGTH_SHORT).show();
        saveCartData(items);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toOrders = new Intent(orderdetail.this, orders.class);
                startActivity(toOrders);
                finish();
            }
        }, 1500);
    }

    private void saveCartData(List<Product> cartItems) {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString("myCart", json);
        editor.apply();
    }

    private List<Product> loadCartData() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("myCart", null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Product>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }
}
