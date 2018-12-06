package com.example.user.summerproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private String TAG = "Timers";
    private String opponentName;
    private static final int ID =12903;
    private NotificationCompat.Builder notification;
    private int Your_X_SECS = 5;
    NotificationManager nm;
    private String mID;
    private DatabaseReference mDatabaseUsers;
    private boolean firstTime = false;
    private DatabaseReference gameDatabase ;
    private DatabaseReference completedGameDatabase;
    private PowerManager pm;

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {



            if(dataSnapshot.hasChild("p2a1") && dataSnapshot.hasChild("notificationHasSent")){

                int time=dataSnapshot.child("time2").getValue(Integer.class);
                String letter=dataSnapshot.child("letter").getValue().toString();
                boolean notificationBoolean = dataSnapshot.child("notificationHasSent").getValue(boolean.class);
                String opID = dataSnapshot.getKey();
                opponentName=dataSnapshot.child("opName").getValue().toString();

                if (!notificationBoolean) {
                    Random random = new Random();
                    int ID = random.nextInt(10000)+1;
                    notification.setWhen(System.currentTimeMillis());
                    notification.setTicker("Νεο παιχνίδι από "+ opponentName);
                    notification.setContentTitle("Νεο παιχνίδι από "+ opponentName);
                    notification.setContentText("Στείλε τις απαντήσεις σου");
                    nm.notify(ID, notification.build());
                    gameDatabase.child(opID).child("notificationHasSent").setValue(true);
                    boolean isScreenOn = pm.isScreenOn();
                    if(isScreenOn==false)
                    {
                        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
                        wl.acquire(10000);
                        PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

                        wl_cpu.acquire(10000);
                    }
                }

            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {



        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private ChildEventListener completeGamesListener=new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.hasChild("p2a1") && dataSnapshot.hasChild("p1a1") && dataSnapshot.hasChild("notificationHasSent")){



                int time1=dataSnapshot.child("time1").getValue(Integer.class);
                String letter=dataSnapshot.child("letter").getValue().toString();
                int time2 = dataSnapshot.child("time2").getValue(Integer.class);
                opponentName=dataSnapshot.child("opName").getValue().toString();
                String opID = dataSnapshot.getKey();

                if (!dataSnapshot.child("notificationHasSent").getValue(boolean.class)) {

                    Random random = new Random();
                    int ID = random.nextInt(10000)+1;
                    notification.setWhen(System.currentTimeMillis());
                    notification.setTicker("Απάντηση από " + opponentName);
                    notification.setContentTitle("Απάντηση από " + opponentName);
                    notification.setContentText("Δες τα αποτελέσματα");
                    nm.notify(ID, notification.build());
                    gameDatabase.child(opID).child("notificationHasSent").setValue(true);
                    boolean isScreenOn = pm.isScreenOn();
                    if(isScreenOn==false)
                    {
                        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
                        wl.acquire(10000);
                        PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

                        wl_cpu.acquire(10000);
                    }
                }


            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            if(dataSnapshot.hasChild("p2a1") && dataSnapshot.hasChild("p1a1") && dataSnapshot.hasChild("notificationHasSent")){

                boolean done = dataSnapshot.child("notificationHasSent").getValue(boolean.class);

                if (!done) {

                    int time1 = dataSnapshot.child("time1").getValue(Integer.class);
                    String letter = dataSnapshot.child("letter").getValue().toString();

                    opponentName = dataSnapshot.child("opName").getValue().toString();
                    String opID = dataSnapshot.getKey();

                    Random random = new Random();
                    int ID = random.nextInt(10000)+1;

                    // notification

                    notification.setWhen(System.currentTimeMillis());
                    notification.setTicker("Απάντηση από " + opponentName);
                    notification.setContentTitle("Απάντηση από " + opponentName);
                    notification.setContentText("Δες τα αποτελέσματα");
                    nm.notify(ID, notification.build());
                    gameDatabase.child(opID).child("notificationHasSent").setValue(true);
                    boolean isScreenOn = pm.isScreenOn();
                    if(isScreenOn==false)
                    {
                        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
                        wl.acquire(10000);
                        PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

                        wl_cpu.acquire(10000);
                    }
                }

            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand Games");
        super.onStartCommand(intent, flags, startId);


        Bundle extras = intent.getExtras();
        mID = extras.getString("user_id");
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        gameDatabase = mDatabaseUsers.child(mID).child("Games");
        completedGameDatabase=gameDatabase.child("CompletedGames");
        notification.setSmallIcon(R.mipmap.ic_launcher); // Logo will be replaced
        Uri uri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getPackageName()+"/raw/chalkwriting");
        notification.setSound(uri);
        notification.setVibrate(new long[]{ 300, 1300});
        Intent intent2 = new Intent(NotificationService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        gameDatabase.removeEventListener(childEventListener);
        gameDatabase.addChildEventListener(childEventListener);
        completedGameDatabase.removeEventListener(completeGamesListener);
        completedGameDatabase.addChildEventListener(completeGamesListener);

        pm = (PowerManager)getBaseContext().getSystemService(Context.POWER_SERVICE);




        return START_REDELIVER_INTENT;




    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate Games");

    }

    @Override
    public void onDestroy()
    {   completedGameDatabase.removeEventListener(completeGamesListener);
        gameDatabase.removeEventListener(childEventListener);
        Log.e(TAG, "onDestroy Games");
       // stoptimertask();
        super.onDestroy();

    }










}