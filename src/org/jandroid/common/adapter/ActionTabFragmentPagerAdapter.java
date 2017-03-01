package org.jandroid.common.adapter;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    protected abstract ActionBar getActionBar();

    protected abstract ViewPager getViewPager();

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        final ActionBar actionBar = getActionBar();
        //未选中时才调用setSelectedNavigationItem,
        if(actionBar.getSelectedNavigationIndex() != position) {
            actionBar.setSelectedNavigationItem(position);
        }
    }

    public void onPageScrollStateChanged(int state) {

    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        getViewPager().setCurrentItem(getActionBar().getSelectedNavigationIndex());
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public int getCount() {
        return getActionBar().getTabCount();
    }

}