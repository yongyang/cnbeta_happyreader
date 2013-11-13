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

import static android.view.WindowManager.LayoutParams;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class WindowUtils {


    public static String obtainResolution(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics.widthPixels + "x" + displaysMetrics.heightPixels;
    }

    public static void nightMode(Activity activity) {
        WindowManager mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        lp.gravity = Gravity.BOTTOM;// 可以自定义显示的位置
        lp.y = 10;// 距离底部的距离是10像素 如果是 top 就是距离top是10像素
        TextView textView = new TextView(activity);
        textView.setBackgroundColor(Color.BLACK);
        textView.setBackgroundColor(0x60000000);
        mWindowManager.addView(textView, lp);
    }
}
