package org.jandroid.cnbeta;

import android.app.Application;
import android.os.Environment;
import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.util.EnvironmentUtils;
import org.jandroid.util.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application implements CnBetaApplicationContext{

    static Logger logger = Logger.newLogger(CnBetaApplication.class);

    private CnBetaHttpClient httpClient = CnBetaHttpClient.getInstance();

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
        File baseDir;
        if(EnvironmentUtils.checkSdCardMounted(this)) {
            // SD
            baseDir = new File(Environment.getExternalStorageDirectory(), Constants.BASE_DIR);
        }
        else {
            //内置存储器
            baseDir = this.getFilesDir();
        }
        try {
            FileUtils.forceMkdir(baseDir);
        }
        catch (IOException e) {
            logger.w("Couldn't make base dir", e);
        }
        return baseDir;
    }

    public String getSessionId() {
        //TODO: sessionId 是什么时候得到的
        return null;
    }

    public void destroy() {
        // shutdown httpClient 会导致再次打开程序无法获取网络数据
//        httpClient.shutdown();
//        System.exit(0);
    }

}
