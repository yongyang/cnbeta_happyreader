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
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.loader.AbstractListLoader;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.adapter.ActionTabFragmentPagerAdapter;

public class TypesActivity extends BaseActivity {

    public final static int[] tabs = new int[]{R.string.tab_dig, R.string.tab_software, R.string.tab_industry, R.string.tab_interact};
    private final AbstractAsyncListFragment[] fragments = new AbstractAsyncListFragment[tabs.length];

    private ViewPager mViewPager;
    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        protected ActionBar getActionBar() {
            return TypesActivity.this.getActionBar();
        }

        @Override
        protected ViewPager getViewPager() {
            return mViewPager;
        }

        @Override
        public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        if(fragments[0] == null) {
                            fragments[0] = newArticleListFragment(ArticleListLoader.Type.DIG);
                        }
                        return fragments[0];
                    case 1:
                        if(fragments[1] == null) {
                            fragments[1] = newArticleListFragment(ArticleListLoader.Type.SOFT);
                        }
                        return fragments[1];
                    case 2:
                        if(fragments[2] == null) {
                            fragments[2] = newArticleListFragment(ArticleListLoader.Type.INDUSTRY);
                        }
                        return fragments[2];
                    case 3:
                        if(fragments[3] == null) {
                            fragments[3] = newArticleListFragment(ArticleListLoader.Type.INTERACT);
                        }
                        return fragments[3];
                    default:
                        // 4 tabs
                        return null;
                }
        }
    };

    private ArticleListFragment newArticleListFragment(AbstractListLoader.Type type){
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

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
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
                //refresh actionitem
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
        return ((CnBetaApplication)getApplicationContext()).onOptionsItemSelected(this, mi);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 每次都会调用该方法, 可以动态改变 menu
        return super.onPrepareOptionsMenu(menu);
    }

}
