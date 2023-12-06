package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class myorders extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);


    }

}

class Order {
    private String orderId;
    private double itemPrice;

    public Order(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}

class ItemAdapter extends RecyclerView.Adapter<orders.ItemAdapter.ViewHolder> {
    private List<Order> itemList;

    public ItemAdapter() {
        this.itemList = new ArrayList<>();
    }

    public void setItemList(List<Order> itemList) {
        this.itemList = itemList;
    }

    public int getItemViewType(int position) {
        return position % 2 == 0 ? 0 : 1;
    }

    @NonNull
    @Override
    public orders.ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_item, parent, false);
        return new orders.ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull orders.ItemAdapter.ViewHolder holder, int position) {
        Order item = itemList.get(position);

        //bind data to views
        holder.itemNameTextView.setText(item.getOrderId());
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

    public LinearLayout createItemLayout(Context context, Order item) {
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