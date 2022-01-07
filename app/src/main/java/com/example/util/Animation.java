package com.example.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.SystemClock;
import android.view.View;

public class Animation {
    public static void specifyAnimation(View view, float yVal, boolean isInfinite) {
        final Animator translationDownAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_Y, yVal)
                .setDuration(1000);

        final Animator alphaAnimator = ObjectAnimator
                .ofFloat(view, View.ALPHA, 1f, 0.5f)
                .setDuration(500);

        final Animator translationUpAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_Y, (-1f) * yVal)
                .setDuration(2000);

        final Animator translationBackAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_Y, 0f)
                .setDuration(1700);

        final Animator alphaAnimator2 = ObjectAnimator
                .ofFloat(view, View.ALPHA, 0.5f, 1f)
                .setDuration(500);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                translationDownAnimator,
                alphaAnimator,
                translationUpAnimator,
                alphaAnimator2,
                translationBackAnimator
        );

        if (isInfinite)
            setToInfinite(animatorSet);

        animatorSet.start();

    }

    private static void setToInfinite(AnimatorSet animatorSet) {
        animatorSet.addListener(new AnimatorListenerAdapter() {

            private boolean mCanceled;

            @Override
            public void onAnimationStart(Animator animation) {
                mCanceled = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mCanceled) {
                    //SystemClock.sleep(1000);
                    animation.start();
                }
            }

        });
    }


}
