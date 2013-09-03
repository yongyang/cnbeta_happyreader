package org.jandroid.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class DateFormatUtils {

    public final static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static DateFormat getDefault() {
        return DEFAULT_DATE_FORMAT;
    }
}
