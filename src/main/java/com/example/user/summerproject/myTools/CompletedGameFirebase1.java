package com.example.user.summerproject.myTools;



public class CompletedGameFirebase1 {
    public String letter;
    public String opName;
    public int opLevel;
    public String p1a1;
    public String p1a2;
    public String p1a3;
    public String p1a4;
    public String p1a5;
    public String p1a6;
    public boolean p1t1;
    public boolean p1t2;
    public boolean p1t3;
    public boolean p1t4;
    public boolean p1t5;
    public boolean p1t6;
    public String p2a1;
    public String p2a2;
    public String p2a3;
    public String p2a4;
    public String p2a5;
    public String p2a6;
    public boolean p2t1;
    public boolean p2t2;
    public boolean p2t3;
    public boolean p2t4;
    public boolean p2t5;
    public boolean p2t6;
    public int time1;
    public int time2;

    public CompletedGameFirebase1(int time,String[] answers,boolean[] isTrue,HalfGame halfGame){
        letter=halfGame.letter;
        opName=halfGame.player1;
        opLevel=halfGame.level1;

        p1a1=answers[0];
        p1a2=answers[1];
        p1a3=answers[2];
        p1a4=answers[3];
        p1a5=answers[4];
        p1a6=answers[5];

        p1t1=isTrue[0];
        p1t2=isTrue[1];
        p1t3=isTrue[2];
        p1t4=isTrue[3];
        p1t5=isTrue[4];
        p1t6=isTrue[5];

        time1=time;

        p2a1=halfGame.answers[0];
        p2a2=halfGame.answers[1];
        p2a3=halfGame.answers[2];
        p2a4=halfGame.answers[3];
        p2a5=halfGame.answers[4];
        p2a6=halfGame.answers[5];

        p2t1=halfGame.isTrue[0];
        p2t2=halfGame.isTrue[1];
        p2t3=halfGame.isTrue[2];
        p2t4=halfGame.isTrue[3];
        p2t5=halfGame.isTrue[4];
        p2t6=halfGame.isTrue[5];

        time2=halfGame.time;



    }

}
