package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class orders extends AppCompatActivity {

    //private ItemAdapter itemAdapter;
    private LinearLayout itemContainer;
    SharedPreferences shared;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //itemAdapter = new ItemAdapter();
        itemContainer = findViewById(R.id.ordersProductHolder);

        //retrieve items from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("items");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);

                    //update adapter with the new data
                    //itemAdapter.setItemList(itemList);
                    //add new item layout to LinearLayout
                    //LinearLayout newItemLayout = itemAdapter.createItemLayout(orders.this, item);
                    //itemContainer.addView(newItemLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //handle errors
                Log.e("FirebaseError", "Error retrieving data: " + databaseError.getMessage());
                Toast.makeText(orders.this, "Error retrieving data. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class Item {
        private String itemName;
        private double itemPrice;

        public String getItemName() {
            return itemName;
        }

        public double getItemPrice() {
            return itemPrice;
        }
    }

//    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
//        private List<Item> itemList;
//
//        public ItemAdapter(List<Item> itemList) {
//            this.itemList = itemList;
//        }
//
//        public void setItemList(List<Item> itemList) {
//            this.itemList = itemList;
//        }
//
//        public int getItemViewType(int position) {
//            return position % 2 == 0 ? 0 : 1;
//        }
//
//        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
//            super.onAttachedToRecyclerView(recyclerView);
//            // Perform setup tasks when the adapter is attached to a RecyclerView
//        }
//
//        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
//            super.onDetachedFromRecyclerView(recyclerView);
//            // Perform cleanup tasks when the adapter is detached from a RecyclerView
//        }
//
////        static class ViewHolder extends RecyclerView.ViewHolder {
////            // ... ViewHolder implementation
////        }
//        // Other methods
//
//        public LinearLayout createItemLayout(Context context, Item item) {
//            // Create a new LinearLayout for each item
//            LinearLayout itemLayout = new LinearLayout(context);
//            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
//
//            // Create ImageView for item image
//            ImageView itemImageView = new ImageView(context);
//            itemImageView.setLayoutParams(new LinearLayout.LayoutParams(
//                    100,
//                    110
//            ));
//            // Set the image resource based on your item, for example:
//            // itemImageView.setImageResource(R.drawable.your_image_resource);
//            itemLayout.addView(itemImageView);
//
//            // Create TextView for item name
//            TextView itemNameTextView = new TextView(context);
//            itemNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
//                    200,
//                    110
//            ));
//            itemNameTextView.setText(item.getItemName());
//            itemNameTextView.setTextColor(Color.parseColor("#046305"));
//            itemNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            itemNameTextView.setTypeface(null, Typeface.BOLD);
//            itemLayout.addView(itemNameTextView);
//
//            // Create TextView for item price
//            TextView itemPriceTextView = new TextView(context);
//            itemPriceTextView.setLayoutParams(new LinearLayout.LayoutParams(
//                    100,
//                    110
//            ));
//            itemPriceTextView.setText(String.valueOf(item.getItemPrice()));
//            itemPriceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            itemPriceTextView.setTypeface(null, Typeface.BOLD);
//            itemLayout.addView(itemPriceTextView);
//
//            return itemLayout;
//        }
//
//
//        // implement in activity
//    }

    public void onLogin(View view) {
        //should be logout button, we can implement dynamically
    }

    public void onHome(View view) {
        Toast.makeText(this,"You're already in the home orders page!", Toast.LENGTH_SHORT).show();
    }

    public void onAccount(View view) {
        Intent toSummary = new Intent(this,accountsummary.class);
        startActivity(toSummary);
        finish();
    }
}

