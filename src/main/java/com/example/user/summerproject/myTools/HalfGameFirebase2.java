package com.example.user.summerproject.myTools;



public class HalfGameFirebase2 {
    public String letter;
    public String opName;
    public int time2;
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
    public boolean notificationHasSent = false;
    public int opLevel;



    public HalfGameFirebase2(String letter,String opName,int opLevel,int time,String[] answers,boolean[] isTrue){

        this.letter=letter;
        this.time2=time;
        this.opName=opName;

        p2a1=answers[0];
        p2a2=answers[1];
        p2a3=answers[2];
        p2a4=answers[3];
        p2a5=answers[4];
        p2a6=answers[5];

        p2t1=isTrue[0];
        p2t2=isTrue[1];
        p2t3=isTrue[2];
        p2t4=isTrue[3];
        p2t5=isTrue[4];
        p2t6=isTrue[5];

        this.opLevel=opLevel;
    }
}
