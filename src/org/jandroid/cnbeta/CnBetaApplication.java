package org.jandroid.cnbeta;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.apache.commons.io.FileUtils;
import org.jandroid.common.EnvironmentUtils;
import org.jandroid.common.Logger;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.WindowUtils;

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

    private int themeId = R.style.Theme_cnBeta_Light; // default light
    private boolean isEyeFriendlyModeEnabled = false;

    private TextView maskView;

    @Override
    public void onCreate() {
        super.onCreate();
        cnBetaPreferences = CnBetaPreferences.getInstance(this);
        if(maskView == null) {
            maskView = new TextView(this);
            maskView.setBackgroundColor(0x60000000);
        }
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

    /**
     * 在这里统一处理标准菜单项目
     * @param theActivity
     * @param item
     * @return true consumed, false not
     */
    public boolean onOptionsItemSelected(final Activity theActivity, final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.openMainActivity(theActivity);
                return true;
            case R.id.main:
                Utils.openMainActivity(theActivity);
                return true;
            case R.id.topics:
                Utils.openTopicActivity(theActivity);
                return true;
            case R.id.dig_soft_industry_interact:
                Utils.openTypesActivity(theActivity);
                return true;
            case R.id.rank:
                Utils.openMRankActivity(theActivity);
                return true;
            case R.id.more_item:
                return true;
            case R.id.history:
                Utils.openHistoryActivity(theActivity);
                return true;
            case R.id.setting_item:
                Utils.openPreferenceActivity(theActivity);
                return true;
/*
            case R.id.versionCheck:
                Utils.openVersionCheckDialog(theActivity);
                break;
*/

            case R.id.aboutus_item:
                Utils.openAboutActivity(theActivity);
                return true;
        }

        return false;
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

    public CnBetaPreferences getCnBetaPreferences() {
        return cnBetaPreferences;
    }

    public View getMaskView() {
        return maskView;
    }

    public boolean isEyeFriendlyModeEnabled() {
        return isEyeFriendlyModeEnabled;
    }

    public void setEyeFriendlyModeEnabled(boolean enable) {
        isEyeFriendlyModeEnabled = enable;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }
}
