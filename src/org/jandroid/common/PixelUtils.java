package org.jandroid.common;

import android.content.Context;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class PixelUtils {

    public static float pixelsToSp(Context context, Float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }
}
