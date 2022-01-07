package com.example.catdonotification;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class NotificationService extends IntentService {
    public NotificationService() {
        super("notif intent service");
    }

    public static AtomicBoolean isRunning = new AtomicBoolean(false);

    private boolean stop = false;
    /*
    @Override
    // execution of service will start on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning.set(true);
        int count = 0;

        while (isRunning.get() && count < 100){

            Log.d("THREAD", Thread.currentThread().toString() + isRunning.get());

            count++;
            int reqCode = MainActivity.dummyReq;
            MainActivity.dummyReq += 1;
            Intent newIntent = new Intent(this, MainActivity.class);
            MainActivity.showNotification(this, "hi", "fuck you!", newIntent, reqCode);
        }
        return START_STICKY;
    }

    @Override
    // execution of the service will stop on calling this method
    public void onDestroy() {
        this.stop = true;
        Log.d("THREAD-on-destroy", Thread.currentThread().toString() + isRunning.get());

        // stopping the process
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


     */

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isRunning.set(true);

        while (isRunning.get()){
            SystemClock.sleep(500);

            Log.d("THREAD", Thread.currentThread().toString() + isRunning.get());

            int reqCode = MainActivity.dummyReq;
            MainActivity.dummyReq += 1;
            Intent newIntent = new Intent(this, MainActivity.class);
            MainActivity.showNotification(this, "hi", "fuck you!", newIntent, reqCode);
        }
    }
}