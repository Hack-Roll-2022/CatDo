package com.example.util;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.catdonotification.*;

public class AppLifecycleObserver implements LifecycleObserver {

    private final Context context;
    private boolean isStarted = false;

    // constructor to pass in context
    public AppLifecycleObserver(Context context) {
        this.context = context;
    }

    public static final String TAG = AppLifecycleObserver.class.getName();

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        System.out.println("Entered sneaky + " + MainActivity.mTimerRunning);

        if (MainActivity.mTimerRunning) {
            ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);

        }

        // entered fore ground
        Log.d("fore",ProcessLifecycleOwner.get().getLifecycle().getCurrentState().toString());

        if (isStarted) {
            SystemClock.sleep(2000);
            // end popup
            context.stopService(new Intent(context, ForegroundService.class));
            isStarted = false;

            // end notification, with atomic bool
            NotificationService.isRunning.compareAndSet(true, false);
            context.stopService(new Intent(context, NotificationService.class));
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        // entered background
        Log.d("back",ProcessLifecycleOwner.get().getLifecycle().getCurrentState().toString());

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();

        System.out.println("isStarted: " + isStarted + ", isScreenOn: "  + isScreenOn);

        if (!isStarted && isScreenOn) {
            SystemClock.sleep(2000);

            // start popup
            context.startService(new Intent(context, ForegroundService.class));
            isStarted = true;

            // start notification
            context.startService(new Intent(context, NotificationService.class));
        }
    }


}