package org.jandroid.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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

    protected int getMaskViewBackgroundColor() {
        return 0x70000000;
    }

    protected void onThemeChanged() {
        logger.d("onThemeChanged");
        handler.post(new Runnable() {
            public void run() {
                recreate();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        syncThemeId();
        // update theme
        this.getApplication().setTheme(getThemeId());
        this.setTheme(getThemeId());

        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // update theme when changed, will call recreate
        if (isThemeChanged()) {

            // user handler.post to ensure recreate after resume finished
            handler.post(new Runnable() {
                public void run() {
                    recreate();
                }
            });

        }
        updateMaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // now syncfont, then isFontChanged return false, font should have been updated in onResume
        syncFont();
    }

    protected void onStop() {
        super.onStop();
        //在stop或者destroy的时候，一定要调用，否则可以内存溢出
//        removeMaskView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在stop或者destroy的时候，一定要调用，否则可以内存溢出
        removeMaskView();
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
            if (this.maskView != null) {
                if (((ColorDrawable) maskView.getBackground()).getColor() != getMaskViewBackgroundColor()) {
                    // maskView bg color changed, re-add
                    WindowUtils.removeMaskView(this, maskView);
                    maskView = null;
                    this.maskView = WindowUtils.addMaskView(this, getMaskViewBackgroundColor());
                }
            }
            else {
                this.maskView = WindowUtils.addMaskView(this, getMaskViewBackgroundColor());
            }
        }
        else {
            if (maskView != null) {
                WindowUtils.removeMaskView(this, maskView);
                maskView = null;
            }
        }
    }

    public void removeMaskView() {
        if (maskView != null) {
            WindowUtils.removeMaskView(this, maskView);
            maskView = null;
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
        Typeface typeface = FontUtils.loadTypeface(this, this.getPrefsFontPath());
        if (typeface != null) {
            FontUtils.updateFont(rootView, typeface);
        }
    }
}
