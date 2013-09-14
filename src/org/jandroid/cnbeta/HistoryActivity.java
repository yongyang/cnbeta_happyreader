package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import org.jandroid.cnbeta.fragment.AbstractAsyncListFragment;
import org.jandroid.cnbeta.fragment.HistoryArticleListFragment;
import org.jandroid.cnbeta.fragment.HistoryCommentListFragment;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.adapter.ActionTabFragmentPagerAdapter;

public class HistoryActivity extends BaseActivity {

    public final static int[] tabs = new int[]{R.string.tab_history_article, R.string.tab_history_comment};
    private final AbstractAsyncListFragment[] fragments = new AbstractAsyncListFragment[tabs.length];

    private ViewPager mViewPager;

    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        protected ActionBar getActionBar() {
            return HistoryActivity.this.getActionBar();
        }

        @Override
        protected ViewPager getViewPager() {
            return mViewPager;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (fragments[0] == null) {
                        fragments[0] = new HistoryArticleListFragment();
                    }
                    return fragments[0];
                case 1:
                    if (fragments[1] == null) {
                        fragments[1] = new HistoryCommentListFragment();
                    }
                    return fragments[1];
                default:
                    // only 4 tabs
                    return null;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);
        setContentView(R.layout.main);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        initViewPager();
        initActionBar();

        if (savedInstanceState != null) {
//            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

    }
    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    private void initActionBar() {
        final ActionBar actionBar = getActionBar();
        for (int resourceId : tabs) {
            //全部资讯, 实时更新, 阅读历史
            actionBar.addTab(actionBar.newTab().setText(resourceId).setTabListener(pagerAdapter));
        }
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getMenuInflater().inflate(R.menu.default_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.refresh_item:
                fragments[getActionBar().getSelectedNavigationIndex()].reloadData();
                break;
        }
        return ((CnBetaApplicationContext)getApplicationContext()).onOptionsItemSelected(this, mi);
    }

}
