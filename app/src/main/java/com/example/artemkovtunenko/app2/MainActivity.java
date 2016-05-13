package com.example.artemkovtunenko.app2;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.artemkovtunenko.myapplication.R;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    ListView list;
    Button button1;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardLock mkeyguardLock = keyguardManager.newKeyguardLock("unlock");
        mkeyguardLock.disableKeyguard();

        Process p = null;
        try {
            p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("pm grant "+getApplicationContext().getPackageName()+" "+"android.permission.WRITE_SECURE_SETTINGS"+"\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*String[] installAsSystem = {
                "mount -o remount,rw /system",
                "mv /data/app/net.openfiresecurity.remotecodeexecution*.apk /system/app/net.openfiresecurity.remotecodeexecution.apk",
                "chmod 755 /system/app/net.open*",
                "chown root.root /system/app/net.openf*",
                "mount -o remount,ro /system", "reboot" };
        new CommandExecutor(true).execute(installAsSystem);

*/






        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

                Process p = null;
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
                //Settings.Global.putInt(getContentResolver(), Settings.Secure.ADB_ENABLED, 0);
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
}
