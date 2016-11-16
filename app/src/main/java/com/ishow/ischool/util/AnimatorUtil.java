package com.ishow.ischool.util;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.commonlib.util.LogUtil;

import java.text.DecimalFormat;


/**
 * Created by abel on 16/11/15.
 */

public class AnimatorUtil {

    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    public final static DecimalFormat dfint = new DecimalFormat("###,###,###");
    public final static DecimalFormat dffloat = new DecimalFormat("###,###,###.##");

    public static void riseNum(final TextView textView, final int number, int duration) {
        int fromNumber = getFromNumber(number);
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber,
                (int) number);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (number > 1000) {
                    textView.setText(dfint.format(animation.getAnimatedValue()));
                } else {
                    textView.setText(animation.getAnimatedValue().toString());
                }
            }
        });
        valueAnimator.start();
    }

    public static void riseNum(final TextView textView, final float number, int duration) {
        float fromNumber = getFromNumber(number);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromNumber,
                number);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (number > 1000) {
                    textView.setText(dffloat.format(animation.getAnimatedValue()));
                } else {
                    textView.setText(animation.getAnimatedValue().toString());
                }
            }
        });
        valueAnimator.start();
    }

    public static void riseNum(final ProgressBar progressBar, int number, int duration) {
        int fromNumber = getFromNumber(number);
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber,
                (int) number);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressBar.setProgress(Integer.parseInt(animation.getAnimatedValue().toString()));
            }
        });
        valueAnimator.start();
    }

    public static PropertyValuesHolder getPropertyValuesHolder(String label, int number) {
        return PropertyValuesHolder.ofInt(label, getFromNumber(number), number);
    }

    public static PropertyValuesHolder getPropertyValuesHolder(String label, float number) {
        return PropertyValuesHolder.ofFloat(label, getFromNumber(number), number);
    }

    public static int getFromNumber(int number) {
        int fromNumber = 0;
        if (number > 1000) {
            fromNumber = (int) (number - Math.pow(10, sizeOfInt(number) - 1));
        } else {
            fromNumber = number >> 1;
        }
        return fromNumber;

    }

    public static float getFromNumber(float number) {
        float fromNumber = 0;
        if (number > 1000) {
            fromNumber = number - (int) Math.pow(10, sizeOfInt((int) number) - 1);
        } else {
            fromNumber = number / 2;
        }
        return fromNumber;

    }

    private static int sizeOfInt(int x) {
        for (int i = 0; ; i++) {
            if (x <= sizeTable[i])
                return i + 1;
        }
    }
}
