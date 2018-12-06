package com.example.user.summerproject.myTools;



public class CompletedGame extends Game {
    public String letter;
    public String player1;
    public String player2;
    public int time1;
    public int time2;
    public String[] answers1;
    public String[] answers2;
    public boolean[] isTrue1;
    public boolean[] isTrue2;
    public int[] points1;
    public int[] points2;
    public int sumPoints1;
    public int sumPoints2;
    public String id1;
    public String id2;
    public int level1;
    public int level2;

    public CompletedGame(){}

    public CompletedGame(HalfGame firstHalf,HalfGame secondHalf){
        letter=firstHalf.letter;
        player1=firstHalf.player1;
        id1=firstHalf.id1;
        level1=firstHalf.level1;
        id2=secondHalf.id1;
        level2=secondHalf.level1;
        player2=secondHalf.player1;
        time1=firstHalf.time;
        time2=secondHalf.time;
        answers1=firstHalf.answers;
        answers2=secondHalf.answers;
        isTrue1=firstHalf.isTrue;
        isTrue2=secondHalf.isTrue;
        sumPoints1=0;
        sumPoints2=0;
        points1 = new int[6];
        points2 = new int[6];

        int reducedPoints = 5;
        int standardPoints = 10;
        int raisedPoints = 20;

        for (int i = 0; i <= 5; i++) {

            if (firstHalf.isTrue[i] && secondHalf.isTrue[i]) {

                if (firstHalf.answers[i].equals(secondHalf.answers[i])) {

                    points1[i] = reducedPoints;
                    points2[i] = reducedPoints;

                } else {

                    points1[i] = standardPoints;
                    points2[i] = standardPoints;

                }

            } else if (firstHalf.isTrue[i] && !secondHalf.isTrue[i]) {

                points1[i] = raisedPoints;
                points2[i] = 0;

            } else if (!firstHalf.isTrue[i] && secondHalf.isTrue[i]) {

                points1[i] = 0;
                points2[i] = raisedPoints;

            } else {

                points1[i] = 0;
                points2[i] = 0;

            }

        }
    }

    public void calculateTotalPoints(){
        sumPoints1=0;
        sumPoints2=0;
        for(int i=0;i<6;i++){
            sumPoints1+=points1[i];
            sumPoints2+=points2[i];
        }
    }



}
