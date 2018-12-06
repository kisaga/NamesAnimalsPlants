package com.example.user.summerproject.Game;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.summerproject.MainActivity;
import com.example.user.summerproject.R;
import com.example.user.summerproject.myTools.HalfGame;
import com.example.user.summerproject.myTools.LetterChooser;
import com.example.user.summerproject.myTools.LisgarTools;
import com.example.user.summerproject.myTools.NetworkChangeReceiver;
import com.example.user.summerproject.word_tester.Suggestion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


public class GameActivity extends AppCompatActivity implements HomeListener {
    public static final String preferences="myPreferences";
    public PagerAdapter myAdapter;
    public LisgarTools myTools;
    public ViewPager viewPager;
    public String[] answers=new String[6];
    public boolean[] isTrue=new boolean[6];
    public boolean[] isFull=new boolean[6];
    private Button stopButton;
    //==========================
    //=========NEW FIELDS=======
    //==========================
    public TextView popUp;
    public TextView letterView;
    public TextView timeView;
    public int time;
    public char aChar;
    private String opName;
    private String opId;
    private String playerName;
    private String playerID;
    public String myChar;
    private boolean [] opIsTrue;
    private String [] opAnswers;
    private int opTime;
    public Random rd;
    public boolean firstPlaying=true;
    public TextView[] myIndicators=new TextView[6];
    private CircularViewPagerHandler circularViewPagerHandler;
    private NetworkChangeReceiver networkChangeReceiver;


    private CountDownTimer startTimer,gameTimer,endTimer;




    private int opLvl;
    private int playerLvl;

    private ImageView rightButton;
    private ImageView leftButton;
    public boolean me ;


    private boolean clocktickingStarted=false;
    private boolean gameClosed=false;
    private MediaPlayer clockticking;
    private MediaPlayer mpCorrect;
    private DatabaseReference userFriends;
    private boolean resultSent=false;

    private boolean gameSounds;

    private Fragment[] fragments;
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentThree fragmentThree;
    private FragmentFour fragmentFour;
    private FragmentFive fragmentFive;
    private FragmentSix fragmentSix;
    private LinearLayout keyboardLayout;

    @Override
    public void onBackPressed(){
    }


    public void goLeft(View v) {
        int currItem=viewPager.getCurrentItem();

        if(currItem==0) {
            viewPager.setCurrentItem(5,true);
            String font=getResources().getString(R.string.font_name);
            Typeface mTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),font);
            myIndicators[5].setTypeface(mTypeface,Typeface.BOLD_ITALIC);
            myIndicators[0].setTypeface(mTypeface,Typeface.NORMAL);
        }else{
            viewPager.setCurrentItem(currItem -1, true);
        }
        //l;kjsdflgj;lskdfjg
    }
    public void goRight(View v){
        int currItem=viewPager.getCurrentItem();
        if(currItem==5) {
            viewPager.setCurrentItem(0,true);
            String font=getResources().getString(R.string.font_name);
            Typeface mTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),font);
            myIndicators[0].setTypeface(mTypeface,Typeface.BOLD_ITALIC);
            myIndicators[5].setTypeface(mTypeface,Typeface.NORMAL);
        }else{
            viewPager.setCurrentItem(currItem + 1, true);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_layout);
        Bundle extras=getIntent().getExtras();
        myTools = new LisgarTools(getApplicationContext());
        opId=extras.getString("opId");
        opName=extras.getString("opName");
        opLvl=extras.getInt("opLevel");
        playerName = extras.getString("playerName");
        playerID = extras.getString("playerID");
        playerLvl=extras.getInt("playerLevel");
        gameSounds=extras.getBoolean("gameSounds");


        networkChangeReceiver=new NetworkChangeReceiver();

        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkChangeReceiver,filter);

        String font=getResources().getString(R.string.font_name);
        Typeface mTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),font);




        rightButton=(ImageView) findViewById(R.id.right_button);
        rightButton.bringToFront();
        leftButton=(ImageView) findViewById(R.id.left_button);
        leftButton.bringToFront();

        keyboardLayout=(LinearLayout)findViewById(R.id.keyboard_linear);

        viewPager = (ViewPager) findViewById(R.id.main_content);
        myAdapter = new PagerAdapter(getSupportFragmentManager(),gameSounds);
        fragments=myAdapter.getFragments();
        stopButton=(Button)findViewById(R.id.stopButton);
        stopButton.setTypeface(mTypeface);
        stopButton.bringToFront();
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardLayout.setVisibility(View.GONE);
                endGame();

            }
        });
        for(int i=0;i<6;i++){
            isTrue[i]=false;
            isFull[i]=false;
            answers[i]="";
        }
        viewPager.setAdapter(myAdapter);



        circularViewPagerHandler=new CircularViewPagerHandler(viewPager,myIndicators,mTypeface,getApplicationContext());
        //==============================================================
        //========================NEW CODE==============================
        //==============================================================
        LinearLayout indicatorLayout= (LinearLayout) findViewById(R.id.indicator_layout);
        indicatorLayout.bringToFront();
        //get indicators and send them to the CircularViewPagerHandler
        myIndicators[0]=(TextView)findViewById(R.id.names_indicator);
        myIndicators[1]=(TextView)findViewById(R.id.animals_indicator);
        myIndicators[2]=(TextView)findViewById(R.id.plants_indicator);
        myIndicators[3]=(TextView)findViewById(R.id.professions_indicator);
        myIndicators[4]=(TextView)findViewById(R.id.colors_indicator);
        myIndicators[5]=(TextView)findViewById(R.id.countries_indicator);

        for(int i=0;i<6;i++){
            myIndicators[i].setTypeface(mTypeface);
            myIndicators[i].bringToFront();
            final int finalI = i;
            myIndicators[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(finalI,true);
                }
            });
        }
        viewPager.addOnPageChangeListener(circularViewPagerHandler);
        //==============================================================
        //==============================================================


        viewPager.setCurrentItem(0);



        //set the first indicator bold (new code)============
        myIndicators[0].setTypeface(mTypeface, Typeface.BOLD_ITALIC);
        //====================================================

        //==============================================================
        //========================NEW CODE==============================
        //==============================================================

        letterView=(TextView)findViewById(R.id.text_letter);
        letterView.setTypeface(mTypeface);

        myChar=extras.getString("letter");

        if(myChar!=null){

            SharedPreferences settings=getSharedPreferences(preferences,MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("aChar",myChar);
            me=false;
            opAnswers = extras.getStringArray("answers");
            opIsTrue = extras.getBooleanArray("isTrue");
            opTime=extras.getInt("opTime");
            editor.apply();
        }else{
            String playerDistString=extras.getString("playerDistString");
            String opDistString=extras.getString("opDistString");
            LetterChooser letterChooser=new LetterChooser();

            myChar=letterChooser.chooseLetter(playerDistString,opDistString);
            me=true;

            SharedPreferences settings=getSharedPreferences(preferences,MODE_PRIVATE);
            SharedPreferences.Editor editor=settings.edit();
            editor.putString("aChar",myChar);
            editor.apply();
        }


        // set time and get the textView
        //normally we get time from settings and main
        time=60;
        timeView=(TextView)findViewById(R.id.text_timer);
        timeView.setTypeface(mTypeface);
        timeView.setText( "   "+time+" : 00");
        popUp=(TextView)findViewById(R.id.StartPopUp);
        popUp.setTypeface(mTypeface);
        popUp.bringToFront();

        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        clockticking=MediaPlayer.create(this ,R.raw.clockticking5 );
        mpCorrect=MediaPlayer.create(this, R.raw.correctanswer2);
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================

        endTimer=new CountDownTimer(2500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewPager.setVisibility(View.INVISIBLE);
                rightButton.setVisibility(View.INVISIBLE);
                leftButton.setVisibility(View.INVISIBLE);
                keyboardLayout.setVisibility(View.GONE);
                if(!gameClosed) {
                    popUp.setText("Τέλος Χρόνου");
                    popUp.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                if(!gameClosed) {
                    endGame();
                }
            }
        };
        gameTimer=new CountDownTimer(time*1000,60) {
            @Override
            public void onTick(long millisUntilFinished) {

                //making string before this : (colon)
                long seconds=millisUntilFinished/1000;
                String secString=""+seconds;

                //making sting after this : (colon)
                //this string needs a zero so it can have two digits
                long centiSecs=millisUntilFinished%1000/10;
                String centiSecsString;

                if(centiSecs>=10){
                    centiSecsString=""+centiSecs;
                }else{
                    centiSecsString=" 0"+centiSecs;
                }
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
                if(seconds<10 && !clocktickingStarted && !gameClosed){
                    if(gameSounds) {
                        clockticking.start();
                        clocktickingStarted = true;
                    }
                }
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
                time=(int)(seconds*100+centiSecs);
                timeView.setText("   "+secString+" : "+centiSecsString+"");
            }

            @Override
            public void onFinish() {

                time=0;
                timeView.setText("   00 : 00");
                endTimer.start();
            }
        };
        startTimer=new CountDownTimer(2500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewPager.setVisibility(View.INVISIBLE);
                rightButton.setVisibility(View.INVISIBLE);
                leftButton.setVisibility(View.INVISIBLE);
                popUp.setText("Παίζεις με το γράμμα "+myChar);

            }

            @Override
            public void onFinish() {
                popUp.setVisibility(View.INVISIBLE);
                letterView.setText(myChar);
                viewPager.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.VISIBLE);
                leftButton.setVisibility(View.VISIBLE);
                keyboardLayout.setVisibility(View.VISIBLE);
                gameTimer.start();
            }
        };
        startTimer.start();



        //==============================================================
        //==============================================================
    }

    public void endGame(){

        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        gameClosed=true;
        clockticking.stop();
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================

        Intent intent=new Intent();
        intent.putExtra("letter",myChar);
        intent.putExtra("opId",opId);
        intent.putExtra("opName",opName);
        intent.putExtra("time",time);
        intent.putExtra("answers",answers);
        intent.putExtra("isTrue",isTrue);
        intent.putExtra("opTime",opTime);
        intent.putExtra("opIsTrue",opIsTrue);
        intent.putExtra("opAnswers",opAnswers);
        setResult(RESULT_OK,intent);

        boolean mb= isTrue[0] && isTrue[1] && isTrue[2] && isTrue[3] && isTrue[4] && isTrue[5];
        if(mb){
            if(gameSounds) {
                MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.reversepageflipping);
                pageFlipping.start();
            }
            finish();
            overridePendingTransition(R.anim.noanim,R.anim.game_fade_out);
        }else{
            showSuggestionRelative();
        }

    }

    public void showSuggestionRelative(){
        for(int i=0;i<myIndicators.length;i++){
            myIndicators[i].setVisibility(View.INVISIBLE);
        }
        rightButton.setVisibility(View.INVISIBLE);
        leftButton.setVisibility(View.INVISIBLE);
        timeView.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.INVISIBLE);
        letterView.setVisibility(View.INVISIBLE);
        popUp.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.INVISIBLE);
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.suggestion_relative);

        TextView[] textViews=new TextView[6];
        textViews[0]=(TextView)findViewById(R.id.suggestion1);
        textViews[1]=(TextView)findViewById(R.id.suggestion2);
        textViews[2]=(TextView)findViewById(R.id.suggestion3);
        textViews[3]=(TextView)findViewById(R.id.suggestion4);
        textViews[4]=(TextView)findViewById(R.id.suggestion5);
        textViews[5]=(TextView)findViewById(R.id.suggestion6);
        final CheckBox[] checkBoxes=new CheckBox[6];
        checkBoxes[0]=(CheckBox)findViewById(R.id.checkbutton1);
        checkBoxes[1]=(CheckBox)findViewById(R.id.checkbutton2);
        checkBoxes[2]=(CheckBox)findViewById(R.id.checkbutton3);
        checkBoxes[3]=(CheckBox)findViewById(R.id.checkbutton4);
        checkBoxes[4]=(CheckBox)findViewById(R.id.checkbutton5);
        checkBoxes[5]=(CheckBox)findViewById(R.id.checkbutton6);


        String font=getResources().getString(R.string.font_name);
        Typeface mTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),font);

        for (int i=0;i<6;i++){
            textViews[i].setText(answers[i]);
            textViews[i].setTypeface(mTypeface);
            if(isTrue[i] || !isFull[i]){
                checkBoxes[i].setVisibility(View.INVISIBLE);
            }
        }

        TextView textView=(TextView)findViewById(R.id.suggest_message);
        textView.setTypeface(mTypeface);

        Button button=(Button)findViewById(R.id.continue_button);
        button.setTypeface(mTypeface);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    DatabaseReference suggestionsDatabase = FirebaseDatabase.getInstance().getReference().child("Suggestions");
                    for (int i = 0; i < 6; i++) {
                        if (!isTrue[i] && checkBoxes[i].isChecked()) {
                            switch (i) {
                                case 0:
                                    Suggestion suggestion0 = new Suggestion(playerID,answers[0]);
                                    suggestionsDatabase.child("Names").push().setValue(suggestion0);
                                    break;
                                case 1:
                                    Suggestion suggestion1 = new Suggestion(playerID,answers[1]);
                                    suggestionsDatabase.child("Animals").push().setValue(suggestion1);
                                    break;
                                case 2:
                                    Suggestion suggestion2 = new Suggestion(playerID,answers[2]);
                                    suggestionsDatabase.child("Plants").push().setValue(suggestion2);
                                    break;
                                case 3:
                                    Suggestion suggestion3 = new Suggestion(playerID,answers[3]);
                                    suggestionsDatabase.child("Professions").push().setValue(suggestion3);
                                    break;
                                case 4:
                                    Suggestion suggestion4 = new Suggestion(playerID,answers[4]);
                                    suggestionsDatabase.child("Colors").push().setValue(suggestion4);
                                    break;
                                case 5:
                                    Suggestion suggestion5 = new Suggestion(playerID,answers[5]);
                                    suggestionsDatabase.child("Cities").push().setValue(suggestion5);
                                    break;
                            }
                        }
                    }
                    if (me) {

                        myTools.addSentGameFirebase(myChar, playerID, playerName, playerLvl, opId, opName, opLvl, time, answers, isTrue);
                    } else {
                        HalfGame halfGame = new HalfGame(opTime, opAnswers, opIsTrue, myChar, opName, opId, opLvl, playerName, playerID, playerLvl);
                        myTools.answerGameFirebase(playerID,playerName, opId,opName, time, answers, isTrue, halfGame);
                    }
                    resultSent = true;
                    if (gameSounds) {
                        MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.reversepageflipping);
                        pageFlipping.start();
                    }
                    finish();
                    overridePendingTransition(R.anim.noanim, R.anim.game_fade_out);
                }else {
                    Toast toast=Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }
            }
        });
        relativeLayout.setVisibility(View.VISIBLE);
    }

    public char calcutateRndChar(){
        String abc="ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
        rd=new Random();
        char letter=abc.charAt(rd.nextInt(abc.length()));
        return letter;

    }

    @Override
    public void onHomeClick(String answer,int pos,boolean isTrue,boolean isFull1) {
        answers[pos]=answer;
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        if(this.isTrue[pos]==false && isTrue==true){
            if (gameSounds) {
                mpCorrect.start();
            }
        }
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        this.isTrue[pos]=isTrue;
        this.isFull[pos]=isFull1;
        if(isFull[pos]){
            if(this.isTrue[pos]){
                int color=getResources().getColor(R.color.rightGreen);
                myIndicators[pos].setTextColor(color);
            }else{
                myIndicators[pos].setTextColor(Color.RED);
            }

        }else{
            int color=getResources().getColor(R.color.pencilColor);
            myIndicators[pos].setTextColor(color);
        }

        boolean mb= isFull[0] && isFull[1] && isFull[2] && isFull[3] && isFull[4] && isFull[5];
        if(mb){
            stopButton.setVisibility(View.VISIBLE);
        }else{
            stopButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void onDestroy() {

        clockticking.stop();
        if(!resultSent) {
            if (me) {

                myTools.addSentGameFirebase(myChar, playerID, playerName,playerLvl, opId, opName,opLvl, time, answers, isTrue);
            } else {
                HalfGame halfGame = new HalfGame(opTime, opAnswers, opIsTrue, myChar, opName, opId,opLvl, playerName, playerID,playerLvl);
                myTools.answerGameFirebase(playerID,playerName, opId,opName, time, answers, isTrue, halfGame);
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onRestart() {
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkChangeReceiver,filter);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeReceiver);
        super.onStop();
    }


    public void keyboardClick(View view){
        int current=viewPager.getCurrentItem();
        TextView textView=(TextView)view;
        String letter=textView.getText().toString();
        switch (current){
            case 0:
                fragmentOne= (FragmentOne) fragments[current];
                fragmentOne.keyboardClick(letter);
                break;
            case 1:
                fragmentTwo= (FragmentTwo) fragments[current];
                fragmentTwo.keyboardClick(letter);
                break;
            case 2:
                fragmentThree= (FragmentThree) fragments[current];
                fragmentThree.keyboardClick(letter);
                break;
            case 3:
                fragmentFour= (FragmentFour) fragments[current];
                fragmentFour.keyboardClick(letter);
                break;
            case 4:
                fragmentFive= (FragmentFive) fragments[current];
                fragmentFive.keyboardClick(letter);
                break;
            case 5:
                fragmentSix= (FragmentSix) fragments[current];
                fragmentSix.keyboardClick(letter);
                break;
        }
    }
    public void backspaceClicked(View view){
        int current=viewPager.getCurrentItem();
        switch (current){
            case 0:
                fragmentOne= (FragmentOne) fragments[current];
                fragmentOne.backspaceClicked();
                break;
            case 1:
                fragmentTwo= (FragmentTwo) fragments[current];
                fragmentTwo.backspaceClicked();
                break;
            case 2:
                fragmentThree= (FragmentThree) fragments[current];
                fragmentThree.backspaceClicked();
                break;
            case 3:
                fragmentFour= (FragmentFour) fragments[current];
                fragmentFour.backspaceClicked();
                break;
            case 4:
                fragmentFive= (FragmentFive) fragments[current];
                fragmentFive.backspaceClicked();
                break;
            case 5:
                fragmentSix= (FragmentSix) fragments[current];
                fragmentSix.backspaceClicked();
                break;
        }
    }



}
