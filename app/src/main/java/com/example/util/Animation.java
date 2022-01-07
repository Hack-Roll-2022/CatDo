package com.example.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.SystemClock;
import android.view.View;

public class Animation {
    public static void specifyAnimation(View view, float yVal, float xVal, boolean isInfinite) {
//        Path path = new Path();
//        path.arcTo(0f, 0f, 500f, 1000f, 270f, -180f, true);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
//        animator.setDuration(2000);
//        animator.start();
        final Animator translationDownAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_Y, yVal)
                .setDuration(1000);

        final Animator translationLeftAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_X, (-1f)*xVal)
                .setDuration(500);

        final Animator translationUpAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_Y, (-1f) * yVal)
                .setDuration(2000);

        final Animator translationBackAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_Y, 0f)
                .setDuration(1700);

        final Animator translationBackHAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_X, 0f)
                .setDuration(1700);

        final Animator translationRightAnimator = ObjectAnimator
                .ofFloat(view, View.TRANSLATION_X, xVal)
                .setDuration(1500);

        final Animator rotateAnimator = ObjectAnimator
                .ofFloat(view,View.ROTATION_Y, 720)
                .setDuration(800);

        final Animator scaleLarge = ObjectAnimator
                .ofFloat(view,View.SCALE_Y,2)
                .setDuration(300);

        final Animator scaleSmall = ObjectAnimator
                .ofFloat(view,View.SCALE_Y,0.5f)
                .setDuration(300);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                translationDownAnimator,
                translationLeftAnimator,
                translationUpAnimator,
                translationRightAnimator,
                translationBackAnimator,
                translationBackHAnimator,
                rotateAnimator,
                scaleLarge,
                scaleSmall
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
