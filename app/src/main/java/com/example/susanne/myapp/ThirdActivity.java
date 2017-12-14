package com.example.susanne.myapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ThirdActivity extends AppCompatActivity {
    JSONArray quiz;
    TextView question;
    Integer index;
    TextView A;
    TextView B;
    TextView C;
    TextView D;
    TextView points_display;
    String id;
    UserData mUser;
    String url;
    String difficulty;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        difficulty = intent.getStringExtra("difficulty");
        url = getApi(category);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setListener();
        FirebaseUser user = mAuth.getCurrentUser();
        id = user.getUid();

        difficulty = getDifficulty(difficulty);
        question = findViewById(R.id.question);
        A = findViewById(R.id.A);
        B = findViewById(R.id.B);
        C = findViewById(R.id.C);
        D = findViewById(R.id.D);
        points_display = findViewById(R.id.points);
        points_display.setText("Score: ");

        getUserFromDB();
        getQuestions(url + difficulty);
    }

    public String getApi(String name){
        String url = "http://cocktail-trivia-api.herokuapp.com/api/category/";
        switch (name){
            case ("Animals"):{
                url += "animals";
                break;
            }
            case ("Board Games"):{
                url += "entertainment-board-games";
                break;
            }
            case ("Music"):{
                url += "entertainment-music";
                break;
            }
            case ("Mathematics"):{
                url += "science-mathematics";
                break;
            }
            case ("Comics"):{
                url += "entertainment-comics";
                break;
            }
            case ("General Knowledge"):{
                url += "general-knowledge";
                break;
            }
            case ("Video Games"):{
                url += "entertainment-video-games";
                break;
            }
            case ("Science and Nature"):{
                url += "science-nature";
                break;
            }
            case ("History"):{
                url += "history";
                break;
            }
            case("Film"):{
                url += "entertainment-film";
                break;
            }
        }
        return url;
    }
    public void getQuestions(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        quiz = response;
                        index = 0;
                        loadQuestion(index);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThirdActivity.this, "That didn't work", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
    public void updateHighScore(Integer points){
        Integer highScore = mUser.highScore + points;
        System.out.println("hoi");
        databaseReference.child("users").child(id).child("highScore").setValue(highScore);
        points_display.setText("Score: " + mUser.highScore);
    }

    public void loadQuestion(Integer i){
        if (index >= quiz.length()){
            Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
            startActivity(intent);
            finish();
        }
        try {
            JSONObject object = quiz.getJSONObject(i);
            question.setText(replaceChar(object.getString("text")));
            JSONArray answers = object.getJSONArray("answers");

            A.setText("A) " + replaceChar(answers.getJSONObject(0).getString("text")));
            A.setTag(answers.getJSONObject(0).getBoolean("correct"));
            B.setText("B) " + replaceChar(answers.getJSONObject(1).getString("text")));
            B.setTag(answers.getJSONObject(1).getBoolean("correct"));
            C.setText("C) " + replaceChar(answers.getJSONObject(2).getString("text")));
            C.setTag(answers.getJSONObject(2).getBoolean("correct"));
            D.setText("D) " + replaceChar(answers.getJSONObject(3).getString("text")));
            D.setTag(answers.getJSONObject(3).getBoolean("correct"));


        } catch (JSONException e){
            question.setText("Could not load question");
        }
    }

    public void goToNext(View view) {
        Boolean correct = (Boolean) view.getTag();
        if (correct){
            Toast.makeText(getApplicationContext(), "That answer was correct!", Toast.LENGTH_SHORT).show();
            updateHighScore(10);
        }
        else {
            updateHighScore(-5);
            Toast.makeText(getApplicationContext(), "That answer was not correct!", Toast.LENGTH_SHORT).show();
        }

        index += 1;
        loadQuestion(index);
    }

    public String replaceChar(String string){
        String replaceString = string.replace("&#039;", "'");
        replaceString = replaceString.replace("&quot;", "\"");
        replaceString = replaceString.replace("&euml;", "ë");
        replaceString = replaceString.replace("&amp;", "&");
        replaceString = replaceString.replace("&lsquo;", "'");
        replaceString = replaceString.replace("&rsquo;", "'");
        replaceString = replaceString.replace("&ldquo;", "\"");
        replaceString = replaceString.replace("&hellip;", "...");
        replaceString = replaceString.replace("&rdquo;", "\"");
        replaceString = replaceString.replace("&lt;", "<");
        replaceString = replaceString.replace("&gt;", ">");
        return replaceString;
    }

    public String getDifficulty(String string){
        String difficulty = "";
        if (string.equals("Easy")){
            difficulty = "/difficulty/easy";
        }
        else if (string.equals("Medium")){
            difficulty = "/difficulty/medium";
        }
        else if (string.equals("Hard")){
            difficulty = "/difficulty/hard";
        }
        return difficulty;
    }

    public void getUserFromDB(){
        Log.d("error melding", "error");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("value", "iets");
                mUser = dataSnapshot.child("users").child(id).getValue(UserData.class);
                points_display.setText("Score: " + mUser.highScore);
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
                    Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }
}
