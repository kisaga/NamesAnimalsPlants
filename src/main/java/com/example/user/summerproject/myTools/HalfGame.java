package com.example.user.summerproject.myTools;



public class HalfGame extends Game {

    public int time;
    public String[] answers;
    public boolean[] isTrue;
    public String letter;
    public String player1;
    public String id1;
    public String player2;
    public String id2;
    public int level1;
    public int level2;

    public HalfGame(){}

    public HalfGame(int time,String[] answers,boolean[] isTrue,String letter,String player1,String id1,int level1,String player2,String id2,int level2){
        this.time=time;
        this.answers=answers;
        this.isTrue=isTrue;
        this.letter=letter;
        this.player1=player1;
        this.player2=player2;
        this.id1=id1;
        this.id2=id2;
        this.level1=level1;
        this.level2=level2;
    }
}
