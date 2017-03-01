package org.jandroid.common;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class SystemUtils {

    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public static String getDisplayResolution(Context context) {
        WindowManager windowManager = getWindowManager(context);
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaysMetrics);
        return displaysMetrics.widthPixels + "x" + displaysMetrics.heightPixels;
    }

    public static void main(String[] args) {

    }
}
