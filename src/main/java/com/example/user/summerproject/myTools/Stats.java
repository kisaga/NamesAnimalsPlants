package com.example.user.summerproject.myTools;


import java.util.ArrayList;

public class Stats {
    private OldGame[] oldGames;
    public int nGames[]=new int[24];
    public int distFromMin[]=new int[24];
    public int nWins[]=new int[24];
    public float[] winPerc=new float[24];
    public int[][] nPointsPerCategory=new int[6][4];
    public float[][] percPerPointPerCategory=new float[6][4];
    public int[] sumPoints=new int[6];
    public String bestLet;
    public String worstLet;
    public float bestPerc;
    public float worstPerc;
    public int bestIndex;
    public int worstIndex;

    public Stats(OldGame[] oldGames){
        this.oldGames=oldGames;
        for(int i=0;i<24;i++){
            nGames[i]=0;
            nWins[i]=0;
        }

        calcWinsGamesPerLet();
        bestPerc=0.0f;
        worstPerc=1.0f;
        calcPercPerLetMaxAndMin();
        calcDistFromMinPerLet();
        bestLet=getLetter(bestIndex);
        worstLet=getLetter(worstIndex);
        calcPercNPointsCategory();
    }


    public void calcPercNPointsCategory(){

        for(int j=0;j<6;j++) {
            nPointsPerCategory[j][0]=0;
            nPointsPerCategory[j][1]=0;
            nPointsPerCategory[j][2]=0;
            nPointsPerCategory[j][3]=0;
        }
        for(int i=0;i<oldGames.length;i++){
            for(int j=0;j<6;j++) {
                int points=oldGames[i].points[j];
                if(points==0){
                    nPointsPerCategory[j][0]++;
                    sumPoints[j]+=points;
                }else if(points==5){
                    nPointsPerCategory[j][1]++;
                    sumPoints[j]+=points;
                }else if(points==10){
                    nPointsPerCategory[j][2]++;
                    sumPoints[j]+=points;
                }else if(points==20){
                    nPointsPerCategory[j][3]++;
                    sumPoints[j]+=points;
                }
            }
        }
        for(int i=0;i<6;i++){
            for(int j=0;j<4;j++){
                percPerPointPerCategory[i][j]=100f*nPointsPerCategory[i][j]/(1.f*oldGames.length);
            }
        }

    }

    public void calcWinsGamesPerLet(){
        for(int i=0;i<oldGames.length;i++){
            switch (oldGames[i].letter){
                case "Α":
                    nGames[0]++;
                    if(oldGames[i].won){
                        nWins[0]++;
                    }
                    break;
                case "Β":
                    nGames[1]++;
                    if(oldGames[i].won){
                        nWins[1]++;
                    }
                    break;
                case "Γ":
                    nGames[2]++;
                    if(oldGames[i].won){
                        nWins[2]++;
                    }
                    break;
                case "Δ":
                    nGames[3]++;
                    if(oldGames[i].won){
                        nWins[3]++;
                    }
                    break;
                case "Ε":
                    nGames[4]++;
                    if(oldGames[i].won){
                        nWins[4]++;
                    }
                    break;
                case "Ζ":
                    nGames[5]++;
                    if(oldGames[i].won){
                        nWins[5]++;
                    }
                    break;
                case "Η":
                    nGames[6]++;
                    if(oldGames[i].won){
                        nWins[6]++;
                    }
                    break;
                case "Θ":
                    nGames[7]++;
                    if(oldGames[i].won){
                        nWins[7]++;
                    }
                    break;
                case "Ι":
                    nGames[8]++;
                    if(oldGames[i].won){
                        nWins[8]++;
                    }
                    break;
                case "Κ":
                    nGames[9]++;
                    if(oldGames[i].won){
                        nWins[9]++;
                    }
                    break;
                case "Λ":
                    nGames[10]++;
                    if(oldGames[i].won){
                        nWins[10]++;
                    }
                    break;
                case "Μ":
                    nGames[11]++;
                    if(oldGames[i].won){
                        nWins[11]++;
                    }
                    break;
                case "Ν":
                    nGames[12]++;
                    if(oldGames[i].won){
                        nWins[12]++;
                    }
                    break;
                case "Ξ":
                    nGames[13]++;
                    if(oldGames[i].won){
                        nWins[13]++;
                    }
                    break;
                case "Ο":
                    nGames[14]++;
                    if(oldGames[i].won){
                        nWins[14]++;
                    }
                    break;
                case "Π":
                    nGames[15]++;
                    if(oldGames[i].won){
                        nWins[15]++;
                    }
                    break;
                case "Ρ":
                    nGames[16]++;
                    if(oldGames[i].won){
                        nWins[16]++;
                    }
                    break;
                case "Σ":
                    nGames[17]++;
                    if(oldGames[i].won){
                        nWins[17]++;
                    }
                    break;
                case "Τ":
                    nGames[18]++;
                    if(oldGames[i].won){
                        nWins[18]++;
                    }
                    break;
                case "Υ":
                    nGames[19]++;
                    if(oldGames[i].won){
                        nWins[19]++;
                    }
                    break;
                case "Φ":
                    nGames[20]++;
                    if(oldGames[i].won){
                        nWins[20]++;
                    }
                    break;
                case "Χ":
                    nGames[21]++;
                    if(oldGames[i].won){
                        nWins[21]++;
                    }
                    break;
                case "Ψ":
                    nGames[22]++;
                    if(oldGames[i].won){
                        nWins[22]++;
                    }
                    break;
                case "Ω":
                    nGames[23]++;
                    if(oldGames[i].won){
                        nWins[23]++;
                    }
                    break;
            }
        }
    }

    public void calcDistFromMinPerLet(){
        //find man and min
        int max=-1;
        int min=2147483647;
        for(int i=0;i<24;i++){
            if(nGames[i]>max){
                max=nGames[i];
            }
            if(nGames[i]<min){
                min=nGames[i];
            }
        }
        //create distances array

        for(int i=0;i<24;i++){
            distFromMin[i]=nGames[i]-min;
        }

        // sort array by dist-make sorted indices array

    }

    private void calcPercPerLetMaxAndMin(){
        for(int i=0;i<24;i++){

            winPerc[i]=(nWins[i]*100.f)/(1.f*nGames[i]);

            if(bestPerc<winPerc[i]){
                bestPerc=winPerc[i];
                bestIndex=i;
            }

            if(worstPerc<winPerc[i]){
                worstPerc=winPerc[i];
                worstIndex=i;
            }
        }
    }


    public void calcWinsGamesPerCategory(){

    }

    public String getLetter(int index) {
        String let="";

        switch (index) {
            case 0:
                let = "Α";
                break;
            case 1:
                let = "Β";
                break;
            case 2:
                let = "Γ";
                break;
            case 3:
                let = "Δ";
                break;
            case 4:
                let ="Ε";
                break;
            case 5:
                let = "Ζ";
                break;
            case 6:
                let = "Η";
                break;
            case 7:
                let = "Θ";
                break;
            case 8:
                let = "Ι";
                break;
            case 9:
                let = "Κ";
                break;
            case 10:
                let = "Λ";
                break;
            case 11:
                let = "Μ";
                break;
            case 12:
                let = "Ν";
                break;
            case 13:
                let = "Ξ";
                break;
            case 14:
                let = "Ο";
                break;
            case 15:
                let = "Π";
                break;
            case 16:
                let = "Ρ";
                break;
            case 17:
                let = "Σ";
                break;
            case 18:
                let = "Τ";
                break;
            case 19:
                let = "Υ";
                break;
            case 20:
                let = "Φ";
                break;
            case 21:
                let = "Χ";
                break;
            case 22:
                let = "Ψ";
                break;
            case 23:
                let = "Ω";
                break;
        }
        return let;
    }

    public String getCategory(int index){
        String let="";

        switch (index) {
            case 0:
                let = "Ονόμα";
                break;
            case 1:
                let = "Ζώο";
                break;
            case 2:
                let = "Φυτό";
                break;
            case 3:
                let = "Επάγγελμα";
                break;
            case 4:
                let ="Χρώμα";
                break;
            case 5:
                let = "Χώρα";
                break;
        }
        return let;
    }




}
