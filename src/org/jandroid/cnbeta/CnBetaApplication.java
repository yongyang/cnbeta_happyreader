package org.jandroid.cnbeta;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.loader.HistoryArticleListLoader;
import org.jandroid.cnbeta.loader.HistoryCommentListLoader;
import org.jandroid.common.EnvironmentUtils;
import org.jandroid.common.Logger;
import org.jandroid.common.ToastUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application implements CnBetaApplicationContext{

    static Logger logger = Logger.getLogger(CnBetaApplication.class);

    Handler handler = new Handler();

    private CnBetaHttpClient httpClient = CnBetaHttpClient.getInstance();

    //当网络变化时, 要显示 Toast 提示用户
    private boolean hasNetwork = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    public boolean isNetworkConnected() {
        boolean connected = EnvironmentUtils.checkNetworkConnected(this);
        if(hasNetwork && !connected) {
            handler.post(new Runnable() {
                public void run() {
                    ToastUtils.showShortToast(CnBetaApplication.this, "网络未连接，仅能加载本地已缓存的数据");
                }
            });
        }
        hasNetwork = connected;
        return hasNetwork;
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
            case R.id.aboutus_item:
                Utils.openAboutActivity(theActivity);
                break;
        }

        return true;
    }

    public void exit() {
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        if(prefs.getBoolean(getString(R.string.pref_key_autoCleanCache), false)) {
            cleanCache();
        }

        if(prefs.getBoolean(getString(R.string.pref_key_autoCleanHistory), false)) {
            cleanHistory();
        }

//        shutdown httpClient 会导致再次打开程序无法获取网络数据
        httpClient.shutdown();

        handler.postDelayed(new Runnable() {
            public void run() {
                System.exit(0);
            }
        }, 100);
    }

    public boolean cleanCache(){

        boolean success = true;
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        boolean cleanHistory = prefs.getBoolean(getString(R.string.pref_key_alsoCleanHistory), false);
        if(!cleanHistory) {
            try {
                File historyArticleFile = new HistoryArticleListLoader().getFile(getBaseDir());
                if(historyArticleFile.exists()) {
                    FileUtils.moveFileToDirectory(historyArticleFile, getBaseDir().getParentFile(), false);
                }
            }
            catch (Exception e) {
                success = false;
            }
            try {
                File historyCommentFile = new HistoryCommentListLoader().getFile(getBaseDir());
                if(historyCommentFile.exists()) {
                    FileUtils.moveFileToDirectory(historyCommentFile, getBaseDir().getParentFile(), false);
                }
            }
            catch (Exception e) {
                success = false;
            }
        }
        FileUtils.deleteQuietly(getBaseDir());
        if(!cleanHistory) {
            try {
                File tempHistoryArticleFile = new HistoryArticleListLoader().getFile(getBaseDir().getParentFile());
                if(tempHistoryArticleFile.exists()) {
                    FileUtils.moveFileToDirectory(tempHistoryArticleFile, getBaseDir(), true);
                }
            }
            catch (Exception e){
                success = false;
            }

            try {
                File templHistoryCommentFile = new HistoryCommentListLoader().getFile(getBaseDir().getParentFile());
                if(templHistoryCommentFile.exists()) {
                    FileUtils.moveFileToDirectory(templHistoryCommentFile, getBaseDir(), true);
                }
            }
            catch (Exception e) {
                success = false;
            }
        }
        return success;
    }

    public boolean cleanHistory() {
        boolean success = true;
        try {
            FileUtils.deleteQuietly(new HistoryCommentListLoader().getFile(getBaseDir()));
            FileUtils.deleteQuietly(new HistoryArticleListLoader().getFile(getBaseDir()));
        }
        catch (Exception e) {
            success = false;
        }
        return success;
    }

}
