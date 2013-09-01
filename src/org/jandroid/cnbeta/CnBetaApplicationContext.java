package org.jandroid.cnbeta;

import android.app.Activity;
import android.view.MenuItem;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface CnBetaApplicationContext {

    boolean isNetworkConnected();

    boolean isSdCardMounted();

    File getBaseDir();

    boolean onOptionsItemSelected(Activity theActivity, MenuItem item);
}
