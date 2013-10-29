package org.jandroid.cnbeta;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import org.jandroid.common.ToastUtils;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaPreferences {

    private Application application;
    private SharedPreferences prefs;

    private String lastFont = "default";
    private Typeface lastTypeface;

    private static CnBetaPreferences instance;

    private CnBetaPreferences(Application application) {
        this.application = application;
        prefs = application.getSharedPreferences(application.getPackageName() + "_preferences", Context.MODE_PRIVATE);
    }

    synchronized static CnBetaPreferences getInstance(Application application) {
        if (instance == null) {
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

    public Typeface getCustomFontTypeface() {
        String customFont = getCustomFont();
        //默认字体
        if(customFont == null || customFont.isEmpty() || customFont.equals("default")){
            return Typeface.DEFAULT;
        }
        else {
            try {
                if (!customFont.equals(lastFont)) {
                    Typeface typeface;
                    if (customFont.contains("/android_asset/")) {
                        typeface = Typeface.createFromAsset(application.getAssets(), customFont.substring("file:///android_asset/".length()));
                    }
                    else {
                        typeface = Typeface.createFromFile(new File(customFont.substring("file://".length())));
                    }
                    lastFont = customFont;
                    lastTypeface = typeface;

                }
                return lastTypeface;
            }
            catch (Exception e) {
                ToastUtils.showShortToast(application, "加载个性化字体失败, " + e.toString());
                return null;
            }
        }
    }
}
