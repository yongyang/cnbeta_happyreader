package org.jandroid.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import org.jandroid.cnbeta.R;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ThemeActivity extends BaseActivity {

    private View maskView;

    // current font
    private String font = "default";

    // the theme resourceId
    private int themeId = R.style.Theme_cnBeta_Light;

    protected abstract String getPrefsFontPath();

    protected abstract int getFontSizeOffset();

    protected abstract int getPrefsThemeId();

    protected abstract boolean isMaskViewEnabled();


    protected void onThemeChanged() {
        logger.d("onThemeChanged");
/*
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(this, this.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
*/
        recreate();
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
        if (isThemeChanged()) {
            recreate();
        }

        updateMaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // now syncfont, now isFontChanged return false
        syncFont();
    }

    protected void onStop() {
        super.onStop();
        if (maskView != null) {
            WindowUtils.removeMaskView(this, maskView);
            maskView = null;
        }
    }

    protected void syncThemeId() {
        int newThemeId = getPrefsThemeId();
        if (newThemeId != themeId) {
            setThemeId(newThemeId);
        }
    }

    protected boolean isThemeChanged() {
        return getThemeId() != getPrefsThemeId();
    }

    protected void syncFont() {
        String newFont = getPrefsFontPath();
        if (!this.font.equals(newFont)) {
            this.font = newFont; // 立即更新
        }
    }

    public void updateMaskView() {
        // if isEyeFriendlyModeEnable
        if (isMaskViewEnabled()) {
            if (this.maskView == null) {
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

    public boolean isFontChanged() {
        return !this.font.equals(getPrefsFontPath());
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }


    public void updateTextSize(TextView textView, int dimResourceId) {
        FontUtils.updateTextSize(this, textView, dimResourceId, getFontSizeOffset());
    }

    public void updateTextSize(TextView[] textViews, int[] dimResourceIds) {
        if (textViews == null || dimResourceIds == null || textViews.length != dimResourceIds.length) {
            throw new IllegalArgumentException("TextView and dimResourceIds arrays' length are different");
        }
        for (int i = 0; i < textViews.length; i++) {
            updateTextSize(textViews[i], dimResourceIds[i]);
        }
    }

    public void updateTypeFace(View rootView) {
        FontUtils.updateFont(rootView, FontUtils.loadTypeface(this, this.getPrefsFontPath()));
    }

}
