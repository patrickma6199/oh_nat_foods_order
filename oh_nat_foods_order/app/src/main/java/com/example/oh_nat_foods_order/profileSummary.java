package com.example.oh_nat_foods_order;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class profileSummary extends AppCompatActivity {

    private EditText name;
    private TextView username;
    private Button submit;

    //for database
    private DatabaseReference usernames;

    //for session variable
    String uid;
    SharedPreferences shared;

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

    protected final OnCompleteListener<DataSnapshot> onNewName = new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if(task.getResult().exists()) {
                usernames.child(uid).child("name").setValue(name.getText().toString());
                Toast.makeText(profileSummary.this,"Name Change Successful!",Toast.LENGTH_SHORT).show();
            } else {
                Log.e("firebase","summary: uid error");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesummary);

        //initializing shared and uid from session variable
        shared = getApplicationContext().getSharedPreferences("mySession",MODE_PRIVATE);
        uid = shared.getString("uid","");

        //initializing database reference
        usernames = FirebaseDatabase.getInstance().getReference().child("Users");

        //initializing views
        username = (TextView) findViewById(R.id.profileSum_username);
        name = (EditText) findViewById(R.id.profileSum_name);
        submit = (Button) findViewById(R.id.profileSum_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernames.child(uid).get().addOnCompleteListener(onNewName);
            }
        });

        //fetching user's information from database
        fetchUserInfo(uid);
    }

    private void fetchUserInfo(String uid) {
        usernames.child(uid).get().addOnCompleteListener(onFetched);
    }
}