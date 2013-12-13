package org.jandroid.cnbeta;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import org.jandroid.common.NumberUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class PrefsObject {

    private Application application;
    private SharedPreferences prefs;


    private static PrefsObject instance;

    private PrefsObject(Application application) {
        this.application = application;
        prefs = application.getSharedPreferences(application.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

    public synchronized static PrefsObject getInstance(Application application) {
        if (instance == null) {
            instance = new PrefsObject(application);
        }
        return instance;
    }

    public boolean isAutoCleanCache() {
        return prefs.getBoolean(application.getString(R.string.pref_key_autoCleanCache), false);
    }

    public boolean isAutoCleanHistory() {
        return prefs.getBoolean(application.getString(R.string.pref_key_autoCleanHistory), false);
    }

    public int getFontSizeOffset() {
        String size = prefs.getString(application.getString(R.string.pref_key_fontSizeOffset), "0");
        try {
            return Integer.parseInt(size);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public String getSignature() {
        return prefs.getString(application.getString(R.string.pref_key_signature), "");
    }

    public String getCustomFont() {
        return prefs.getString(application.getString(R.string.pref_key_customFont), "default");
    }

    public String getNightModeBrightness() {
        return prefs.getString(application.getString(R.string.pref_key_nightmode_brightness), "0x50000000");
    }

    public int getNightModeBrightnessInteger() {
        return NumberUtils.hexColor2Int(getNightModeBrightness());
    }

    public boolean isImageEnabledOnPhoneNetwork() {
        return prefs.getBoolean(application.getString(R.string.pref_key_enableImageOnMobileNetwork), true);
    }

    public boolean isPluginEnabledOnPhoneNetwork() {
        return prefs.getBoolean(application.getString(R.string.pref_key_enablePluginOnMobileNetwork), false);
    }

    public boolean isCommentListOrderReverse() {
        return prefs.getBoolean(application.getString(R.string.pref_key_commentListOrderReverse), false);
    }
}
