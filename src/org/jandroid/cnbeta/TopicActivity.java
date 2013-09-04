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
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.fragment.TopicArticleListFragment;
import org.jandroid.cnbeta.fragment.TopicListFragment;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.common.AnimateUtils;
import org.jandroid.common.BaseActivity;

public class TopicActivity extends BaseActivity {
    protected MenuItem refreshMenuItem;
    protected ImageView refreshActionView;

    public static abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }

    private static final String SELECTED_ITEM = "selected_item";

    public final static int[] tabs = new int[]{R.string.tab_topics, R.string.tab_topic_articles};
    private final Fragment[] fragments = new Fragment[tabs.length];

    private ViewPager mViewPager;

    private long topicId = 0;
    private String topicName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topicId = getIntent().getLongExtra("id", 9);
        topicName = getIntent().getStringExtra("name");
        if(topicName == null) {
            topicName = "Apple 苹果";
        }

        this.requestWindowFeature(Window.FEATURE_PROGRESS);
//        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);
        setContentView(R.layout.main);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        initViewPager();
        initActionBar();

        if(getIntent().hasExtra("id")) { // 由阅读文章中点击topic图片打开
            getActionBar().setSelectedNavigationItem(1);
        }

        if (savedInstanceState != null) {
//            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

        refreshActionView = (ImageView) getLayoutInflater().inflate(R.layout.iv_refresh_action_view, null);

    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

            @Override
            public int getCount() {
                return TopicActivity.this.getActionBar().getTabCount();
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        if (fragments[0] == null) {
                            //TODO: 创建 TopicListFragment
                            fragments[0] = new TopicListFragment(ArticleListLoader.Type.ALL);
                        }
                        return fragments[0];
                    case 1:
                        if (fragments[1] == null) {
                            //默认显示"苹果"主题文章
                            fragments[1] = new TopicArticleListFragment(getTopicId(), getTopicName());
                        }
                        return fragments[1];
                    default:
                        // only 2 tabs
                        return null;
                }
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                final ActionBar actionBar = getActionBar();
                if(position == 0) {
                    getActionBar().setTitle(getTitle());
                }
                else {
                    getActionBar().setTitle(getTopicName());
                }
                if(actionBar.getSelectedNavigationIndex() != position) {
                    actionBar.setSelectedNavigationItem(position);
                }
            }

            public void onPageScrollStateChanged(int state) {

            }

            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(TopicActivity.this.getActionBar().getSelectedNavigationIndex());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    private void initActionBar() {
        final ActionBar actionBar = getActionBar();
        for (int resourceId : tabs) {
            //全部资讯, 实时更新, 阅读历史
            actionBar.addTab(actionBar.newTab().setText(resourceId).setTabListener((ActionTabFragmentPagerAdapter)mViewPager.getAdapter()));
        }
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(SELECTED_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //add  refresh actionitem
        getMenuInflater().inflate(R.menu.search_refresh_menu, menu);
        refreshMenuItem = menu.findItem(R.id.refresh_item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        return ((CnBetaApplication)getApplicationContext()).onOptionsItemSelected(this, mi);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 每次都会调用该方法, 可以动态改变 menu
        return super.onPrepareOptionsMenu(menu);
    }

    protected void startRotateRefreshActionView() {
        if (refreshMenuItem != null) {
            /* Attach a rotating ImageView to the refresh item as an ActionView */
            AnimateUtils.rotate(refreshActionView);
            refreshMenuItem.setActionView(refreshActionView);
        }
    }

    protected void stopRotateRefreshActionView() {
        if (refreshMenuItem != null) {
            View actionView = refreshMenuItem.getActionView();
            if (actionView != null) {
                actionView.clearAnimation();
                refreshMenuItem.setActionView(null);
            }
        }
    }
}
