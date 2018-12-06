package com.example.user.summerproject.myTools;



public class HalfGameFirebase1 {
    public String letter;
    public String opName;
    public int time1;
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
    public int opLevel;

    public HalfGameFirebase1(String letter,String opName,int opLevel,int time1,String[] answers,boolean[] isTrue){

        this.letter=letter;
        this.time1=time1;
        this.opName=opName;

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

        this.opLevel=opLevel;
    }

}
