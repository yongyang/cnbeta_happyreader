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

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaApplication extends Application implements CnBetaApplicationContext{

    static Logger logger = Logger.getLogger(CnBetaApplication.class);

    Handler handler = new Handler();

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
                break;
        }

        return true;
    }

    public void exit() {
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        if(prefs.getBoolean("pref_key_autoCleanCache", false)) {
            cleanCache();
        }

        if(prefs.getBoolean("pref_key_autoCleanHistory", false)) {
            cleanHistory();
        }

//        shutdown httpClient 会导致再次打开程序无法获取网络数据
        httpClient.shutdown();

        handler.postDelayed(new Runnable() {
            public void run() {
                System.exit(0);
            }
        }, 500);
    }

    public boolean cleanCache(){

        boolean success = true;
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

        boolean cleanHistory = prefs.getBoolean("pref_key_includeHistory", false);
        if(!cleanHistory) {
            try {
                FileUtils.moveFileToDirectory(new HistoryArticleListLoader().getFile(getBaseDir()), getBaseDir().getParentFile(), false);
            }
            catch (Exception e) {
                success = false;
            }
            try {
                FileUtils.moveFileToDirectory(new HistoryCommentListLoader().getFile(getBaseDir()), getBaseDir().getParentFile(), false);
            }
            catch (Exception e) {
                success = false;
            }
        }

        FileUtils.deleteQuietly(getBaseDir());
        if(!cleanHistory) {
            try {
                FileUtils.moveFileToDirectory(new HistoryArticleListLoader().getFile(getBaseDir().getParentFile()), getBaseDir(), true);
            }
            catch (Exception e){
                success = false;
            }

            try {
                FileUtils.moveFileToDirectory(new HistoryCommentListLoader().getFile(getBaseDir().getParentFile()), getBaseDir(), true);
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
