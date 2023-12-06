package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myorders extends AppCompatActivity {

    private DatabaseReference orders;
    private DatabaseReference root;

    private SharedPreferences shared;
    private String uid;

    private LinearLayout myOrdersContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);

        shared = getApplication().getSharedPreferences("mySession",MODE_PRIVATE);
        uid = shared.getString("uid","");

        myOrdersContainer = findViewById(R.id.myOrdersContainer);

        root = FirebaseDatabase.getInstance().getReference();
        orders = root.child("Orders").child(uid);

        fetchOrders();
    }

    private void fetchOrders() {
        orders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOrdersContainer.removeAllViews();
                for(DataSnapshot item: snapshot.getChildren()) {
                    populateOrderView(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(myorders.this, "Error fetching orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateOrderView(DataSnapshot item) {
        View myOrderView = LayoutInflater.from(this).inflate(R.layout.orders_list_item, myOrdersContainer, false);

        TextView orderOrderId = myOrderView.findViewById(R.id.myOrdersNumText);
        TextView orderDetails = myOrderView.findViewById(R.id.myOrdersDetailsText);
        TextView orderQueueNumber = myOrderView.findViewById(R.id.myOrdersQueueText);
        TextView orderStatus = myOrderView.findViewById(R.id.myOrdersStatusText);

        String orderId = item.getKey();
        String queueNumber = item.child("Queue").getValue(String.class);
        String details = item.child("Items").getValue(String.class);

        orderOrderId.setText(orderId);
        orderQueueNumber.setText(queueNumber);
        orderDetails.setText(details);

        setupOrderStatusButton(orderStatus, queueNumber, orderId);

        myOrdersContainer.addView(myOrderView);
    }

    private void setupOrderStatusButton(TextView orderStatus, String queueNumber, String orderId) {
        if(!"0".equals(queueNumber)) {
            orderStatus.setText("CANCEL");
            orderStatus.setOnClickListener(v -> cancelOrder(orderId));
        } else {
            orderStatus.setText("PICK UP");
            orderStatus.setOnClickListener(v -> {
                // Implement pick-up logic
            });
        }
    }

    public void cancelOrder(String orderId) {
        orders.child(orderId).removeValue().addOnSuccessListener(aVoid -> {
            Toast.makeText(myorders.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
            decrementQueueNumbers(orderId);
        }).addOnFailureListener(e -> {
            Toast.makeText(myorders.this, "Failed to Cancel Order", Toast.LENGTH_SHORT).show();
        });
    }

    private void decrementQueueNumbers(String orderId) {
        DatabaseReference allOrdersRef = root.child("Orders");

        allOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through each user
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    adjustQueueForUser(userSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(myorders.this, "Error updating queue", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void adjustQueueForUser(DataSnapshot userSnapshot) {
        for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
            String queueNumber = orderSnapshot.child("Queue").getValue(String.class);
            if (queueNumber != null && !queueNumber.equals("0")) {
                int newQueueNum = Integer.parseInt(queueNumber) - 1;
                orderSnapshot.getRef().child("Queue").setValue(Integer.toString(newQueueNum));
            }
        }
    }
}
