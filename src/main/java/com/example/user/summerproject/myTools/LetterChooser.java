package com.example.user.summerproject.myTools;


import java.util.ArrayList;

public class LetterChooser {

    public String[] getStringArray(String string){
        char[] chars=string.toCharArray();
        String[] letters=new String[24];
        for (int i=0;i<24;i++){
            letters[i]=Character.toString(chars[i]);
        }
        return letters;
    }

    public String getSortedStringFromIntArray(int[] dist){

        String[] letters=new String[24];
        for(int i=0;i<24;i++){
            letters[i]=getLetter(i);
        }

        Quicksort quicksort=new Quicksort();
        quicksort.sort(dist,letters);

        String sortedString="";
        for (int i=0;i<24;i++){
            sortedString+=letters[i];
        }

        return sortedString;

    }

    public String chooseLetter(String sortedString1,String sortedString2){

        String[] sortedLetters1=getStringArray(sortedString1);
        String[] sortedLetters2=getStringArray(sortedString2);


        int[] indexOfLetter1InSortedLetters1=new int[24];
        int[] indexOfLetter1InSortedLetters2=new int[24];

        for(int i=0;i<24;i++){
            indexOfLetter1InSortedLetters1[i]=i;
            for(int j=0;j<24;j++){
                if(sortedLetters1[i].equals(sortedLetters2[j])){
                    indexOfLetter1InSortedLetters2[i]=j;
                    break;
                }
            }
        }

        int min=100;
        int minIndex=0;
        for(int i=0;i<24;i++){
            int sum=indexOfLetter1InSortedLetters1[i]+indexOfLetter1InSortedLetters2[i];
            if(sum<=min){
                min=sum;
                minIndex=i;
            }
        }

        String letter=sortedLetters1[minIndex];



        return letter;
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


    public int[] getIncreasedDist(ArrayList<Game> games,int[] distFromMin){
        int[] increasedDist=distFromMin;
        for(int i=0;i<games.size();i++){
            if(games.get(i) instanceof HalfGame){
                HalfGame halfGame=(HalfGame)games.get(i);
                String letter=halfGame.letter;
                for(int j=0;j<24;j++){
                    if(letter.equals(getLetter(i))){
                        increasedDist[i]++;
                        break;
                    }
                }
            }
        }
        return increasedDist;
    }

}
