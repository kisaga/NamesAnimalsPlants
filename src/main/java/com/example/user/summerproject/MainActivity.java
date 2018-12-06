package com.example.user.summerproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.user.summerproject.myTools.NetworkChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    public static boolean hasInternet=false;


    private String playerName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseUsers;
    private static final int SET__NICKNAME_RESULT_CODE=0;
    private NetworkChangeReceiver networkChangeReceiver;
    private boolean fromSet=false;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);







        if(requestCode==SET__NICKNAME_RESULT_CODE) {
            fromSet=true;

            playerName = data.getStringExtra("nickname");



            Intent tutorialIntent = new Intent(MainActivity.this, TutorialActivity.class);
            tutorialIntent.putExtra("name", playerName);
            startActivity(tutorialIntent);
            overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkChangeReceiver=new NetworkChangeReceiver();

        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkChangeReceiver,filter);


        Typeface mTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");







        mAuth=FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuthListener=new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if(firebaseAuth.getCurrentUser()==null){


                    Intent signInIntent=new Intent(MainActivity.this,SignInActivity.class);
                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signInIntent);
                    overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);


                }else{


                    final String user_id=firebaseAuth.getCurrentUser().getUid();

                    mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {




                            if(dataSnapshot.hasChild(user_id)){

                                String name=dataSnapshot.child(user_id).child("Name").getValue(String.class);
                                if(name.equals(" ")){

                                    Intent setNickname=new Intent(MainActivity.this,SetNickname.class);
                                    startActivityForResult(setNickname,SET__NICKNAME_RESULT_CODE);
                                    overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                                    //=====================

                                }else{

                                    playerName=name;


                                    Intent viewrIntent=new Intent(MainActivity.this,ViewerActivity.class);
                                    viewrIntent.putExtra("name",playerName);
                                    viewrIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                                    startActivity(viewrIntent);
                                    overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);

                                }

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }
        };





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
    protected void onStart(){
        super.onStart();



        CountDownTimer countDownTimer=new CountDownTimer(1500,1500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(hasInternet){
                    if(!fromSet){
                        mAuth.addAuthStateListener(mAuthListener);
                    }
                }else{
                    Toast.makeText(getBaseContext(),"Υπήρξε πρόβλημα με την σύνδεση",Toast.LENGTH_SHORT).show();
                    finishAffinity();
                }
            }
        };

        countDownTimer.start();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeReceiver);
        mAuth.removeAuthStateListener(mAuthListener);
        super.onStop();
    }
}
