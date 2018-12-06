package com.example.user.summerproject.myTools;


import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;

public class LevelHandler {
    public int getExp(CompletedGame completedGame){
        float p1=completedGame.sumPoints1;
        float p2=completedGame.sumPoints2;
        float t1=(completedGame.time1-completedGame.time1%100)/100;
        float t2=(completedGame.time2-completedGame.time2%100)/100;

        float pointFactor;
        if(p1<=60){
            pointFactor=-1+p1/20.f;
        }else {
            pointFactor=2+0.02f*(p1-60);
        }

        float difPointFactor=(1.f*p1-1.f*p2)/80.f;

        if(difPointFactor>1.f){
            difPointFactor=1.f;
        }else if(difPointFactor<-1.f){
            difPointFactor=-1.f;
        }
        float sumPointFactor=pointFactor+difPointFactor;
        if(sumPointFactor<0){
            sumPointFactor=10*sumPointFactor;
        }

        float timeFactor=1+t1/120.f;
        float diffTimeFactor=1+(t1-t2)/120.f;
        float sumTimeFactor=timeFactor+diffTimeFactor;
        int exp=0;

        if(sumPointFactor>0){

            exp=Math.round(sumPointFactor*sumTimeFactor*10);

        }else if(sumPointFactor<0){

            exp=Math.round(sumPointFactor/sumTimeFactor*10);

        }

        return exp;
    }
    public int getLevel(long playerPoints){
        int level=1;
        int roundWins=(int)(3+Math.round(Math.pow(1.11,level)));
        int totalRoundWins=roundWins;
        long totalPointsOfWins=Math.round(totalRoundWins*50);
        while(playerPoints>=totalPointsOfWins){

            level++;

            roundWins=(int)(3+Math.round(Math.pow(1.11,level)));

            totalRoundWins=totalRoundWins+roundWins;

            totalPointsOfWins=Math.round(totalRoundWins*50);
        }
        return level;
    }
    public int[] getBounds(long playerPoints){
        int[] bounds=new int[2];
        int level=1;
        int roundWins=(int)(3+Math.round(Math.pow(1.11,level)));
        int totalRoundWins=roundWins;
        long totalPointsOfWins=Math.round(totalRoundWins*50);

        if(playerPoints<totalPointsOfWins){
            bounds[0]=0;
            bounds[1]=(int)totalPointsOfWins;
        }

        while(playerPoints>=totalPointsOfWins){
            bounds[0]=(int)totalPointsOfWins;
            level++;

            roundWins=(int)(3+Math.round(Math.pow(1.11,level)));

            totalRoundWins=totalRoundWins+roundWins;

            totalPointsOfWins=Math.round(totalRoundWins*50);
            bounds[1]=(int)totalPointsOfWins;
        }
        return bounds;
    }
    public String getTag(int playerLvl){
        String tag;
        tag="";
        if(playerLvl<5){
            tag="Αρχάριος/α";
        }else if(playerLvl<10){
            tag="Μυημένος/η";
        }else if(playerLvl<15){
            tag="Επιδέξιος/α";
        }else if(playerLvl<20){
            tag="Γνώστης/στρια";
        }else if(playerLvl<30){
            tag="Έμπειρος/η";
        }else if(playerLvl<40){
            tag="Ανεμπόδιστος/η";
        }else if(playerLvl<50){
            tag="Dr.";
        }else if(playerLvl<60){
            tag="Ανθρ. Λεξικό";
        }else {
            tag="Σοφός";
        }
        return tag;
    }
}
