package com.example.artemkovtunenko.app2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by artemkovtunenko on 5/25/16.
 */
public class USBConnect extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("BROADCAST RECEIVER", "USB Connect !!!!!!!!! .....");
        if(intent.getAction().equalsIgnoreCase("android.intent.action.UMS_CONNECTED"))
        {
            //Settings.Global.putInt(, Settings.Secure.ADB_ENABLED, 0);
            Log.i("BROADCAST RECEIVER", "USB Connect !!!!!!!!! .....");
            //Intent i = new Intent(context, MainActivity.class);
            //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
        }
        //Intent i = new Intent(context, MainActivity.class);
        // i.addFlags(Intent.);
        //context.startActivity(i);

        //<action android:name="android.intent.action.UMS_CONNECTED" />
        //<action android:name="android.intent.action.UMS_DISCONNECTED" />

    }
}
