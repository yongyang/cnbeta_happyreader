package org.jandroid.cnbeta;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import org.apache.commons.io.FileUtils;
import org.jandroid.common.EnvironmentUtils;
import org.jandroid.common.Logger;
import org.jandroid.common.ToastUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application implements CnBetaApplicationContext {

    static Logger logger = Logger.getLogger(CnBetaApplication.class);

    private Handler handler = new Handler();

    //当网络变化时, 要显示 Toast 提示用户
    private boolean hasNetwork = false;

    private CnBetaPreferences cnBetaPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        cnBetaPreferences = CnBetaPreferences.getInstance(this);
    }

    public boolean isNetworkConnected() {
        boolean connected = EnvironmentUtils.checkNetworkConnected(this);
        if (hasNetwork && !connected) {
            handler.post(new Runnable() {
                public void run() {
                    ToastUtils.showShortToast(CnBetaApplication.this, "网络未连接，访问本地缓存数据");
                }
            });
        }
        hasNetwork = connected;
        return hasNetwork;
    }

    public boolean isSdCardMounted() {
        return EnvironmentUtils.checkSdCardMounted(this);
    }

    private File getBaseDir() {
        File baseDir;
        if (EnvironmentUtils.checkSdCardMounted(this)) {
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

    public File getFontsDir() {
        //Fonts 只能安装在 Context Dir 才能被访问
        File baseDir = getFilesDir();
        File fontsDir = new File(baseDir, "fonts");
        try {
            FileUtils.forceMkdir(fontsDir);
        }
        catch (IOException e) {
            logger.w("Couldn't make fonts dir", e);
        }
        return fontsDir;
    }

    public File getLocalCacheDir() {
        File baseDir = getBaseDir();
        File fontsDir = new File(baseDir, "cache");
        try {
            FileUtils.forceMkdir(fontsDir);
        }
        catch (IOException e) {
            logger.w("Couldn't make cache dir", e);
        }
        return fontsDir;
    }

    public File getHistoryDir() {
        File baseDir = getBaseDir();
        File fontsDir = new File(baseDir, "history");
        try {
            FileUtils.forceMkdir(fontsDir);
        }
        catch (IOException e) {
            logger.w("Couldn't make cache dir", e);
        }
        return fontsDir;
    }

    // 在这里统一处理标准菜单项目
    public boolean onOptionsItemSelected(final Activity theActivity, final MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.openMainActivity(theActivity);
                break;
            case R.id.main:
                Utils.openMainActivity(theActivity);
                break;
            case R.id.topics:
                Utils.openTopicActivity(theActivity);
                break;
            case R.id.dig_soft_industry_interact:
                Utils.openTypesActivity(theActivity);
                break;
            case R.id.rank:
                Utils.openMRankActivity(theActivity);
                break;
            case R.id.more_item:
                break;
            case R.id.history:
                Utils.openHistoryActivity(theActivity);
                break;
            case R.id.setting_item:
                Utils.openPreferenceActivity(theActivity);
                break;
/*
            case R.id.versionCheck:
                Utils.openVersionCheckDialog(theActivity);
                break;
*/
            case R.id.aboutus_item:
                Utils.openAboutActivity(theActivity);
                break;
        }

        return true;
    }

    public void onExit() {
        if (cnBetaPreferences.isAutoCleanCache()) {
            cleanCache();
        }

        if (cnBetaPreferences.isAutoCleanHistory()) {
            cleanHistory();
        }

//        shutdown httpClient 会导致再次打开程序无法获取网络数据
//        httpClient.shutdown();

/*
        handler.postDelayed(new Runnable() {
            public void run() {
                System.exit(0);
            }
        }, 100);
*/
    }

    public boolean cleanCache() {
        //为了兼容2.1.1版本，将根目录下的文件也删除
        for (File file : getBaseDir().listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        })) {
            FileUtils.deleteQuietly(file);
        }

        return FileUtils.deleteQuietly(getLocalCacheDir());

    }

    public boolean cleanHistory() {
        return FileUtils.deleteQuietly(getHistoryDir());
    }

    public boolean cleanFonts() {
        return FileUtils.deleteQuietly(getFontsDir());
    }

    public CnBetaPreferences getCnBetaPreferences(){
        return cnBetaPreferences;
    }

}
