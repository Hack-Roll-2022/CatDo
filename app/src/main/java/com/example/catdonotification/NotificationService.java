package com.example.catdonotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class NotificationService extends Service {
    private boolean stop = false;
    @Override
    // execution of service will start on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {
        int reqCode = MainActivity.dummyReq;
        MainActivity.dummyReq += 1;
        Intent newIntent = new Intent(this, MainActivity.class);
        if (!stop){
           MainActivity.showNotification(this, "hi", "fuck you!", newIntent, reqCode);
        }
        return START_STICKY;
    }

    @Override
    // execution of the service will stop on calling this method
    public void onDestroy() {
        this.stop = true;
        // stopping the process
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}