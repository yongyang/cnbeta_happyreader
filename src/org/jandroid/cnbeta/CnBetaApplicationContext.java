package org.jandroid.cnbeta;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface CnBetaApplicationContext {

    boolean isNetworkConnected();

    boolean isSdCardMounted();

    File getFontsDir();

    File getLocalCacheDir();

    File getHistoryDir();

    boolean onOptionsItemSelected(Activity theActivity, MenuItem item);

    CnBetaPreferences getCnBetaPreferences();

    boolean isDarkThemeEnabled();

    void setDarkThemeEnabled(boolean enable);

}
