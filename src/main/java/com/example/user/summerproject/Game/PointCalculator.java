package com.example.user.summerproject.Game;



public class PointCalculator {


    private String[] answers1=new String[6];
    private String[] answers2=new String[6];
    private int[] points1=new int[6];
    private int[] points2=new int[6];
    private int sumPoints1,sumPoints2;
    private int reducedPoints=5;
    private int standardPoints=10;
    private int raisedPoints=20;



    public PointCalculator(String[] answersOne,String[] answersTwo){
        this.answers1=answersOne;
        this.answers2=answersTwo;
        boolean isValid1,isValid2;

        sumPoints1=sumPoints2=0;

        for(int i=0;i<=5;i++){

            isValid1= !(answers1[i].length()<1) ;
            isValid2= !(answers2[i].length()<1) ;

            if( isValid1 && isValid2){

                if(answers1[i].equals(answers2[i])){

                    points1[i]=reducedPoints;
                    points2[i]=reducedPoints;

                }else{

                    points1[i]=standardPoints;
                    points2[i]=standardPoints;

                }

            }else if( isValid1 && !isValid2 ){

                points1[i]=raisedPoints;
                points2[i]=0;

            }else if( !isValid1 && isValid2 ){

                points1[i]=0;
                points2[i]=raisedPoints;

            }else{

                points1[i]=0;
                points2[i]=0;

            }
            sumPoints1+=points1[i];
            sumPoints2+=points2[i];
        }
    }
}
