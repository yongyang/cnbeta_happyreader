package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.TextView;
import org.jandroid.common.FontUtils;
import org.jandroid.common.adapter.ActionTabFragmentPagerAdapter;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class CnBetaActionTabFragmentActivity extends CnBetaThemeActivity {

    protected ViewPager mViewPager;

    protected ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        protected ActionBar getActionBar() {
            return CnBetaActionTabFragmentActivity.this.getActionBar();
        }

        @Override
        protected ViewPager getViewPager() {
            return mViewPager;
        }

        @Override
        public Fragment getItem(int position) {
            return getTabFragmentByItem(position);
        }

    };

    protected abstract Fragment getTabFragmentByItem(int position);
    protected abstract int[] getTabResourceIds();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setContentView(R.layout.viewpager_activity);

        createViewPager();
        createActionBar();
    }

    protected void createViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    protected void createActionBar() {
        final ActionBar actionBar = getActionBar();
        for (int resourceId : getTabResourceIds()) {

            // 使用 customview，一遍可以更改字体
            ViewGroup tabContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.actiontab_tabtext_customview, null);
            TextView tabTextView = (TextView) tabContainer.findViewById(R.id.tabTextView);
            tabTextView.setText(resourceId);
            actionBar.addTab(actionBar.newTab().setCustomView(tabContainer).setTabListener(pagerAdapter));
//            actionBar.addTab(actionBar.newTab().setText(resourceId).setTabListener(pagerAdapter));
        }
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // update action tab text font
        Typeface typeface = FontUtils.loadTypeface(this, ((CnBetaApplicationContext) getApplicationContext()).getCnBetaPreferences().getCustomFont());
        if(typeface != null) {
            for (int i = 0; i < getActionBar().getTabCount(); i++) {
                FontUtils.updateFont(getActionBar().getTabAt(i).getCustomView(), typeface);
            }
        }
    }

}
