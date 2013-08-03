package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.util.IntentUtils;

//TODO: 动态替换 tabs 来显示各分类文章，而不是新建 Activity
public class TypesActivity extends Activity {

    public static abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }

    private static final String SELECTED_ITEM = "selected_item";

    public final static int[] tabs2 = new int[]{R.string.tab_dig, R.string.tab_software, R.string.tab_industry, R.string.tab_interact};
    private final Fragment[] fragments2 = new Fragment[tabs2.length];

    private ViewPager mViewPager;
    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        public int getCount() {
            return TypesActivity.this.getActionBar().getTabCount();
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        if(fragments2[0] == null) {
                            fragments2[0] = new ArticleListFragment(ArticleListLoader.Type.DIG);
                        }
                        return fragments2[0];
                    case 1:
                        if(fragments2[1] == null) {
                            fragments2[1] = new ArticleListFragment(ArticleListLoader.Type.SOFT);
                        }
                        return fragments2[1];
                    case 2:
                        if(fragments2[2] == null) {
                            fragments2[2] = new ArticleListFragment(ArticleListLoader.Type.INDUSTRY);
                        }
                        return fragments2[2];
                    case 3:
                        if(fragments2[3] == null) {
                            fragments2[3] = new ArticleListFragment(ArticleListLoader.Type.INTERACT);
                        }
                        return fragments2[3];
                    default:
                        // 4 tabs
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
            mViewPager.setCurrentItem(TypesActivity.this.getActionBar().getSelectedNavigationIndex());
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
        for(int resourceId : tabs2){
            //全部资讯, 实时更新, 阅读历史
            actionBar.addTab(actionBar.newTab().setText(resourceId).setTabListener(pagerAdapter));
        }
        pagerAdapter.notifyDataSetChanged();
    }

    private void setupViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
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

}
