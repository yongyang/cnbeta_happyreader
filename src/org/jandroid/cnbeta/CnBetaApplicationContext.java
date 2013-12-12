package org.jandroid.cnbeta;

import android.app.Activity;
import android.view.MenuItem;
import org.jandroid.cnbeta.entity.BaseArticle;
import org.jandroid.cnbeta.entity.HistoryArticle;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface CnBetaApplicationContext {

    boolean isNetworkConnected();

    boolean isMobileNetworkConnected();

    boolean isSdCardMounted();

    File getFontsDir();

    File getLocalCacheDir();

    File getHistoryDir();

    boolean onOptionsItemSelected(Activity theActivity, MenuItem item);

    PrefsObject getPrefsObject();

    boolean isDarkThemeEnabled();

    void setDarkThemeEnabled(boolean enable);

    void addHistoryArticle(HistoryArticle... historyArticles);
    boolean isArticleRead(long sid);


}
