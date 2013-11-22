package org.jandroid.common;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import org.jandroid.cnbeta.R;
import org.jandroid.common.adapter.ActionTabFragmentPagerAdapter;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ActionTabFragmentActivity extends ThemeActivity {

    protected ViewPager mViewPager;

    protected ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        protected ActionBar getActionBar() {
            return ActionTabFragmentActivity.this.getActionBar();
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
    protected abstract void initActionBar();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setContentView(R.layout.viewpager_activity);

        initViewPager();
        initActionBar();
    }

    protected void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
