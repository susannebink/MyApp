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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Log in or sign in user and add user's information to the database.
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String email;
    String password;
    FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        setListener();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
    // Check if user is already signed in
    public void setListener(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d("signed in", "OnAuthStateChanged:signed_in" + user.getUid());
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("signed out", "OnAuthStateChanged:signed_out" );
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    // Function for the log in of a user, validates email and password
    public void signIn() {
        EditText get_email = findViewById(R.id.editText);
        EditText get_password = findViewById(R.id.editText2);

        email = get_email.getText().toString();
        password = get_password.getText().toString();
        if (email.equals("")){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("sign in success", "signInWithEmail:success");
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("sign in fail", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //                            updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

    // Function for signing up a user, validates the email
    public void signUp(){
        EditText get_email = findViewById(R.id.editText);
        EditText get_password = findViewById(R.id.editText2);

        email = get_email.getText().toString();
        password = get_password.getText().toString();

        // Check if email and password aren't blank and checks if password is longer than six characters
        if (email.equals("")){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6){
            Toast.makeText(this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
        }

        // Sign user up is everything was correct
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("create user", "createUserWithEmail:success");
                                addUser(email, password);
                                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("create user", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }

    // On click for sign in button
    public void onClickSignIn(View view) {
        signIn();
    }

    // On click for sign up button
    public void onClickSignUp(View view) {
        signUp();
    }

    // Add the user to the firebase database
    public void addUser(String email, String password){
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();
        UserData nUser = new UserData(email, password, 0);
        databaseReference.child("users").child(id).setValue(nUser);
    }

}
