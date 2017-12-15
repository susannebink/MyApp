package com.example.susanne.myapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Choose the quiz you want to play. Select by category and difficulty (optional).
 */
public class SecondActivity extends AppCompatActivity {
    Spinner spinner;
    Spinner difficulty;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setAdapters();
        setListener();
        mAuth = FirebaseAuth.getInstance();

    }

    // Check if a user is signed in, if not go to login page
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
                    Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    // Get the selected items from the user (category and difficulty)
    public void quizSelected(View view) {
        String item = spinner.getSelectedItem().toString();
        String difficulty_chosen = difficulty.getSelectedItem().toString();
        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
        intent.putExtra("category", item);
        intent.putExtra("difficulty", difficulty_chosen);
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
        finish();
        return true;
    }

    // Set the adapters for the spinners and set the items for the spinners
    public void setAdapters(){
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        difficulty = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.difficulties, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapter2);
    }

    // Go to scoreboard
    public void scoreBoard(View view) {
        Intent intent = new Intent(SecondActivity.this, FourthActivity.class);
        startActivity(intent);
    }
}
