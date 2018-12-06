package com.example.user.summerproject.myTools;


public class OldGame  {

    public boolean won;
    public int[] points;
    public String letter;
    public int time;

    public OldGame(){}

    public OldGame(boolean won, int[] points,String letter,int time){
        this.letter=letter;
        this.won=won;
        this.points=points;
        this.time=time;
    }
}
