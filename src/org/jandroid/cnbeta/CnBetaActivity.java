package org.jandroid.cnbeta;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.WindowUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class CnBetaActivity extends BaseActivity {

    private View maskView;

    // current font
    private String font = "default";

    // the theme resourceId
    private int themeId = R.style.Theme_cnBeta_Light;

    public void updateMaskView() {
        // if isEyeFriendlyModeEnable
        if (((CnBetaApplicationContext) getApplicationContext()).isEyeFriendlyModeEnabled()) {
            if(this.maskView == null) {
                // 还没有加上上 maskView
                this.maskView = WindowUtils.addMaskView(this);
            }
        }
        else {
            if (maskView != null) {
                WindowUtils.removeMaskView(this, maskView);
                maskView = null;
            }
        }
    }

    public void updateTheme() {
        if(isThemeChanged()) {
            syncThemeId();
            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        syncThemeId();
        // update theme
        this.setTheme(getThemeId());

        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);

        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // update theme when changed, will call recreate
        if(isThemeChanged()) {
            updateTheme();
        }

        updateMaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Sync Font onPause, so isFontChange always return true
        syncFont();
    }

    protected void onStop() {
        if (maskView != null) {
            WindowUtils.removeMaskView(this, maskView);
            maskView = null;
        }
        super.onStop();
    }





    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public boolean isFontChanged() {
        return CnBetaPreferences.getInstance(getApplication()).getCustomFont().equals(font);
    }

    public void syncFont(){
        this.font = CnBetaPreferences.getInstance(getApplication()).getCustomFont();
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public boolean isThemeChanged(){
        return ((CnBetaApplicationContext)getApplicationContext()).getThemeId() != getThemeId();
    }

    public void syncThemeId() {
        if(isThemeChanged()) {
            setThemeId(((CnBetaApplicationContext)getApplicationContext()).getThemeId());
        }
    }

    public boolean isDarkTheme() {
        return this.getThemeId() == getDarkThemeId();
    }

    public int getDarkThemeId() {
        return R.style.Theme_cnBeta_Dark;
    }

    public int getLightThemeId() {
        return R.style.Theme_cnBeta_Light;
    }
}
