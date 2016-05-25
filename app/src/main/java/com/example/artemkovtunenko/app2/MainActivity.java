package com.example.artemkovtunenko.app2;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.artemkovtunenko.myapplication.R;

import java.io.DataOutputStream;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {


    private String TAG= "Testing";
    private ListView list;
    private Button button1;
    private Button button2;
    private ToggleButton toggleButton;
    public static boolean screenOn = true;
    private BroadcastReceiver receiver = null;
    private BroadcastReceiver receiverBoot = null;
    private CheckBox checkBoxScreen;
    private CheckBox checkBoxUSBdebug;
    private View.OnClickListener listener;
    private View.OnClickListener listener2;
    private PowerManager power;
    private PowerManager.WakeLock lock;
    private int adbCheck;
    private PopupMenu settingsPopup;
    private ImageView imageView3;
    private MenuInflater menuInflater;
    private boolean onBoot = false;
    private PackageManager pm;
    private ComponentName component;


    public int adbCheckFunc()
    {
        return Settings.Secure.getInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.startOnBoot:
                if(BootReceiver.appStarted())
                    item.setChecked(true);
                item.setChecked(item.isChecked());
                //if(checked) {
                    pm.setComponentEnabledSetting(component,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Log.i(TAG, "Start on BOOT enabled !!!!!!!!! .....");
                return true;

                //}
            case R.id.dontStartOnBoot:
                if(!BootReceiver.appStarted())
                    item.setChecked(false);
                  item.setChecked(item.isChecked());
                //if(checked){
                    pm.setComponentEnabledSetting(component,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Log.i(TAG, "Start on BOOT disabled !!!!!!!!! .....");
                return true;
                //}
            default:
                pm.setComponentEnabledSetting(component,
                        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                        PackageManager.DONT_KILL_APP);
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "On Create .....");

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardLock mkeyguardLock = keyguardManager.newKeyguardLock("unlock");
        mkeyguardLock.disableKeyguard();

        power = (PowerManager) getSystemService(Context.POWER_SERVICE);
        lock = power.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");

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
        catch (RuntimeException r){}


        //disable ADB debugging
        Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
        Log.i(TAG, "Program has Created. USB disabled !!!!!!!!! .....");






        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //pm = getPackageManager();
        //component = new ComponentName(getApplicationContext(), BootReceiver.class);


        //Listen to when screen goes off
        //When it goes off, detect it, and turn off USB debugging
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Screen Receiver !!!!!!!!! .....");
                if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
                {
                    Log.i(TAG, "Screen went OFF !!!!!!!!! .....");
                    Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
                }
                else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
                {
                    Log.i(TAG, "Screen is ON !!!!!!!!! .....");
                }

            }
        };
        //register that receiver
        registerReceiver(receiver, filter);


        //Settings button
        //lunch ADB debug setting from main screen
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

        //Set up 2 checkboxes and initialize them 
        checkBoxScreen = (CheckBox)findViewById(R.id.checkBox1);
        checkBoxUSBdebug = (CheckBox)findViewById(R.id.checkBox);
        adbCheck = adbCheckFunc();
        if(adbCheck != 0)
        {
            checkBoxUSBdebug.setChecked(true);
        }

        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                switch (view.getId())
                {
                    case R.id.checkBox1:
                        if(checked)
                        {
                            lock.acquire();
                            Log.i("TAG", "Screen LOCKED !!!!!!!!! .....");
                            Toast.makeText(MainActivity.this,
                                    "Screen LOCKED ON", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            lock.release();
                            Log.i("TAG", "Screen UNLOCKED !!!!!!!!! .....");
                            Toast.makeText(MainActivity.this,
                                    "Screen UNLOCKED", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.checkBox:
                        if(checked)
                        {
                            Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 1);
                            Log.i("TAG", "USB Debug ON !!!!!!!!! .....");
                            Toast.makeText(MainActivity.this,
                                    "USB Debugging is On", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
                            Log.i("TAG", "USB Debug OFF !!!!!!!!! .....");
                            Toast.makeText(MainActivity.this,
                                    "USB Debugging is Off", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }

            }

        };
        checkBoxScreen.setOnClickListener(listener);
        checkBoxUSBdebug.setOnClickListener(listener);


        /*
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        */


        /////////////////////////////////




        /*button2 = (Button) findViewById(R.id.button2);
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
                //Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
            //}
        //});




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
        unregisterReceiver(receiver);
        Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
        Log.i(TAG, "On Destroy .....");
    }

    @Override
    protected void onPause() {
        super.onPause();
        adbCheck = adbCheckFunc();
        if(adbCheck == 1)
        {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }
        Log.i(TAG, "On Pause .....");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "On Restart .....");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "On Resume .....");
        adbCheck = adbCheckFunc();
        if(adbCheck != 0)
        {
            checkBoxUSBdebug.setChecked(true);
        }
        else
        {
            checkBoxUSBdebug.setChecked(false);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        adbCheck = adbCheckFunc();
        // toggle the USB debugging setting
        if(adbCheck == 0)
        {
            Log.i(TAG, "USB is off");
            //Toast.makeText(MainActivity.this,
                    //"USB Debugging is Off", Toast.LENGTH_LONG).show();
        }
        else
        {
            Log.i(TAG, "USB is on");
            //Toast.makeText(MainActivity.this,
                    //"USB Debugging is On", Toast.LENGTH_LONG).show();
        }
        //Settings.Secure.putInt(getContentResolver(),
                //Settings.Secure.ADB_ENABLED, adb);

        Log.i(TAG, "On Start .....");
    }
}
