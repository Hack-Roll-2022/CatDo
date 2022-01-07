package com.example.catdonotification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.SharedPreferences;

import android.os.Build;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.media.RingtoneManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import android.os.CountDownTimer;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
import android.provider.Settings;
import android.util.Log;

import com.example.util.AppLifecycleObserver;


import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    private long _startTime;
    private long _endTime;
    private long _timeLeft;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private EditText mEditTextInput;
    private Button mButtonSet;
    private MaterialProgressBar materialProgressBar;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;


    private AppLifecycleObserver appLifecycleObserver = null;


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private ImageButton setCat1, setCat2, setCat3, setCat4;
    public static int dummyReq;
    public static int gifCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        materialProgressBar = findViewById(R.id.progress);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        checkOverlayPermission();

    }

    private void setTime(long timeInMs) {
        _startTime = timeInMs;
        resetTimer();
        closeKeyboard();
    }

    int curr = 0;

    private void startTimer() {
        // turn on tracking of app lifecycle observer
        if (appLifecycleObserver == null) {
            appLifecycleObserver = new AppLifecycleObserver(getApplicationContext());
        }
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);

        _endTime = System.currentTimeMillis() + _timeLeft;

        mCountDownTimer = new CountDownTimer(_timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                _timeLeft = millisUntilFinished;
                curr++;
                materialProgressBar.setProgress((int) (curr * 100000 / _startTime));
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();

                ProcessLifecycleOwner.get().getLifecycle().removeObserver(appLifecycleObserver);
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(appLifecycleObserver);

        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        _timeLeft = _startTime;
        materialProgressBar.setProgress(0);
        curr = 0;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (_timeLeft / 1000) / 3600;
        int minutes = (int) ((_timeLeft / 1000) % 3600) / 60;
        int seconds = (int) (_timeLeft / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

            if (_timeLeft < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (_timeLeft < _startTime) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", _startTime);
        editor.putLong("millisLeft", _timeLeft);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", _endTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        _startTime = prefs.getLong("startTimeInMillis", _startTime);
        _timeLeft = prefs.getLong("millisLeft", _timeLeft);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            _endTime = prefs.getLong("endTime", 0);
            _timeLeft = _endTime - System.currentTimeMillis();

            if (_timeLeft < 0) {
                _timeLeft = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }

    public void release(View view) {
        Toast.makeText(this, "Cat Released", Toast.LENGTH_SHORT).show();
    }

    public void select(View view) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup, null);
        setCat1 = (ImageButton) popupView.findViewById(R.id.cat1);
        setCat2 = (ImageButton) popupView.findViewById(R.id.cat2);
//        setCat3 = (ImageButton) popupView.findViewById(R.id.cat3);
//        setCat4 = (ImageButton) popupView.findViewById(R.id.cat4);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        setCat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat1);
                gifCat = 0;
                dialog.hide();
            }
        });

        setCat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat2);
                gifCat = 1;
                dialog.hide();
            }
        });

//        setCat3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImageView img = findViewById(R.id.imageView);
//                img.setImageResource(R.drawable.cat3);
//                dialog.hide();
//            }
//        });
//
//        setCat4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImageView img = findViewById(R.id.imageView);
//                img.setImageResource(R.drawable.cat4);
//                dialog.hide();
//            }
//        });
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

    public static void showNotification(Context context, String title, String message, Intent intent, int reqCode) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, reqCode, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "channel_name";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // create channel for newer android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id
        Log.d("showNotification", "showNotification: " + reqCode);
    }
}
