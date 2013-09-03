package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.RankArticleListAsyncTask;
import org.jandroid.cnbeta.entity.RankArticle;
import org.jandroid.cnbeta.fragment.Top10ArticleListFragment;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.IntentUtils;
import org.jandroid.common.async.AsyncResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: 由 Activity 加载数据，然后去更新 Fragment

public class Top10Activity extends BaseActivity implements HasAsync<Map<String, List<RankArticle>>> {

    private Handler handler = new Handler();

    private final Map<String, List<RankArticle>> allRankArticlesMap = new HashMap<String, List<RankArticle>>();

    //TODO: set lastLoadTime to refresh time in Fragment
    private Date lastLoadTime = null;
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String SELECTED_ITEM = "selected_item";

    public final static int[] tabs = new int[]{R.string.tab_hits24, R.string.tab_comments24, R.string.tab_recommend};
    public final static int[] tabGroup_hits = new int[]{R.string.tab_hits24, R.string.tab_hits_week, R.string.tab_hits_month};
    public final static int[] tabGroup_comments = new int[]{R.string.tab_comments24, R.string.tab_comments_week, R.string.tab_comments_month};

    private final Top10ArticleListFragment[] fragments = new Top10ArticleListFragment[tabs.length];

    private ViewPager mViewPager;

    // 当前是否正在加载数据，避免多次加载
    private volatile boolean isLoading = false;


    private MenuItem refreshMenuItem;
    private ImageView refreshActionView;
    private Animation clockWiseRotationAnimation;

    public static enum RankType {
        HITS24("hits24"),
        HITS_WEEK("hits_week"),
        HITS_MONTH("hits_month"),
        COMMENTS24("comments24"),
        COMMENTS_WEEK("comments_week"),
        COMMENTS_MONTH("comments_month"),
        RECOMMEND("recommend");

        private String type;

        private RankType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "RankType{" +
                    "type='" + type + '\'' +
                    '}';
        }
    }


    public static abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }


    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        public int getCount() {
            return Top10Activity.this.getActionBar().getTabCount();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (fragments[0] == null) {
                        fragments[0] = new Top10ArticleListFragment(RankType.HITS24, RankType.HITS_WEEK, RankType.HITS_MONTH);
                    }
                    return fragments[0];
                case 1:
                    if (fragments[1] == null) {
                        fragments[1] = new Top10ArticleListFragment(RankType.COMMENTS24, RankType.COMMENTS_WEEK, RankType.COMMENTS_MONTH);
                    }
                    return fragments[1];
                case 2:
                    if (fragments[2] == null) {
                        fragments[2] = new Top10ArticleListFragment(RankType.RECOMMEND);
                    }
                    return fragments[2];
                default:
                    // 3 tabs
                    return null;
            }
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        public void onPageSelected(int position) {
            final ActionBar actionBar = getActionBar();
            //未选中时才调用setSelectedNavigationItem,
            if (position != actionBar.getSelectedNavigationIndex()) {
                actionBar.setSelectedNavigationItem(position);
            }
        }

        public void onPageScrollStateChanged(int state) {

        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(Top10Activity.this.getActionBar().getSelectedNavigationIndex());
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            //切换排行类型，如：人气-天/周/月
            int index = Top10Activity.this.getActionBar().getSelectedNavigationIndex();
            Top10ArticleListFragment fragment = (Top10ArticleListFragment) getItem(index);
            fragment.switchRankType(allRankArticlesMap);
            RankType rankType = fragment.getCurrentRankType();
            int rankTypeIndex = fragment.getCurrentRankTypeIndex();
            if (rankType.getType().contains("hits")) {
                tab.setText(tabGroup_hits[rankTypeIndex]);
            }
            else if (rankType.getType().contains("comments")) {
                tab.setText(tabGroup_comments[rankTypeIndex]);
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
//        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);
        setContentView(R.layout.main);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setupViewPager();
        setupActionBar();

        if (savedInstanceState != null) {
//            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

        refreshActionView = (ImageView) getLayoutInflater().inflate(R.layout.iv_refresh_action_view, null);
        clockWiseRotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation_clockwise_refresh);
        clockWiseRotationAnimation.setRepeatCount(Animation.INFINITE);

        reloadRanks();
    }

    private void setupActionBar() {
        final ActionBar actionBar = getActionBar();
        for (int resourceId : tabs) {
            //全部资讯, 实时更新, 阅读历史
            actionBar.addTab(actionBar.newTab().setText(resourceId).setTabListener(pagerAdapter));
        }
        pagerAdapter.notifyDataSetChanged();
    }

    private void setupViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO: 立即加载数据，然后更新 Fragment
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
        //refresh actionitem
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


    public void reloadRanks() {
        if (!isLoading) {
            isLoading = true;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载排行信息中...");

        executeAsyncTaskMultiThreading(new RankArticleListAsyncTask() {
            @Override
            public HasAsync<Map<String, List<RankArticle>>> getAsyncContext() {
                return Top10Activity.this;
            }
        }
        );

    }

    public void loadRanks(final Top10ArticleListFragment fragment) {
        if (!isLoading && !allRankArticlesMap.isEmpty()) {
            fragment.updateData(allRankArticlesMap);
        }
    }

    public String getLastLoadTimeText() {
        return dateFormat.format(lastLoadTime);
    }

    private void rotateRefreshActionView() {
        if (refreshMenuItem != null) {
            /* Attach a rotating ImageView to the refresh item as an ActionView */
            refreshActionView.startAnimation(clockWiseRotationAnimation);
            refreshMenuItem.setActionView(refreshActionView);
        }
    }

    private void dismissRefreshActionView() {
        if (refreshMenuItem != null) {
            View actionView = refreshMenuItem.getActionView();
            if (actionView != null) {
                actionView.clearAnimation();
                refreshMenuItem.setActionView(null);
            }
        }
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) getApplication();
    }

    public void onProgressShow() {
        // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
        setProgressBarIndeterminate(true);
        setProgressBarVisibility(true);
        rotateRefreshActionView();
        for (final Top10ArticleListFragment fragment : fragments) {
            if (fragment != null) {
//                        fragment.showProgressUI();
            }
        }

    }

    public void onProgressDismiss() {
        setProgressBarVisibility(false);
        dismissRefreshActionView();
        for (final Top10ArticleListFragment fragment : fragments) {
            if (fragment != null) {
//                fragment.dismissProgressUI();
            }
        }
    }

    public void onSuccess(AsyncResult<Map<String, List<RankArticle>>> mapAsyncResult) {
        Map<String, List<RankArticle>> rankArticlesMap = mapAsyncResult.getResult();
        if (rankArticlesMap != null) {
            lastLoadTime = new Date();
            allRankArticlesMap.clear();
            allRankArticlesMap.putAll(rankArticlesMap);

            for (final Top10ArticleListFragment fragment : fragments) {
                if (fragment != null) {
/*
                    handler.post(new Runnable() {
                        public void run() {
*/
                            fragment.updateData(allRankArticlesMap);
/*
                        }
                    });
*/
                }
            }
        }
        isLoading = false;

    }

    public void onFailure(AsyncResult<Map<String, List<RankArticle>>> mapAsyncResult) {

    }
}
