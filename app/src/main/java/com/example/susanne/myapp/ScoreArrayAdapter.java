package com.example.susanne.myapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Susanne on 14-12-2017.
 * Custom array adapter for the scoreboard
 */

public class ScoreArrayAdapter extends ArrayAdapter {

    private ArrayList<HighScore> myScoreList = new ArrayList<>();
    Context context1;

    public ScoreArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, Map<Long, String> map) {
        super(context, resource, textViewResourceId);
        this.context1 = context;

        // Iterate over the map and store the information in an arraylist
        Integer i = 1;
        for (Map.Entry<Long, String> entry : map.entrySet()) {
            Long highScore = entry.getKey();
            String userName = entry.getValue();

            HighScore newScore = new HighScore(userName, highScore, i);
            myScoreList.add(newScore);
            i++;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.score_row, null);
        }
        
        // Get user's name, score and rank from the arraylist
        String myName = myScoreList.get(position).getName();
        Long myScore = myScoreList.get(position).getHighScore();
        Integer myNumber = myScoreList.get(position).getNumber();

        TextView number = convertView.findViewById(R.id.number);
        TextView user = convertView.findViewById(R.id.user);
        TextView score = convertView.findViewById(R.id.score);

        // Set the texts of the textviews with the data from the arraylist
        score.setText(myScore.toString());
        user.setText(myName);
        number.setText(myNumber.toString()+".");
        return convertView;
    }

    @Override
    public int getCount(){
        return myScoreList.size();
    }

}
