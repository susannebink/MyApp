package com.example.susanne.myapp;

/**
 * Created by Susanne on 15-12-2017.
 * Used to keep track of the ranking of users' highscores
 */

public class HighScore {
    public String name;
    public Long score;
    public Integer number;

    public HighScore (){}

    public HighScore(String aName, Long aScore, Integer aNumber){
        this.name = aName;
        this.score = aScore;
        this.number = aNumber;
    }

    public String getName(){
        return this.name;
    }
    public Long getHighScore(){
        return this.score;
    }
    public Integer getNumber(){
        return this.number;
    }
}
