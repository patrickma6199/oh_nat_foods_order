package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class myorders extends AppCompatActivity {

    private DatabaseReference orders;
    private DatabaseReference root;

    private SharedPreferences shared;
    private String uid;

    private LinearLayout myOrdersContainer;
    TextView homeButton, accountButton, ordersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);

        shared = getApplication().getSharedPreferences("mySession",MODE_PRIVATE);
        uid = shared.getString("uid","");

        myOrdersContainer = (LinearLayout) findViewById(R.id.myOrdersContainer);

        root = FirebaseDatabase.getInstance().getReference();
        orders = root.child("Orders").child(uid);
        orders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOrdersContainer.removeAllViews();
                for(DataSnapshot item: snapshot.getChildren()) {
                    View myOrderView = LayoutInflater.from(myorders.this).inflate(R.layout.orders_list_item, myOrdersContainer, false);

                    TextView orderOrderId = (TextView) myOrderView.findViewById(R.id.myOrdersNumText);
                    TextView orderDetails = (TextView) myOrderView.findViewById(R.id.myOrdersDetailsText);
                    TextView orderQueueNumber = (TextView) myOrderView.findViewById(R.id.myOrdersQueueText);
                    TextView orderStatus = (TextView) myOrderView.findViewById(R.id.myOrdersStatusText);

                    String orderId = item.getKey();
                    String queueNumber = item.child("Queue").getValue().toString();
                    String details = item.child("Items").getValue().toString();

                    orderOrderId.setText(orderId);
                    orderQueueNumber.setText(queueNumber);
                    orderDetails.setText(details);

                    if(!queueNumber.equals("0")) {

                        // cancel button
                        orderStatus.setText("CANCEL");
                        orderStatus.setOnClickListener(v -> {
                            cancelOrder(orderId);
                        });

                    } else {

                        //pick up button
                        orderStatus.setText("PICK UP");
                        orderStatus.setOnClickListener(v -> {

                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //initializing bottom menu bar
        //home = (TextView) findViewById(R.id.home);
    }

    public void cancelOrder(String orderId) {
        orders.child(orderId).removeValue().addOnSuccessListener(aVoid -> {
            decrementQueueNumbers(orderId);
        });
    }

    private void decrementQueueNumbers(String orderId) {
        DatabaseReference allOrdersRef = root.child("Orders");

        // Retrieve the current queue numbers
        allOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through each user
                for(DataSnapshot snapshot: dataSnapshot)
                if(dataSnapshot.getKey().equals("nextQueue")) {
                    String currentNum = dataSnapshot.getValue().toString();
                    String newNum = (Integer.parseInt(currentNum) - 1) + "";
                    dataSnapshot.getRef().setValue(newNum);
                } else {
                    for (DataSnapshot queueSnapshot : dataSnapshot.getChildren()) {
                        // Iterate through each order
                        queueSnapshot.
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

}