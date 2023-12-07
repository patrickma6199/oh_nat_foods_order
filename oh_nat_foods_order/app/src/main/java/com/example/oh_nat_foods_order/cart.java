package com.example.oh_nat_foods_order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class cart extends AppCompatActivity {

    private List<Product> items;
    private LinearLayout cartItemsContainer;
    private TextView subtotalTextView, gstTextView, totalTextView;
    private double subtotal, gst, total;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemsContainer = findViewById(R.id.cart_items_container);
        subtotalTextView = findViewById(R.id.subtotalValue);
        gstTextView = findViewById(R.id.gstValue);
        totalTextView = findViewById(R.id.totalValue);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        items = loadCartData();
        populateCart();
    }

    private void populateCart() {
        cartItemsContainer.removeAllViews();
        subtotal = 0;

        for (Product product : items) {
            productsRef.child(product.getProductName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double price = dataSnapshot.child("Price").getValue(Double.class);
                    String description = dataSnapshot.child("Description").getValue(String.class);

                    View cartItem = LayoutInflater.from(cart.this).inflate(R.layout.cart_item, cartItemsContainer, false);

                    TextView productName = cartItem.findViewById(R.id.item_order_name);
                    TextView productPrice = cartItem.findViewById(R.id.item_order_price);
                    TextView productDescription = cartItem.findViewById(R.id.item_order_description);
                    TextView itemCount = cartItem.findViewById(R.id.item_count);
                    ImageView minusIcon = cartItem.findViewById(R.id.minus_icon);
                    ImageView plusIcon = cartItem.findViewById(R.id.plus_icon);

                    productName.setText(product.getProductName());
                    productPrice.setText("$" + price);
                    productDescription.setText(description);
                    itemCount.setText(String.valueOf(product.getQuantity()));

                    minusIcon.setOnClickListener(v -> updateQuantity(product, false, price));
                    plusIcon.setOnClickListener(v -> updateQuantity(product, true, price));

                    subtotal += price * product.getQuantity();
                    updateTotals();

                    cartItemsContainer.addView(cartItem);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
    }

    private void updateQuantity(Product product, boolean increase, double price) {
        int quantity = product.getQuantity();
        if (increase) {
            quantity++;
        } else if (quantity > 1) {
            quantity--;
        } else {
            items.remove(product);
            cartItemsContainer.removeViewAt(items.indexOf(product));
            saveCartData();
            updateTotals();
            return;
        }

        product.setQuantity(quantity);
        saveCartData();
        populateCart();
    }

    private void updateTotals() {
        gst = subtotal * 0.05;
        total = subtotal + gst;

        subtotalTextView.setText(String.format("$%.2f", subtotal));
        gstTextView.setText(String.format("$%.2f", gst));
        totalTextView.setText(String.format("$%.2f", total));
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

    private void saveCartData() {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        editor.putString("myCart", json);
        editor.apply();
    }

    public void onCheckout(View view) {
        Intent intent = new Intent(this, Checkout.class);
        Gson gson = new Gson();
        String cartJson = gson.toJson(items);
        intent.putExtra("cartItems", cartJson);
        startActivity(intent);
    }

    public void onBackCart(View view) {
        Intent intent = new Intent(this,orders.class);
        startActivity(intent);
        finish();
    }
}