package org.jandroid.common;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class AnimateUtils {
    /**
     * 移动方法
     *
     * @param v      需要移动的View
     * @param startX 起始x坐标
     * @param toX    终止x坐标
     * @param startY 起始y坐标
     * @param toY    终止y坐标
     */
    public static void move(View v, int startX, int toX, int startY, int toY) {
        move(v, startX, toX, startY, toY, 2000);
    }

    public static void move(View v, int startX, int toX, int startY, int toY, int duration) {
        TranslateAnimation animation = new TranslateAnimation(startX, toX, startY, toY);
        animation.setDuration(duration);
        animation.setFillAfter(true);
        v.startAnimation(animation);
    }

    public static void rotate(View v) {
        RotateAnimation animation = new RotateAnimation(0, 360, 0.5f, 0.5f);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setFillAfter(false);
        v.startAnimation(animation);
    }

    public static void fadeout(View v) {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setFillAfter(true);
        animation.setDuration(500);
        v.startAnimation(animation);
    }

}
