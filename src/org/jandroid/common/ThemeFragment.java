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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof  ThemeActivity)) {
            throw new IllegalArgumentException("Activity must instanceof " + ThemeActivity.class.getName() + " for Fragment " + this.getClass().getName());
        }
    }

    @Override
    public void onResume() {
        updateTypeFace(getView());
        super.onResume();
    }

    public void updateTextSize(TextView textView, int dimResourceId) {
        ((ThemeActivity)theActivity).updateTextSize(textView, dimResourceId);
    }

    public void updateTextSize(TextView[] textViews, int[] dimResourceIds) {
        ((ThemeActivity)theActivity).updateTextSize(textViews, dimResourceIds);
    }

    public void updateTypeFace(View rootView) {
        ((ThemeActivity)theActivity).updateTypeFace(rootView);
    }

    // if call recreate() after theme changed, don't need to do anything here
    protected void onThemeChanged() {

    }
}
