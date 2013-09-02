package org.jandroid.cnbeta;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.view.MenuItem;
import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.common.EnvironmentUtils;
import org.jandroid.common.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application implements CnBetaApplicationContext{

    static Logger logger = Logger.getLogger(CnBetaApplication.class);

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

    // 在这里统一处理标准菜单项目
    public boolean onOptionsItemSelected(Activity theActivity, MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
        }
        switch (item.getItemId()) {
            case R.id.more_item:
                break;
            default:
        }
        return true;
    }

    public void destroy() {
//        shutdown httpClient 会导致再次打开程序无法获取网络数据
        httpClient.shutdown();
        System.exit(0);
    }

}
