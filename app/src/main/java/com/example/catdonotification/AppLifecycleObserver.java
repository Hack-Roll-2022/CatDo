package com.example.catdonotification;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class AppLifecycleObserver implements LifecycleObserver {

    private final Context context;

    // contructor to pass in context
    public AppLifecycleObserver(Context context) {
        this.context = context;
    }

    public static final String TAG = AppLifecycleObserver.class.getName();

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        // entered fore ground

        Log.d("THREAD-on-start", Thread.currentThread().toString());

        Log.d("fore",ProcessLifecycleOwner.get().getLifecycle().getCurrentState().toString());
        NotificationService.isRunning.compareAndSet(true, false);

        context.stopService(new Intent(context, NotificationService.class));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        // entered background

        Log.d("THREAD-on-end", Thread.currentThread().toString());

        Log.d("back",ProcessLifecycleOwner.get().getLifecycle().getCurrentState().toString());
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isInteractive();

        // if screen is not locked
        if (isScreenOn) {
            BackPushNotification();
        }
    }

    // use static method to show notification
    private void BackPushNotification(){
        //context.startService(new Intent(context, NotificationService.class));
        context.startService(new Intent(context, NotificationService.class));
    }

}