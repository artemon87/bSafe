package com.example.artemkovtunenko.app2;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.artemkovtunenko.myapplication.R;

import java.io.DataOutputStream;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {


    String TAG= "Testing";
    ListView list;
    Button button1;
    Button button2;
    public static boolean screenOn = true;
    private BroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "On Create .....");

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardLock mkeyguardLock = keyguardManager.newKeyguardLock("unlock");
        mkeyguardLock.disableKeyguard();

        Process newProcess = null;
        try {
            newProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(newProcess.getOutputStream());
            os.writeBytes("pm grant "+getApplicationContext().getPackageName()+" "+"android.permission.WRITE_SECURE_SETTINGS"+"\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }






        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Screen Receiver !!!!!!!!! .....");
                if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
                {
                    Log.i(TAG, "Screen went OFF !!!!!!!!! .....");
                }
                else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
                {
                    Log.i(TAG, "Screen is ON !!!!!!!!! .....");
                }

            }
        };
        registerReceiver(receiver, filter);




        button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(ComponentName.unflattenFromString("com.android.settings/.DevelopmentSettings"));
                intent.addCategory("android.intent.category.LAUNCHER");
                startActivity(intent);



            }
        });


        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Process p = null;
                Context c = null;
                try {
                    p = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(p.getOutputStream());
                    os.writeBytes("pm grant "+c.getPackageName()+" "+"android.permission.WRITE_SECURE_SETTINGS"+"\n");
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int adb = Settings.Secure.getInt(getContentResolver(),
                        Settings.Secure.ADB_ENABLED, 0);
                // toggle the USB debugging setting
                adb = adb == 0 ? 1 : 0;
                Settings.Secure.putInt(getContentResolver(),
                        Settings.Secure.ADB_ENABLED, adb);
                        */
                /*Process p = null;
                try {
                    p = Runtime.getRuntime().exec("su");
                    DataOutputStream os = new DataOutputStream(p.getOutputStream());
                    os.writeBytes("pm grant "+getApplicationContext().getPackageName()+" "+"android.permission.WRITE_SECURE_SETTINGS"+"\n");
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
            }
        });



/*
        list = (ListView) findViewById(R.id.listView1);
        ArrayList<String> strList = new ArrayList<String>();
        strList.add("One");
        strList.add("Two");

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.layout, strList);
        list.setVisibility(list.VISIBLE);
        list.setAdapter(adapter1);
*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
        Log.i(TAG, "On Destroy .....");
    }
    /* (non-Javadoc)
    * @see android.app.Activity#onPause()
    */
    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "On Pause .....");
    }

    /* (non-Javadoc)
    * @see android.app.Activity#onRestart()
    */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "On Restart .....");
    }

    /* (non-Javadoc)
    * @see android.app.Activity#onResume()
    */
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "On Resume .....");
    }

    /* (non-Javadoc)
    * @see android.app.Activity#onStart()
    */
    @Override
    protected void onStart() {
        super.onStart();

        int adbCheck = Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.ADB_ENABLED, 0);
        // toggle the USB debugging setting
        if(adbCheck == 0)
        {
            Log.i(TAG, "USB is off");
        }
        else
        {
            Log.i(TAG, "USB is on");
            Toast.makeText(MainActivity.this,
                    "USB Debugging is On", Toast.LENGTH_LONG).show();
        }
        //Settings.Secure.putInt(getContentResolver(),
                //Settings.Secure.ADB_ENABLED, adb);

        Log.i(TAG, "On Start .....");
    }
}
