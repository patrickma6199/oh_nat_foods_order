package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class myorders extends AppCompatActivity {

    private DatabaseReference orders;

    private SharedPreferences shared;

    private LinearLayout myOrdersContainer;
    TextView homeButton, accountButton, ordersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorders);

        orders = FirebaseDatabase.getInstance().getReference().child("Orders").child("username");

        myOrdersContainer = (LinearLayout) findViewById(R.id.myOrdersContainer);

        orders.get();

        //initializing bottom menu bar
        //home = (TextView) findViewById(R.id.home);
    }

}