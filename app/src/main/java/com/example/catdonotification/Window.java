package com.example.catdonotification;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.util.Animation;

import static android.content.Context.WINDOW_SERVICE;

import pl.droidsonroids.gif.GifImageView;

public class Window {

    // declaring required variables
    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;

    public Window(Context context){
        this.context=context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mParams = new WindowManager.LayoutParams(
                    // Shrink the window to wrap the content rather
                    // than filling the screen
                    //WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    // Display it on top of other application windows
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    // Don't let it grab the input focus
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    // Make the underlying application window visible
                    // through any transparent parts

                    PixelFormat.TRANSLUCENT);

        }
        // getting a LayoutInflater
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.popup_window, null);

        View mPopupScreen = mView.findViewById(R.id.popup_screen);


        // Define the position of the
        // window within the screen
        mParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);

        // define path
        // Path path = new Path();
        // path.arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true);
        // PathInterpolator pathInterpolator = new PathInterpolator(path);

        // TODO: animation for window
        Animation.specifyAnimation(mPopupScreen, 800, 280,true);
    }


    public void open() {

        try {
            // check if the view is already
            // inflated or present in the window
            if(mView.getWindowToken()==null) {
                if(mView.getParent()==null) {
                    mWindowManager.addView(mView, mParams);
                    GifImageView gifImg = mView.findViewById(R.id.movingCatGif);
                    if (MainActivity.gifCat == 0) {
                        gifImg.setImageResource(R.drawable.catnobg);
                    } else {
                        gifImg.setImageResource(R.drawable.cat2nobg);
                    }

                }
            }
        } catch (Exception e) {
            Log.d("Error1",e.toString());
        }

    }

    public void close() {

        try {
            // remove the view from the window
            ((WindowManager)context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            mView.invalidate();
            // remove all views
            ViewGroup parents = ((ViewGroup)mView.getParent());
            if (parents != null) {
                parents.removeAllViews();
            }

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions
        } catch (Exception e) {
            Log.d("Error2",e.toString());
        }
    }



}

