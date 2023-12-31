package com.example.oh_nat_foods_order;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class orders extends AppCompatActivity {

    private DatabaseReference productsRef;
    private LinearLayout itemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        itemsContainer = findViewById(R.id.order_items_container);

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemsContainer.removeAllViews();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    View itemView = LayoutInflater.from(orders.this).inflate(R.layout.item_order, itemsContainer, false);
                    TextView productName = itemView.findViewById(R.id.item_order_name);
                    TextView productPrice = itemView.findViewById(R.id.item_order_price);
                    TextView productDescription = itemView.findViewById(R.id.item_order_description);
                    ImageView productImage = itemView.findViewById(R.id.item_order_image);

                    String name = productSnapshot.getKey();
                    Double price = productSnapshot.child("Price").getValue(Double.class);
                    String description = productSnapshot.child("Description").getValue(String.class);
                    String imageUrl = productSnapshot.child("URL").getValue(String.class);

                    DataSnapshot customizationsSnapshot = productSnapshot.child("Custom");
                    HashMap<String, Double> customizations = new HashMap<>();
                    for (DataSnapshot customOption : customizationsSnapshot.getChildren()) {
                        customizations.put(customOption.getKey(), customOption.getValue(Double.class));
                    }

                    productName.setText(name);
                    productPrice.setText("$" + price);
                    productDescription.setText(description);
                    Glide.with(orders.this).load(imageUrl).into(productImage);

                    itemView.setOnClickListener(v -> openOrderDetails(name, price, description, imageUrl, customizations));
                    itemsContainer.addView(itemView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("OrdersActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(orders.this, "Failed to load data. Please check your connection and try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onHomeOrder(View view) {
        Toast.makeText(this, "You are already in the Home Page!", Toast.LENGTH_SHORT).show();
    }

    public void onOrdersOrder(View view) {
        Intent toMyOrders = new Intent(orders.this,myorders.class);
        startActivity(toMyOrders);
        finish();
    }

    public void onAccountOrder(View view) {
        Intent toMyAccounts = new Intent(orders.this,accountsummary.class);
        startActivity(toMyAccounts);
        finish();
    }

    private void openOrderDetails(String name, Double price, String description, String imageUrl, HashMap<String, Double> customizations) {
        Intent intent = new Intent(this, orderdetail.class);
        intent.putExtra("productName", name);
        intent.putExtra("productPrice", price);
        intent.putExtra("productDescription", description);
        intent.putExtra("productImageUrl", imageUrl);
        intent.putExtra("productCustomizations", customizations);
        startActivity(intent);
    }

    public void onCartButton(View view) {
        Intent toCart = new Intent(orders.this,cart.class);
        startActivity(toCart);
        finish();
    }
}
