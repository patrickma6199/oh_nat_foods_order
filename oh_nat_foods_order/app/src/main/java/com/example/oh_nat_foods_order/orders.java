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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class orders extends AppCompatActivity {

    private ItemAdapter itemAdapter;
    private LinearLayout itemContainer;
    SharedPreferences shared;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        itemAdapter = new ItemAdapter();

        RecyclerView recyclerView = findViewById(R.id.ordersProductView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        //retrieve items from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("items");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    itemList.add(item);

                    //update adapter with new data
                    itemAdapter.setItemList(itemList);

                    //add new item layout to LinearLayout
                    LinearLayout newItemLayout = itemAdapter.createItemLayout(orders.this, item);
                    itemContainer.addView(newItemLayout);
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

    public static class Item {
        private String itemName;
        private double itemPrice;

        public String getItemName() {
            return itemName;
        }

        public double getItemPrice() {
            return itemPrice;
        }
    }

    public static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private List<Item> itemList;

        public ItemAdapter() {
            this.itemList = new ArrayList<>();
        }

        public void setItemList(List<Item> itemList) {
            this.itemList = itemList;
        }

        public int getItemViewType(int position) {
            return position % 2 == 0 ? 0 : 1;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Item item = itemList.get(position);

            //bind data to views
            holder.itemNameTextView.setText(item.getItemName());
            holder.itemPriceTextView.setText(String.valueOf(item.getItemPrice()));
            //we need to handle loading images into the ImageView (poss use library like Picasso or Glide for this)
            //for now, set a placeholder image
            holder.itemImageView.setImageResource(R.drawable.quesa);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImageView;
            TextView itemNameTextView;
            TextView itemPriceTextView;

            ViewHolder(View itemView) {
                super(itemView);
                itemImageView = itemView.findViewById(R.id.ordersProductImage);
                itemNameTextView = itemView.findViewById(R.id.ordersProductNameText);
                itemPriceTextView = itemView.findViewById(R.id.ordersProductPriceText);
            }
        }

        // Other methods

        public LinearLayout createItemLayout(Context context, Item item) {
            //create new LinearLayout for each item
            LinearLayout itemLayout = new LinearLayout(context);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);

            //create ImageView for item image
            ImageView itemImageView = new ImageView(context);
            itemImageView.setLayoutParams(new LinearLayout.LayoutParams(
                    100,
                    110
            ));
            //set image resource based on item, ex: itemImageView.setImageResource(R.drawable.your_image_resource);
            itemLayout.addView(itemImageView);

            //create TextView for item name
            TextView itemNameTextView = new TextView(context);
            itemNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    200,
                    110
            ));
            itemNameTextView.setText(item.getItemName());
            itemNameTextView.setTextColor(Color.parseColor("#046305"));
            itemNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            itemNameTextView.setTypeface(null, Typeface.BOLD);

            //add to ordersProductNameHolder
            LinearLayout productNameHolder = itemLayout.findViewById(R.id.ordersProductNameHolder);
            productNameHolder.addView(itemNameTextView);

            //create TextView for item price
            TextView itemPriceTextView = new TextView(context);
            itemPriceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    100,
                    110
            ));
            itemPriceTextView.setText(String.valueOf(item.getItemPrice()));
            itemPriceTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            itemPriceTextView.setTypeface(null, Typeface.BOLD);

            //add to ordersProductPriceHolder
            LinearLayout productPriceHolder = itemLayout.findViewById(R.id.ordersProductPriceHolder);
            productPriceHolder.addView(itemPriceTextView);

            return itemLayout;
        }
    }

    public void onLogin(View view) {
        // should be logout button, we can implement dynamically
    }

    public void onHome(View view) {
        Toast.makeText(this, "You're already in the home orders page!", Toast.LENGTH_SHORT).show();
    }

    public void onAccount(View view) {
        Intent toSummary = new Intent(this, accountsummary.class);
        startActivity(toSummary);
        finish();
    }
}

