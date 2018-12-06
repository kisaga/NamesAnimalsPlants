package com.example.user.summerproject.myTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.user.summerproject.MainActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {

    private String TAG = "Broadcast";
    public NetworkChangeReceiver(){}
    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);

        Log.e(TAG,"OnReceive");
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {

            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
                //new ForceExitPause(context).execute();
                MainActivity.hasInternet=false;
            }else{
                //new ResumeForceExitPause(context).execute();
                MainActivity.hasInternet=true;
            }

        }
    }
}
