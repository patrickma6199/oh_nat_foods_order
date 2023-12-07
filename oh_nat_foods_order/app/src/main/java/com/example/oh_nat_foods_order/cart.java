package com.example.oh_nat_foods_order;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class cart extends AppCompatActivity {

    private LinearLayout cartItemsContainer;
    private TextView subtotalView, gstView, totalView;
    private ArrayList<CartItem> cartItems; // CartItem is a custom class to hold cart item data
    private double subtotal = 0.0, gst = 0.0, total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemsContainer = findViewById(R.id.cart_items_container);
        subtotalView = findViewById(R.id.subtotalValue);
        gstView = findViewById(R.id.gstValue);
        totalView = findViewById(R.id.totalValue);

        // Initialize the cartItems list
        cartItems = new ArrayList<>();

        // Receive data from orderdetail.java
        receiveDataFromOrderDetail();

        // Populate the ScrollView with cart items
        //populateScrollView();

        // Calculate prices
        calculatePrices();

        // Update UI with prices
        updatePriceUI();
    }

    private void receiveDataFromOrderDetail() {
        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra("productName");
            double price = intent.getDoubleExtra("productPrice", 0.0);
            ArrayList<String> customizations = intent.getStringArrayListExtra("customizations");

            // Add the received item to the cartItems list
            cartItems.add(new CartItem(productName, price, customizations));
        }
    }


//    private void populateScrollView() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        for (CartItem item : cartItems) {
//            View cartItemView = inflater.inflate(R.layout.cart_item, cartItemsContainer, false);
//
//            ImageView productImage = cartItemView.findViewById(R.id.item_order_image);
//            TextView productName = cartItemView.findViewById(R.id.item_order_name);
//            TextView productPrice = cartItemView.findViewById(R.id.item_order_price);
//            TextView productDescription = cartItemView.findViewById(R.id.item_order_description);
//
//            // Assuming you have a method in CartItem to get a combined description including customizations
//            String description = item.getDescriptionWithCustomizations();
//
//            productName.setText(item.getProductName());
//            productPrice.setText(String.format("$%.2f", item.getPrice()));
//            productDescription.setText(description);
//
//            // Set product image if you have it. For now, using a placeholder
//            productImage.setImageResource(R.drawable.placeholder); // Replace 'placeholder' with your drawable
//
//            cartItemsContainer.addView(cartItemView);
//        }
//    }


    private void calculatePrices() {
        subtotal = 0.0;
        for (CartItem item : cartItems) {
            subtotal += item.getPrice();
        }
        gst = subtotal * 0.05;
        total = subtotal + gst;
    }

    private void updatePriceUI() {
        subtotalView.setText(String.format("$%.2f", subtotal));
        gstView.setText(String.format("$%.2f", gst));
        totalView.setText(String.format("$%.2f", total));
    }
}

// Example CartItem class
class CartItem {
    private String productName;
    private double price;
    private ArrayList<String> customizations; // Holds customization descriptions

    public CartItem(String productName, double price, ArrayList<String> customizations) {
        this.productName = productName;
        this.price = price;
        this.customizations = customizations;
    }

    public double getPrice() {
        return price;
    }

    // Additional getters and methods as needed
}
