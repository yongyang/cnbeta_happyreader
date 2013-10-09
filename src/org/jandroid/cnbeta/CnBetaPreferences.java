package org.jandroid.cnbeta;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.widget.TextView;
import org.jandroid.common.PixelUtils;

/**
* @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
*/
public class CnBetaPreferences {

    private Application application;
    private SharedPreferences prefs;

    private static CnBetaPreferences instance;

    private CnBetaPreferences(Application application) {
        this.application = application;
        prefs = application.getSharedPreferences(application.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

    public synchronized static CnBetaPreferences getInstance(Application application ) {
        if(instance == null) {
            instance = new CnBetaPreferences(application);
        }
        return instance;
    }

    public boolean isAutoCleanCache() {
        return prefs.getBoolean(application.getString(R.string.pref_key_autoCleanCache), false);
    }

    public boolean isAutoCleanHistory() {
        return prefs.getBoolean(application.getString(R.string.pref_key_autoCleanHistory), false);
    }

    public boolean isAlsoCleanHistory() {
        return prefs.getBoolean(application.getString(R.string.pref_key_alsoCleanHistory), false);
    }

    public int getFontSizeIncrement() {
        String size = prefs.getString(application.getString(R.string.pref_key_fontSizeIncrement), "0");
        return Integer.parseInt(size);
    }
}
