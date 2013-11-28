package org.jandroid.common;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ThemeFragment extends BaseFragment {

    protected ThemeActivity themeActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof  ThemeActivity)) {
            throw new IllegalArgumentException("Activity must instanceof " + ThemeActivity.class.getName() + " for Fragment " + this.getClass().getName());
        }
        themeActivity = (ThemeActivity)activity;
    }

    @Override
    public void onResume() {
        updateTypeFace(getView());
        super.onResume();
    }

    public void updateTextSize(TextView textView, int dimResourceId) {
        themeActivity.updateTextSize(textView, dimResourceId);
    }

    public void updateTextSize(TextView[] textViews, int[] dimResourceIds) {
        themeActivity.updateTextSize(textViews, dimResourceIds);
    }

    public void updateTypeFace(View rootView) {
        themeActivity.updateTypeFace(rootView);
    }

    protected void onThemeChanged() {

    }
}
