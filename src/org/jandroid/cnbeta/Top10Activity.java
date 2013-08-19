package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.async.RankArticleListAsyncTask;
import org.jandroid.cnbeta.entity.RankArticle;
import org.jandroid.cnbeta.fragment.Top10ArticleListFragment;
import org.jandroid.util.EnvironmentUtils;
import org.jandroid.util.IntentUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Top10Activity extends Activity {

    private final Map<String, List<RankArticle>> allRankArticlesMap = new HashMap<String, List<RankArticle>>();

   //TODO: set lastLoadTime to refresh time in Fragment
    private Date lastLoadTime = new Date();
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String SELECTED_ITEM = "selected_item";

    public final static int[] tabs = new int[]{R.string.tab_hits24, R.string.tab_comments24, R.string.tab_recommend};
    private final Top10ArticleListFragment[] fragments = new Top10ArticleListFragment[tabs.length];

    private ViewPager mViewPager;

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
                        if(fragments[0] == null) {
                            fragments[0] = new Top10ArticleListFragment(RankType.HITS24, RankType.HITS_WEEK, RankType.HITS_MONTH);
                        }
                        return fragments[0];
                    case 1:
                        if(fragments[1] == null) {
                            fragments[1] = new Top10ArticleListFragment(RankType.COMMENTS24, RankType.COMMENTS_WEEK, RankType.COMMENTS_MONTH);
                        }
                        return fragments[1];
                    case 2:
                        if(fragments[2] == null) {
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
            actionBar.setSelectedNavigationItem(position);

        }

        public void onPageScrollStateChanged(int state) {

        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            mViewPager.setCurrentItem(Top10Activity.this.getActionBar().getSelectedNavigationIndex());
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

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
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setupViewPager();
        setupActionBar();

        if (savedInstanceState != null) {
//            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

    }

    private void setupActionBar() {
        final ActionBar actionBar = getActionBar();
        for(int resourceId : tabs){
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (mi.isCheckable()) {
            mi.setChecked(true);
        }
        switch (mi.getItemId()) {
            case android.R.id.home:
            case R.id.main:
                startActivity(IntentUtils.newIntent(this, MainActivity.class));
                this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.dig_soft_industry_interact:
                break;
            case R.id.more_item:
                break;
            case R.id.aboutus_item:
                //TODO:  for test
                Intent intent = new Intent(this, ArticleContentActivity.class);
                this.startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 每次都会调用该方法, 可以动态改变 menu
        return super.onPrepareOptionsMenu(menu);
    }

    public void reloadRanks(final Top10ArticleListFragment.RankLoadCallback callback){
        EnvironmentUtils.checkNetworkConnected(this);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载排行信息中...");

        new RankArticleListAsyncTask(){
            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext)getApplication();
            }

            @Override
            public void showProgressUI() {
                progressDialog.show();
                callback.showProgressUI();
            }

            @Override
            public void dismissProgressUI() {
                callback.dismissProgressUI();
                progressDialog.dismiss();
            }

            @Override
            protected void onPostExecute(AsyncResult asyncResult) {
                super.onPostExecute(asyncResult);
                if(asyncResult.isSuccess()) {
                    Map<String, List<RankArticle>>  articlesMap = (Map<String, List<RankArticle>> )asyncResult.getResult();
                    if(articlesMap != null) {
                        lastLoadTime = new Date();
                        allRankArticlesMap.clear();
                        allRankArticlesMap.putAll(articlesMap);
                        callback.onRankLoadFinished(articlesMap);
                    }
                }
                else {
                    Toast.makeText(Top10Activity.this, asyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }.executeMultiThread();
    }

    public void loadRanks(final Top10ArticleListFragment.RankLoadCallback callback) {
        callback.onRankLoadFinished(allRankArticlesMap);
    }

    public String getLastLoadTimeText() {
        return dateFormat.format(lastLoadTime);
    }
}
