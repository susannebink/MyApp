package com.example.susanne.myapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class FourthActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthListener;
    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mList = findViewById(R.id.scoreBoard);
        setListener();
        getDataFromDB();
    }

    public void getDataFromDB(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Iterator<DataSnapshot> users = dataSnapshot.child("users").getChildren().iterator();

                Map<Long, String> highScoreArray = new TreeMap<>(Collections.<Long>reverseOrder());
                while(users.hasNext()){
                    DataSnapshot user = users.next();
                    String name = user.child("userName").getValue().toString();
                    Long score = (Long) user.child("highScore").getValue();

                    highScoreArray.put(score, name);
                }

                ScoreArrayAdapter arrayAdapter = new ScoreArrayAdapter(getApplicationContext(), R.layout.score_row, R.id.number, highScoreArray);
                arrayAdapter.notifyDataSetChanged();
                mList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("value_failure", "Failed to read value.", error.toException());
            }
        });
    }

    public void setListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d("signed in", "OnAuthStateChanged:signed_in" + user.getUid());
                }
                else {
                    Log.d("signed out", "OnAuthStateChanged:signed_out" );
                    Intent intent = new Intent(FourthActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

}
