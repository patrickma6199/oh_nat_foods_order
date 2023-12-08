package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    private SharedPreferences shared;
    private String uid;
    private DatabaseReference methods;
    private LinearLayout paymentMethodsContainer;
    private Button lastClickedSelectButton = null;

    private TextView gstValue,subtotalValue,totalValue;
    private int nextQueueNum;

    private String itemsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        gstValue = (TextView) findViewById(R.id.gstValue);
        subtotalValue = (TextView) findViewById(R.id.subtotalValue);
        totalValue = (TextView) findViewById(R.id.totalValue);

        Bundle extras = getIntent().getExtras();
        double subtotal = extras.getDouble("subtotal");
        double gst = extras.getDouble("gst");
        double total = extras.getDouble("total");
        subtotalValue.setText(String.format("$%.2f", subtotal));
        gstValue.setText(String.format("$%.2f", gst));
        totalValue.setText(String.format("$%.2f", total));

        shared = getApplicationContext().getSharedPreferences("mySession", MODE_PRIVATE);
        uid = shared.getString("uid", "");

        methods = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("paymentMethod");
        paymentMethodsContainer = findViewById(R.id.paymentMethodsContainer);

        methods.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error reading payment methods");
                } else {
                    DataSnapshot origin = task.getResult();
                    for (DataSnapshot method : origin.getChildren()) {
                        String cardNumber = method.getKey();

                        View paymentMethodView = LayoutInflater.from(Checkout.this).inflate(R.layout.payment_method_item, paymentMethodsContainer, false);
                        TextView paymentMethodText = paymentMethodView.findViewById(R.id.paymentMethodText);
                        Button selectButton = paymentMethodView.findViewById(R.id.deletePaymentMethodButton);

                        paymentMethodText.setText("Card **** **** **** " + cardNumber.substring(cardNumber.length() - 4));

                        selectButton.setText("Select");
                        selectButton.setBackgroundColor(Color.WHITE);
                        selectButton.setTextColor(Color.parseColor("#046305"));

                        selectButton.setOnClickListener(v -> {
                            if (lastClickedSelectButton != null && lastClickedSelectButton != selectButton) {
                                lastClickedSelectButton.setBackgroundColor(Color.WHITE);
                                lastClickedSelectButton.setTextColor(Color.parseColor("#046305"));

                                Toast.makeText(Checkout.this, "Success: Payment Method Selected", Toast.LENGTH_SHORT).show();

                            }
                            if (selectButton.getCurrentTextColor() == Color.WHITE) {
                                selectButton.setBackgroundColor(Color.parseColor("#046305"));
                                selectButton.setTextColor(Color.WHITE);
                                lastClickedSelectButton = selectButton;

                                Toast.makeText(Checkout.this, "Success: Payment Method Selected", Toast.LENGTH_SHORT).show();
                            } else {
                                selectButton.setBackgroundColor(Color.WHITE);
                                selectButton.setTextColor(Color.parseColor("#046305"));

                                Toast.makeText(Checkout.this, "Success: Payment Method Selected", Toast.LENGTH_SHORT).show();
                            }
                        });

                        paymentMethodsContainer.addView(paymentMethodView);
                    }
                }
            }
        });
    }

    public void onBackCart(View view) {
        Intent intent = new Intent(this,orders.class);
        startActivity(intent);
        finish();
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

    public void addOrder(View view) {
        List<Product> items = loadCartData();

        for(Product item: items) {
            itemsString = itemsString + item.getProductName() + ", ";
        }

        itemsString = itemsString.substring(4,itemsString.length()-2);

        DatabaseReference orders = FirebaseDatabase.getInstance().getReference().child("Orders");
        orders.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                nextQueueNum = task.getResult().child("nextQueue").getValue(Integer.class);
                DatabaseReference currentOrder = orders.child(uid).push();
                currentOrder.child("Queue").setValue(nextQueueNum);
                orders.child("nextQueue").setValue(nextQueueNum + 1);
                currentOrder.child("Items").setValue(itemsString);
                Toast.makeText(Checkout.this,"Order Successfully Placed!",Toast.LENGTH_SHORT).show();

                //empties the cart
                items.clear();
                SharedPreferences sharedPreferences = getSharedPreferences("CartPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(items);
                editor.putString("myCart", json);
                editor.apply();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Checkout.this,myorders.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);
            }
        });
    }
}
