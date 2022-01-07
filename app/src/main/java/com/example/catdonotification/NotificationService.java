package com.example.catdonotification;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class NotificationService extends IntentService {
    public NotificationService() {
        super("notif intent service");
    }

    public static AtomicBoolean isRunning = new AtomicBoolean(false);

    private String[] messages = {
            "Hi, why are you playing?",
            "I will bite you if you keep playing",
            "I am a happy cat miew miew miew",
            "NOOOOOOO, STOPPPPPPP",
            "You will get punished if you keep playing",
            "I AM A CAT and YOU CANNOT PLAY YOUR PHONE",
            "yoooooo, focus on your study",
            "You should put back your phone",
            "CATS ARE THE BEST HAHAHAAAA"
    };

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isRunning.set(true);

        while (isRunning.get()){
            SystemClock.sleep(500);

            int reqCode = MainActivity.dummyReq;
            MainActivity.dummyReq += 1;
            Intent newIntent = new Intent(this, MainActivity.class);

            int random = new Random().nextInt(8);
            MainActivity.showNotification(this, "CatDo", messages[random], newIntent, reqCode);
        }
    }
}