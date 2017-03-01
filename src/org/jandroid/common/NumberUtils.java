package org.jandroid.common;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class NumberUtils {

    public static int hexColor2Int(String hexColor) {
        return (int)Long.parseLong(hexColor.substring(2), 16);
    }
}
