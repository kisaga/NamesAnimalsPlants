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

public class FriendNotificationService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private String TAG = "Timers";
    private static final int ID = 12903;
    private NotificationCompat.Builder notification;
    private int Your_X_SECS = 5;
    NotificationManager nm;
    private String mID;
    private DatabaseReference mDatabaseUsers;
    private boolean firstTime = false;
    private DatabaseReference friendsDatabase;
    private DatabaseReference requestsDatabase;
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    private PowerManager pm;


    private ChildEventListener friendListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if(dataSnapshot.hasChild("notificationHasSent")) {

                String name = dataSnapshot.child("Name").getValue(String.class);
                String id = dataSnapshot.child("Id").getValue(String.class);
                boolean notifHasSent = dataSnapshot.child("notificationHasSent").getValue(boolean.class);

                if(!notifHasSent) {
                    Random random = new Random();
                    int ID = random.nextInt(10000) + 1;
                    notification.setWhen(System.currentTimeMillis());
                    notification.setTicker("Νέα φιλία με " + name);
                    notification.setContentTitle("Νέα φιλία με  " + name);
                    notification.setContentText("Στείλε το πρώτο παιχνίδι");
                    nm.notify(ID, notification.build());
                    friendsDatabase.child(id).child("notificationHasSent").setValue(true);
                    //=====================================================
                    //=====================================================
                    //=====================================================
                    //=====================================================
                    //=====================================================
                    boolean isScreenOn = pm.isScreenOn();
                    if (isScreenOn == false) {
                        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
                        wl.acquire(10000);
                        PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

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
    } ;
    private ChildEventListener requestListener =new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            if(dataSnapshot.hasChild("notificationHasSent")) {

                String name = dataSnapshot.child("name").getValue(String.class);
                String id = dataSnapshot.child("Id").getValue(String.class);
                boolean notifHasSent = dataSnapshot.child("notificationHasSent").getValue(boolean.class);

                if(!notifHasSent) {
                    Random random = new Random();
                    int ID = random.nextInt(10000) + 1;
                    notification.setWhen(System.currentTimeMillis());
                    notification.setTicker("Νέο αίτημα φιλίας");
                    notification.setContentTitle("Νέο αίτημα φιλίας");
                    notification.setContentText("Αίτημα από " + name);
                    nm.notify(ID, notification.build());
                    requestsDatabase.child(id).child("notificationHasSent").setValue(true);
                    //=====================================================
                    //=====================================================
                    //=====================================================
                    //=====================================================
                    //=====================================================
                    boolean isScreenOn = pm.isScreenOn();
                    if (isScreenOn == false) {
                        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
                        wl.acquire(10000);
                        PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");

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
    } ;
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand  Friends");
        super.onStartCommand(intent, flags, startId);


        Bundle extras = intent.getExtras();
        mID = extras.getString("user_id");


        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        notification.setSmallIcon(R.mipmap.ic_launcher); // Logo will be replaced
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/chalkwriting");
        notification.setSound(uri);
        notification.setVibrate(new long[]{ 300, 1300});
        Intent intent2 = new Intent(FriendNotificationService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(FriendNotificationService.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);





        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        friendsDatabase = mDatabaseUsers.child(mID).child("Friends");
        requestsDatabase=mDatabaseUsers.child(mID).child("Requests").child("RequestsReceived");




        friendsDatabase.removeEventListener(friendListener);
        friendsDatabase.addChildEventListener(friendListener);

        requestsDatabase.removeEventListener(requestListener);
        requestsDatabase.addChildEventListener(requestListener);

        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================


        return START_REDELIVER_INTENT;


    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate  Friends");

    }

    @Override
    public void onDestroy() {
        friendsDatabase.removeEventListener(friendListener);
        requestsDatabase.removeEventListener(requestListener);
        Log.e(TAG, "onDestroy  Friends");
        // stoptimertask();
        super.onDestroy();

    }



}