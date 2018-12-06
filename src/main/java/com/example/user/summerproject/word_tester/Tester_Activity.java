package com.example.user.summerproject.word_tester;

import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.summerproject.Game.FragmentFive;
import com.example.user.summerproject.R;
import com.example.user.summerproject.myTools.NetworkChangeReceiver;


public class Tester_Activity extends AppCompatActivity {
    private ViewPager viewPager;
    private boolean sounds;
    private String mID;
    private NetworkChangeReceiver networkChangeReceiver;
    private Fragment[] fragments;
    private FragmentOneTester fragmentOneTester;
    private FragmentTwoTester fragmentTwoTester;
    private FragmentThreeTester fragmentThreeTester;
    private FragmentFourTester fragmentFourTester;
    private FragmentFiveTester fragmentFiveTester;
    private FragmentSixTester fragmentSixTester;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tester_content);
        Bundle extras=getIntent().getExtras();
        sounds=extras.getBoolean("sounds");
        mID = extras.getString("mID");
        viewPager = (ViewPager) findViewById(R.id.testerContent);
        final PagerAdapterTester myAdapter = new PagerAdapterTester(getSupportFragmentManager(),sounds,mID);
        fragments=myAdapter.getFragments();
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new CircularViewPagerHandlerTester(viewPager));
        viewPager.setCurrentItem(0);





     }

    public void backClicked(View v){

        if(sounds) {
            MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.reversepageflipping);
            pageFlipping.start();
        }
        finish();
        overridePendingTransition(R.anim.noanim,R.anim.fade_out);
    }
    public void goLeft(View v) {
        int currItem=viewPager.getCurrentItem();

        if(currItem==0) {
            viewPager.setCurrentItem(5,true);

        }else{
            viewPager.setCurrentItem(currItem -1, true);
        }
        //l;kjsdflgj;lskdfjg
    }
    public void goRight(View v){
        int currItem=viewPager.getCurrentItem();
        if(currItem==5) {
            viewPager.setCurrentItem(0,true);

        }else{
            viewPager.setCurrentItem(currItem + 1, true);
        }
        //asdfasdfasdf
        ///asdfasdfasdf
        //asdfasdfasdf
    }

    @Override
    public void onBackPressed() {

        if(sounds) {
            MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.reversepageflipping);
            pageFlipping.start();
        }
        finish();
        overridePendingTransition(R.anim.noanim,R.anim.fade_out);

    }


    @Override
    protected void onStart() {
        super.onStart();
        networkChangeReceiver=new NetworkChangeReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkChangeReceiver,filter);
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
                fragmentOneTester= (FragmentOneTester) fragments[current];
                fragmentOneTester.keyboardClick(letter);
                break;
            case 1:
                fragmentTwoTester= (FragmentTwoTester) fragments[current];
                fragmentTwoTester.keyboardClick(letter);
                break;
            case 2:
                fragmentThreeTester= (FragmentThreeTester) fragments[current];
                fragmentThreeTester.keyboardClick(letter);
                break;
            case 3:
                fragmentFourTester= (FragmentFourTester) fragments[current];
                fragmentFourTester.keyboardClick(letter);
                break;
            case 4:
                fragmentFiveTester= (FragmentFiveTester) fragments[current];
                fragmentFiveTester.keyboardClick(letter);
                break;
            case 5:
                fragmentSixTester= (FragmentSixTester) fragments[current];
                fragmentSixTester.keyboardClick(letter);
                break;
        }
    }
    public void backspaceClicked(View view){
        int current=viewPager.getCurrentItem();
        switch (current){
            case 0:
                fragmentOneTester= (FragmentOneTester) fragments[current];
                fragmentOneTester.backspaceClicked();
                break;
            case 1:
                fragmentTwoTester= (FragmentTwoTester) fragments[current];
                fragmentTwoTester.backspaceClicked();
                break;
            case 2:
                fragmentThreeTester= (FragmentThreeTester) fragments[current];
                fragmentThreeTester.backspaceClicked();
                break;
            case 3:
                fragmentFourTester= (FragmentFourTester) fragments[current];
                fragmentFourTester.backspaceClicked();
                break;
            case 4:
                fragmentFiveTester= (FragmentFiveTester) fragments[current];
                fragmentFiveTester.backspaceClicked();
                break;
            case 5:
                fragmentSixTester= (FragmentSixTester) fragments[current];
                fragmentSixTester.backspaceClicked();
                break;
        }
    }

}




