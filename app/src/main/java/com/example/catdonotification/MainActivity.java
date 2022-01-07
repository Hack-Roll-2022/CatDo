package com.example.catdonotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.example.util.AppLifecycleObserver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkOverlayPermission();

        // use release the cat to start annoying
        View releaseCatBtn = findViewById(R.id.start_annoy);

        // now use the clicking to determine if to listen to app life cycle ob
        releaseCatBtn.setOnClickListener(new View.OnClickListener() {
            private boolean isToCheck = true;
            private AppLifecycleObserver appLifecycleObserver
                    = null;
            @Override
            public void onClick(View view) {
                //startWin();
                if (isToCheck) {
                    if (appLifecycleObserver == null) {
                        appLifecycleObserver = new AppLifecycleObserver(getApplicationContext());
                    }

                    ProcessLifecycleOwner.get().getLifecycle()
                            .addObserver(appLifecycleObserver);
                } else {
                    ProcessLifecycleOwner.get().getLifecycle()
                            .removeObserver(appLifecycleObserver);
                }

                isToCheck = !isToCheck;
            }
        });




    }

    // check if service running
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void stopService() {
        if (isMyServiceRunning(ForegroundService.class)) {
            stopService(new Intent(this, ForegroundService.class));
        }
    }

    // method for starting the service
    public void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if (Settings.canDrawOverlays(this)) {
                stopService();

                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    startForegroundService(new Intent(this, ForegroundService.class));
                } else {
                    startService(new Intent(this, ForegroundService.class));
                }
            }
        } else {
            stopService();

            startService(new Intent(this, ForegroundService.class));
        }
    }

    // method to ask user to grant the Overlay permission
    public void checkOverlayPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }

    // check for permission again when user grants it from
    // the device settings, and start the service
    @Override
    protected void onResume() {
        super.onResume();

        // not start automatically
        // startService();
    }
}

