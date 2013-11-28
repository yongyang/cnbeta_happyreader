package org.jandroid.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.WeakHashMap;

import static android.view.WindowManager.LayoutParams;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class WindowUtils {

    public static View addMaskView(Activity activity) {
        return addMaskView(activity, 0x70000000);
    }

    public static View addMaskView(Activity activity, int bgColor) {
        WindowManager mWindowManager = SystemUtils.getWindowManager(activity);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        lp.gravity = Gravity.BOTTOM;// 可以自定义显示的位置
        lp.y = 10;// 距离底部的距离是10像素 如果是 top 就是距离top是10像素

        View maskView = new TextView(activity);
        maskView.setBackgroundColor(bgColor);
        mWindowManager.addView(maskView, lp);
        return maskView;
    }


    public static void removeMaskView(Activity activity, View maskView) {
        WindowManager mWindowManager = SystemUtils.getWindowManager(activity);
        mWindowManager.removeView(maskView);
    }
}
