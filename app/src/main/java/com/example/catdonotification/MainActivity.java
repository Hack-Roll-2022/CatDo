package com.example.catdonotification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import android.provider.Settings;
import android.view.View;

import com.example.util.AppLifecycleObserver;

public class MainActivity extends AppCompatActivity {
    private long _startTime;
    private long _endTime;
    private long _timeLeft;

    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private EditText mEditTextInput;
    private Button mButtonSet;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;


    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageButton setCat1, setCat2, setCat3, setCat4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

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
    }

    private void setTime(long timeInMs) {
        _startTime = timeInMs;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        _endTime = System.currentTimeMillis() + _timeLeft;

        mCountDownTimer = new CountDownTimer(_timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                _timeLeft = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        _timeLeft = _startTime;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (_timeLeft / 1000) / 3600;
        int minutes = (int) (_timeLeft / 1000) / 60;
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
        setCat3 = (ImageButton) popupView.findViewById(R.id.cat3);
        setCat4 = (ImageButton) popupView.findViewById(R.id.cat4);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        setCat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat1);
                dialog.hide();
            }
        });

        setCat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat2);
                dialog.hide();
            }
        });

        setCat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat3);
                dialog.hide();
            }
        });

        setCat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = findViewById(R.id.imageView);
                img.setImageResource(R.drawable.cat4);
                dialog.hide();
            }
        });

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

