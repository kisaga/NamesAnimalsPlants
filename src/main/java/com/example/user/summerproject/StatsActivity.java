package com.example.user.summerproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.user.summerproject.myTools.LisgarTools;
import com.example.user.summerproject.myTools.OldGame;
import com.example.user.summerproject.myTools.Quicksort;
import com.example.user.summerproject.myTools.Stats;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private String playerName;

    private GridView distributionGridView;
    private ArrayAdapter<String> distributionArrayAdapter;
    private ArrayList<String> distributionArrayList =new ArrayList<>();
    private Stats myStats;
    private int sorted=0; //0=alphabetical 1=byLosePerInt 2=byWinPerInt
    private int sortedCategories =0;
    // sort letter by number of games
    private String[] letters=new String[24];
    private OldGame[] oldGames;
    private int[] nGames=new int[24];
    private int[] nWins=new int[24];
    private int[] nLoses=new int [24];
    private float[] winPerc=new float[24];
    private float[] losePerc=new float[24];
    private int[] winPercInt=new int[24];
    private int[] losePercInt=new int[24];


    private TextView[][] textTitles=new TextView[6][5];

    private TextView[][] textViews=new TextView[6][4];
    private int[][] percPerPointPerCategory=new int[6][4];

    private TextView[] titleTexts=new TextView[6];
    private String[] categoryNames=new String[6];

    private TextView[] sumPointsText=new TextView[6];
    private int[] sumPoints=new int[6];


    private TextView letterButton,categoryButton;
    private LinearLayout letterLayout,categoryLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Bundle extras=getIntent().getExtras();
        playerName=extras.getString("name");


        LisgarTools myTools=new LisgarTools(this);
        //addLotsOldGames();
        String FILE_NAME="oldgames.txt";
        oldGames=myTools.readOldGames();
        myStats=new Stats(oldGames);

        //construct the string of all stats
        String text="";
        for(int i=0;i<24;i++){
            text+=myStats.getLetter(i)+" "+Math.round(100*myStats.winPerc[i])/100.f+" "+Math.round(100*(100-myStats.winPerc[i]))/100.f+" \n";
        }
        TextView title=(TextView)findViewById(R.id.stats_title);
        Typeface chalkTypeface=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");
        title.setTypeface(chalkTypeface);

        TextView letterTitle=(TextView)findViewById(R.id.letter_title);
        letterTitle.setTypeface(chalkTypeface);
        TextView categoryTitle=(TextView)findViewById(R.id.category_title);
        categoryTitle.setTypeface(chalkTypeface);
        TextView letterMsg1=(TextView)findViewById(R.id.letter_message1);
        letterMsg1.setTypeface(chalkTypeface);
        TextView letterMsg2=(TextView)findViewById(R.id.letter_message2);
        letterMsg2.setTypeface(chalkTypeface);
        TextView letterMsg3=(TextView)findViewById(R.id.letter_message3);
        letterMsg3.setTypeface(chalkTypeface);


        letterButton=(TextView)findViewById(R.id.letter_button);
        letterButton.setTypeface(chalkTypeface);

        categoryButton=(TextView)findViewById(R.id.category_button);
        categoryButton.setTypeface(chalkTypeface);


        letterLayout=(LinearLayout)findViewById(R.id.letter_layout);
        categoryLayout=(LinearLayout)findViewById(R.id.category_layout);










    }


    public void showLetter(View v){
        categoryLayout.setVisibility(View.INVISIBLE);
        letterLayout.setVisibility(View.VISIBLE);
        letterButton.setBackgroundColor(getResources().getColor(R.color.transp_black40));
        categoryButton.setBackgroundColor(getResources().getColor(R.color.transp_black20));
    }
    public void showCategory(View v){
        letterLayout.setVisibility(View.INVISIBLE);
        categoryLayout.setVisibility(View.VISIBLE);
        letterButton.setBackgroundColor(getResources().getColor(R.color.transp_black20));
        categoryButton.setBackgroundColor(getResources().getColor(R.color.transp_black40));
    }


    public void sort(View v){
        sorted++;
        if(sorted>=3){
            sorted=sorted%3;
        }
        if(sorted==0){
            //alphabetical sorting
            //i got to sort the 5 arrays alphabetical
            //Arrays need to be sorted
            // 1 letters
            // 2 nWins
            // 3 nLoses
            // 4 winPercInt
            // 5 losePercInt
            myStats=new Stats(oldGames);
            distributionArrayList.clear();
            for (int i=0;i<24;i++){
                int sumCheck=0;
                letters[i]=myStats.getLetter(i);
                nGames[i]=myStats.nGames[i];
                nWins[i]=myStats.nWins[i];
                nLoses[i]=nGames[i]-nWins[i];
                winPerc[i]=myStats.winPerc[i];
                int winCheck=Math.round(winPerc[i]*100)%100;
                if (winCheck>=50){

                    sumCheck+=(100-winCheck);
                }else{
                    sumCheck-=winCheck;
                }
                winPercInt[i]=Math.round(winPerc[i]);
                losePerc[i]=100-winPerc[i];
                int loseCheck=Math.round(losePerc[i]*100)%100;
                if (winCheck>=50){

                    sumCheck+=(100-winCheck);
                }else{
                    sumCheck-=winCheck;
                }
                losePercInt[i]=Math.round(losePerc[i]);

                if(sumCheck>=100){
                    if (winCheck>=50){
                        winPerc[i]--;
                    }else {
                        losePerc[i]--;
                    }
                }else if(sumCheck<=-100){
                    if (winCheck<50){
                        winPerc[i]++;
                    }else {
                        losePerc[i]++;
                    }
                }
            }
            for (int i=0;i<24;i++){
                distributionArrayList.add(letters[i]);

                distributionArrayList.add(winPercInt[i]+"%"+" ("+nWins[i]+")");
                distributionArrayList.add(losePercInt[i]+"%"+" ("+nLoses[i]+")");

            }
            distributionArrayAdapter.notifyDataSetChanged();
        }else if(sorted==1){
            //sorting per wins
            //i got to sort the 5 array by losePercInt so that the numbers with Lots if wins come at Top
            distributionArrayList.clear();
            Quicksort quicksort=new Quicksort();
            quicksort.sort(losePercInt,letters,nWins,nLoses,winPercInt);
            for (int i=0;i<24;i++){
                distributionArrayList.add(letters[i]);
                if (Math.round(winPerc[i])+ Math.round(losePerc[i]) >100)
                {
                    distributionArrayList.add(Math.round(winPercInt[i])+"%"+" ("+nWins[i]+")");
                    distributionArrayList.add(Math.round(losePercInt[i])-1+"%"+" ("+nLoses[i]+")");
                }else {
                    distributionArrayList.add(Math.round(winPercInt[i]) + "%" + " (" + nWins[i] + ")");
                    distributionArrayList.add(Math.round(losePercInt[i]) + "%" + " (" + nLoses[i] + ")");
                }
            }
            distributionArrayAdapter.notifyDataSetChanged();
        }else if(sorted==2){
            //sorting per loses
            //i got to sort the 5 array by winPercInt so that the numbers with Lots if wins come at Top
            distributionArrayList.clear();
            Quicksort quicksort=new Quicksort();
            quicksort.sort(winPercInt,letters,nWins,nLoses,losePercInt);
            for (int i=0;i<24;i++){
                distributionArrayList.add(letters[i]);
                if (Math.round(winPerc[i])+ Math.round(losePerc[i]) >100)
                {
                    distributionArrayList.add(Math.round(winPercInt[i])+"%"+" ("+nWins[i]+")");
                    distributionArrayList.add(Math.round(losePercInt[i])-1+"%"+" ("+nLoses[i]+")");
                }else {
                    distributionArrayList.add(Math.round(winPercInt[i]) + "%" + " (" + nWins[i] + ")");
                    distributionArrayList.add(Math.round(losePercInt[i]) + "%" + " (" + nLoses[i] + ")");
                }
            }
            distributionArrayAdapter.notifyDataSetChanged();
        }
    }

    public void sortCategories(View v){
        sortedCategories++;
        if(sortedCategories >=3){
            sortedCategories=sortedCategories%3;
        }
        if(sortedCategories ==0){

            for(int i=0;i<6;i++){
                int sumCheck=0;
                for(int j=0;j<4;j++){
                    int check=Math.round(myStats.percPerPointPerCategory[i][j]*100)%100;
                    if(check>=50){
                        sumCheck+=(100-check);
                    }else {
                        sumCheck-=check;
                    }
                    percPerPointPerCategory[i][j]=Math.round(myStats.percPerPointPerCategory[i][j]);
                }
                if(sumCheck>=100){
                    percPerPointPerCategory[i][2]--;
                }else if(sumCheck<=-100){
                    percPerPointPerCategory[i][2]++;
                }
                categoryNames[i]=myStats.getCategory(i);
                sumPoints[i]=myStats.sumPoints[i];
            }
            populateCategoryViews();
        }else if(sortedCategories ==1){
            Quicksort quicksort=new Quicksort();
            int[] negativeSumPoints=new int[6];
            for (int i=0;i<6;i++){
                negativeSumPoints[i]=-1*sumPoints[i];
            }
            quicksort.sort(negativeSumPoints,categoryNames,percPerPointPerCategory);
            for (int i=0;i<6;i++){
                sumPoints[i]=-1*negativeSumPoints[i];
            }

            populateCategoryViews();
        }else if(sortedCategories ==2){
            Quicksort quicksort=new Quicksort();
            int[] negativeSumPoints=new int[6];

            quicksort.sort(sumPoints,categoryNames,percPerPointPerCategory);


            populateCategoryViews();
        }



    }




    public void getCategoryElements(){


        textViews[0][0]=(TextView)findViewById(R.id.zero_text1);
        textViews[1][0]=(TextView)findViewById(R.id.zero_text2);
        textViews[2][0]=(TextView)findViewById(R.id.zero_text3);
        textViews[3][0]=(TextView)findViewById(R.id.zero_text4);
        textViews[4][0]=(TextView)findViewById(R.id.zero_text5);
        textViews[5][0]=(TextView)findViewById(R.id.zero_text6);


        textViews[0][1]=(TextView)findViewById(R.id.five_text1);
        textViews[1][1]=(TextView)findViewById(R.id.five_text2);
        textViews[2][1]=(TextView)findViewById(R.id.five_text3);
        textViews[3][1]=(TextView)findViewById(R.id.five_text4);
        textViews[4][1]=(TextView)findViewById(R.id.five_text5);
        textViews[5][1]=(TextView)findViewById(R.id.five_text6);

        textViews[0][2]=(TextView)findViewById(R.id.ten_text1);
        textViews[1][2]=(TextView)findViewById(R.id.ten_text2);
        textViews[2][2]=(TextView)findViewById(R.id.ten_text3);
        textViews[3][2]=(TextView)findViewById(R.id.ten_text4);
        textViews[4][2]=(TextView)findViewById(R.id.ten_text5);
        textViews[5][2]=(TextView)findViewById(R.id.ten_text6);

        textViews[0][3]=(TextView)findViewById(R.id.twenty_text1);
        textViews[1][3]=(TextView)findViewById(R.id.twenty_text2);
        textViews[2][3]=(TextView)findViewById(R.id.twenty_text3);
        textViews[3][3]=(TextView)findViewById(R.id.twenty_text4);
        textViews[4][3]=(TextView)findViewById(R.id.twenty_text5);
        textViews[5][3]=(TextView)findViewById(R.id.twenty_text6);


        textTitles[0][0]=(TextView)findViewById(R.id.zero_title1);
        textTitles[1][0]=(TextView)findViewById(R.id.zero_title2);
        textTitles[2][0]=(TextView)findViewById(R.id.zero_title3);
        textTitles[3][0]=(TextView)findViewById(R.id.zero_title4);
        textTitles[4][0]=(TextView)findViewById(R.id.zero_title5);
        textTitles[5][0]=(TextView)findViewById(R.id.zero_title6);


        textTitles[0][1]=(TextView)findViewById(R.id.five_title1);
        textTitles[1][1]=(TextView)findViewById(R.id.five_title2);
        textTitles[2][1]=(TextView)findViewById(R.id.five_title3);
        textTitles[3][1]=(TextView)findViewById(R.id.five_title4);
        textTitles[4][1]=(TextView)findViewById(R.id.five_title5);
        textTitles[5][1]=(TextView)findViewById(R.id.five_title6);

        textTitles[0][2]=(TextView)findViewById(R.id.ten_title1);
        textTitles[1][2]=(TextView)findViewById(R.id.ten_title2);
        textTitles[2][2]=(TextView)findViewById(R.id.ten_title3);
        textTitles[3][2]=(TextView)findViewById(R.id.ten_title4);
        textTitles[4][2]=(TextView)findViewById(R.id.ten_title5);
        textTitles[5][2]=(TextView)findViewById(R.id.ten_title6);

        textTitles[0][3]=(TextView)findViewById(R.id.twenty_title1);
        textTitles[1][3]=(TextView)findViewById(R.id.twenty_title2);
        textTitles[2][3]=(TextView)findViewById(R.id.twenty_title3);
        textTitles[3][3]=(TextView)findViewById(R.id.twenty_title4);
        textTitles[4][3]=(TextView)findViewById(R.id.twenty_title5);
        textTitles[5][3]=(TextView)findViewById(R.id.twenty_title6);


        textTitles[0][4]=(TextView)findViewById(R.id.sum_points_title1);
        textTitles[1][4]=(TextView)findViewById(R.id.sum_points_title2);
        textTitles[2][4]=(TextView)findViewById(R.id.sum_points_title3);
        textTitles[3][4]=(TextView)findViewById(R.id.sum_points_title4);
        textTitles[4][4]=(TextView)findViewById(R.id.sum_points_title5);
        textTitles[5][4]=(TextView)findViewById(R.id.sum_points_title6);


        titleTexts[0]=(TextView)findViewById(R.id.category_title1);
        titleTexts[1]=(TextView)findViewById(R.id.category_title2);
        titleTexts[2]=(TextView)findViewById(R.id.category_title3);
        titleTexts[3]=(TextView)findViewById(R.id.category_title4);
        titleTexts[4]=(TextView)findViewById(R.id.category_title5);
        titleTexts[5]=(TextView)findViewById(R.id.category_title6);


        sumPointsText[0]=(TextView)findViewById(R.id.sum_points_text1);
        sumPointsText[1]=(TextView)findViewById(R.id.sum_points_text2);
        sumPointsText[2]=(TextView)findViewById(R.id.sum_points_text3);
        sumPointsText[3]=(TextView)findViewById(R.id.sum_points_text4);
        sumPointsText[4]=(TextView)findViewById(R.id.sum_points_text5);
        sumPointsText[5]=(TextView)findViewById(R.id.sum_points_text6);


        Typeface chalkTypeface=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");

        for(int i=0;i<6;i++){
            titleTexts[i].setTypeface(chalkTypeface);
            for(int j=0;j<4;j++){
                textViews[i][j].setTypeface(chalkTypeface);
                textTitles[i][j].setTypeface(chalkTypeface);
            }
            textTitles[i][4].setTypeface(chalkTypeface);
            sumPointsText[i].setTypeface(chalkTypeface);
        }

    }

    public void populateCategoryViews(){

        for(int i=0;i<6;i++){
            titleTexts[i].setText(categoryNames[i]+"");
            for(int j=0;j<4;j++){
                textViews[i][j].setText(percPerPointPerCategory[i][j]+"%");
            }
            sumPointsText[i].setText(sumPoints[i]+"");
        }
    }

    @Override
    public void onBackPressed(){

        finish();
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    public void backClicked(View v){
        finish();
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }


    @Override
    protected void onStart() {
        super.onStart();


        for (int i=0;i<24;i++){
            int sumCheck=0;
            letters[i]=myStats.getLetter(i);
            nGames[i]=myStats.nGames[i];
            nWins[i]=myStats.nWins[i];
            nLoses[i]=nGames[i]-nWins[i];
            winPerc[i]=myStats.winPerc[i];
            int winCheck=Math.round(winPerc[i]*100)%100;
            if (winCheck>=50){

                sumCheck+=(100-winCheck);
            }else{
                sumCheck-=winCheck;
            }
            winPercInt[i]=Math.round(winPerc[i]);
            losePerc[i]=100-winPerc[i];
            int loseCheck=Math.round(losePerc[i]*100)%100;
            if (winCheck>=50){

                sumCheck+=(100-winCheck);
            }else{
                sumCheck-=winCheck;
            }
            losePercInt[i]=Math.round(losePerc[i]);

            if(sumCheck>=100){
                if (winCheck>=50){
                    winPerc[i]--;
                }else {
                    losePerc[i]--;
                }
            }else if(sumCheck<=-100){
                if (winCheck<50){
                    winPerc[i]++;
                }else {
                    losePerc[i]++;
                }
            }

        }

        //Quicksort quicksort=new Quicksort();
        //quicksort.sort(nGames,letters);




        //  populate distributionArrayList with nGames

        for (int i=0;i<24;i++){
            if((nWins[i]+nLoses[i])!=0) {
                distributionArrayList.add(letters[i]);

                distributionArrayList.add(winPercInt[i] + "%" + " (" + nWins[i] + ")");
                distributionArrayList.add(losePercInt[i] + "%" + " (" + nLoses[i] + ")");
            }
        }

        distributionGridView=(GridView)findViewById(R.id.letter_grid_view);
        distributionArrayAdapter=new ArrayAdapter<>(this,R.layout.stats_text_view, distributionArrayList);
        distributionGridView.setAdapter( distributionArrayAdapter);
        distributionArrayAdapter.notifyDataSetChanged();




        //  populate distributionArrayList with nGames
        for(int i=0;i<6;i++){
            int sumCheck=0;
            for(int j=0;j<4;j++){
                int check=Math.round(myStats.percPerPointPerCategory[i][j]*100)%100;
                if(check>=50){
                    sumCheck+=(100-check);
                }else {
                    sumCheck-=check;
                }
                percPerPointPerCategory[i][j]=Math.round(myStats.percPerPointPerCategory[i][j]);
            }
            if(sumCheck>=100){
                percPerPointPerCategory[i][2]--;
            }else if(sumCheck<=-100){
                percPerPointPerCategory[i][2]++;
            }
            categoryNames[i]=myStats.getCategory(i);
            sumPoints[i]=myStats.sumPoints[i];
        }



        getCategoryElements();
        populateCategoryViews();


    }
}
