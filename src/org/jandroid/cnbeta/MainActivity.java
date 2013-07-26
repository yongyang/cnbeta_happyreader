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
import android.widget.Toast;
import org.jandroid.cnbeta.async.ArticleListAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.fragment.ArticleListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private static final String SELECTED_ITEM = "selected_item";

    public final static int[] tabs = new int[]{R.string.tab_quanbuzixun, R.string.tab_shishigengxin};
    public final static int[] tags = new int[]{R.string.tab_tag_quanbuzixun, R.string.tab_tag_shishigengxin};

    // all articles for tabs, tab_tag => articles
    private Map<String, List<Article>> articles = new HashMap<String, List<Article>>();

    private ViewPager mViewPager;

    private List<Fragment> tabFragments = new ArrayList<Fragment>();

    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {
        @Override
        public int getCount() {
            return MainActivity.this.getActionBar().getTabCount();
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
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
            mViewPager.setCurrentItem(MainActivity.this.getActionBar().getSelectedNavigationIndex());
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpViewPager();
        setUpActionBar();

        if (savedInstanceState != null) {
//            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }

    }

    private void setUpActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (int i = 0; i < tabs.length; i++) {
            int resourceId = tabs[i];
            int tagResourceId = tags[i];
            ActionBar.Tab tab = actionBar.newTab().setText(resourceId).setTag(getResources().getString(tagResourceId)).setTabListener(pagerAdapter);
            tabFragments.add(new ArticleListFragment());
            actionBar.addTab(tab);
            pagerAdapter.notifyDataSetChanged();
        }
    }

    private void setUpViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
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

    public void loadArticleList(final String category, final int page) {
        new ArticleListAsyncTask(category, page) {
            @Override
            public ProgressDialog getProgressDialog() {
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("加载新闻列表");
                return progressDialog;
            }

            @Override
            protected void onPostExecute(AsyncResult<List<Article>> listAsyncResult) {
                super.onPostExecute(listAsyncResult);
                List<Article> articles = listAsyncResult.getResult();
            }
        }.executeMultiThread();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (mi.isCheckable()) {
            mi.setChecked(true);
        }
        switch (mi.getItemId()) {
            case android.R.id.home:
            case R.id.more_item:
                break;
            case R.id.aboutus_item:
                //TODO:  for test
                Intent intent = new Intent(this, ArticleContentActivity.class);
                this.startActivity(intent);
                break;
            default:
                Toast.makeText(this, "点击了" + mi.toString(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 每次都会调用该方法, 可以动态改变 menu
        return super.onPrepareOptionsMenu(menu);
    }

    public void reloadArticles(ActionBar.Tab tab) {

    }

    public static abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }
}
