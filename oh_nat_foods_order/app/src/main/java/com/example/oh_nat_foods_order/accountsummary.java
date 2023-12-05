package com.example.oh_nat_foods_order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class accountsummary extends AppCompatActivity {

    private TextView username,name;

    private DatabaseReference usernames;

    String uid, uname;

    protected final OnCompleteListener<DataSnapshot> onFetched = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if(task.getResult().exists()) {
                username.setText(uid);
                name.setText(task.getResult().child("name").getValue().toString());
            } else {
                Log.e("firebase","summary: uid error");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsummary);

        //getting uid from previous page
        Bundle fromLoggedIn = getIntent().getExtras();
        uid = fromLoggedIn.getString("uid");

        //initializing database reference
        usernames = FirebaseDatabase.getInstance().getReference().child("Users");

        username = (TextView) findViewById(R.id.summary_username);
        name = (TextView) findViewById(R.id.summary_name);

        //fetching user's information from database
        fetchUserInfo(uid);
    }

    private void fetchUserInfo(String uid) {
        usernames.child(uid).get().addOnCompleteListener(onFetched);
    }
}