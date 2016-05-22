package com.example.artemkovtunenko.app2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BootReceiver extends BroadcastReceiver {
    private static boolean started = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        started = true;
        Log.i("BOOT", "App started on BOOT !!!!!!!!! .....");
    }
    public static boolean appStarted()
    {
        return started;
    }

}
