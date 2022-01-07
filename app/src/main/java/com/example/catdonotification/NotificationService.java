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

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isRunning.set(true);

        while (isRunning.get()){
            SystemClock.sleep(500);

            int reqCode = MainActivity.dummyReq;
            MainActivity.dummyReq += 1;
            Intent newIntent = new Intent(this, MainActivity.class);
            MainActivity.showNotification(this, "hi", "fuck you!", newIntent, reqCode);
        }
    }
}