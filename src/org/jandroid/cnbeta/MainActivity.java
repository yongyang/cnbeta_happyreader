package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.fragment.RealtimeArticleListFragment;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.IntentUtils;

//TODO: 动态替换 tabs 来显示各分类文章，而不是新建 Activity
public class MainActivity extends BaseActivity {

    public static abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }

    private static final String SELECTED_ITEM = "selected_item";

    public final static int[] tabs = new int[]{R.string.tab_quanbuzixun, R.string.tab_shishigengxin, R.string.tab_yuedulishi};
    private final Fragment[] fragments = new Fragment[tabs.length];

    private ViewPager mViewPager;

    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        public int getCount() {
            return MainActivity.this.getActionBar().getTabCount();
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        if(fragments[0] == null) {
                            fragments[0] = new ArticleListFragment(ArticleListLoader.Type.ALL);
                        }
                        return fragments[0];
                    case 1:
                        if(fragments[1] == null) {
                            fragments[1] = new RealtimeArticleListFragment();
                        }
                        return fragments[1];
                    case 2:
                        //TODO: 阅读历史 tab
                        if(fragments[2] == null) {
                            fragments[2] = new ArticleListFragment(ArticleListLoader.Type.DIG);
                        }
                        return fragments[2];

                    default:
                        // only 3 tabs
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
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
//        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);
        setContentView(R.layout.main);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
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
        for(int resourceId : tabs){
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
                break;
            case R.id.dig_soft_industry_interact:
                startActivity(IntentUtils.newIntent(this, TypesActivity.class));
                this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.rank:
                startActivity(IntentUtils.newIntent(this, Top10Activity.class));
                this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.more_item:
                break;
            case R.id.aboutus_item:
                //TODO:  for test
                Intent intent = new Intent(this, ContentActivity.class);
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // 默认将 finish activity
                        MainActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // destroy application
        ((CnBetaApplication)getApplicationContext()).destroy();
    }
}
