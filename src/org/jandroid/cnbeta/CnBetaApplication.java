package org.jandroid.cnbeta;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.entity.HistoryArticle;
import org.jandroid.cnbeta.loader.HistoryArticleListLoader;
import org.jandroid.common.EnvironmentUtils;
import org.jandroid.common.Logger;
import org.jandroid.common.ToastUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application implements CnBetaApplicationContext {

    static Logger logger = Logger.getLogger(CnBetaApplication.class);

    private Handler handler = new Handler();

    //当网络变化时, 要显示 Toast 提示用户
    private boolean hasNetwork = false;

    private PrefsObject prefsObject;

    private boolean isDarkThemeEnabled = false;

    private Set<Long> readArticleSids = new HashSet<Long>();

    @Override
    public void onCreate() {
        super.onCreate();
        prefsObject = PrefsObject.getInstance(this);

        try {
            List<HistoryArticle> historyArticles = new HistoryArticleListLoader().fromDisk(this.getHistoryDir());
            this.addHistoryArticle(historyArticles.toArray(new HistoryArticle[historyArticles.size()]));
        }
        catch (Exception e) {
            ToastUtils.showShortToast(this, "加载阅读历史失败，请尝试清空历史记录！");
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


    public boolean isMobileNetworkConnected() {
        return EnvironmentUtils.checkMobileNetworkConnected(this);
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
        if (prefsObject.isAutoCleanCache()) {
            cleanCache();
        }

        if (prefsObject.isAutoCleanHistory()) {
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

    public PrefsObject getPrefsObject() {
        return prefsObject;
    }

    public boolean isDarkThemeEnabled() {
        return isDarkThemeEnabled;
    }

    public void setDarkThemeEnabled(boolean isDarkThemeEnabled) {
        this.isDarkThemeEnabled = isDarkThemeEnabled;
    }

    public void addHistoryArticle(HistoryArticle... historyArticles) {
        if(historyArticles != null) {
            for(HistoryArticle historyArticle : historyArticles) {
                readArticleSids.add(historyArticle.getSid());
            }
        }
    }

    public boolean isArticleRead(long sid) {
        return readArticleSids.contains(sid);
    }
}
