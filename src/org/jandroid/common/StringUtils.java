package org.jandroid.common;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class StringUtils {

    public static int compareStringVersions(String v1, String v2) {
        String[] components1 = v1.split("\\.");
        String[] components2 = v2.split("\\.");
        int length = Math.min(components1.length, components2.length);
        for (int i = 0; i < length; i++) {
            int result = new Integer(components1[i]).compareTo(Integer.parseInt(components2[i]));
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
