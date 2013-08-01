package org.jandroid.cnbeta;

import android.app.Application;
import org.jandroid.cnbeta.util.EnvironmentUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application {
    
    public static boolean IS_ONLINE;
    public static boolean IS_SD_CARD_MOUNTED;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    public boolean isNetworkConnected() {
        return EnvironmentUtils.checkNetworkConnected(this);
    }
    
    public boolean isSdCardMounted() {
        return EnvironmentUtils.checkSdCardMounted(this);
    }
    
}
