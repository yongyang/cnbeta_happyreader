package org.jandroid.cnbeta;

import android.app.Application;
import org.apache.commons.io.FileUtils;
import org.jandroid.util.EnvironmentUtils;
import org.jandroid.util.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application implements CnBetaApplicationContext{

    Logger logger = Logger.newLogger(this.getClass());

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

    public File getBaseDir() {
        File baseDir = Constants.getBaseDir();
        try {
            FileUtils.forceMkdir(baseDir);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return baseDir;
    }

    public String getSessionId() {
        return null;
    }
}
