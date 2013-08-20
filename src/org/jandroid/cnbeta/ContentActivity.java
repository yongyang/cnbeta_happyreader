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
import android.widget.Toast;
import org.jandroid.cnbeta.async.ArticleContentAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.fragment.ArticleCommentsFragment;
import org.jandroid.cnbeta.fragment.ArticleContentFragment;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends Activity {
    public final static int[] tabs = new int[]{R.string.tab_zhengwen, R.string.tab_pinglun};
    private List<Fragment> tabFragments = new ArrayList<Fragment>();

    private final Content content = new Content();
    
    private ViewPager mViewPager;
    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        public int getCount() {
            return ContentActivity.this.getActionBar().getTabCount();
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
            mViewPager.setCurrentItem(ContentActivity.this.getActionBar().getSelectedNavigationIndex());
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    };


   	@Override
   	public void onCreate(Bundle savedInstanceState){
   		super.onCreate(savedInstanceState);

   		setContentView(R.layout.content);
        setupViewPager();
        setupActionBar();

   	}

    private void setupActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        actionBar.addTab(actionBar.newTab().setText(R.string.tab_zhengwen).setTabListener(pagerAdapter));
        tabFragments.add(new ArticleContentFragment());
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_pinglun).setTabListener(pagerAdapter));
        tabFragments.add(new ArticleCommentsFragment());
        pagerAdapter.notifyDataSetChanged();
    }

    private void setupViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.content_viewpager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   		getMenuInflater().inflate(R.menu.main_menu, menu);
   		return super.onCreateOptionsMenu(menu);
   	}
    @Override
   	public boolean onOptionsItemSelected(MenuItem mi) {
        if(mi.isCheckable())
      		{
      			mi.setChecked(true);
      		}
      		switch (mi.getItemId()) {
                case android.R.id.home:
                    Intent intent = new Intent(this, MainActivity.class);
                    this.startActivity(intent);
                    break;
                case R.id.more_item:
                default:
                    Toast.makeText(this, "点击了" + mi.toString(),Toast.LENGTH_SHORT).show();

            }

   		return true;
   	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    public static abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }

    
    //TODO:
    public void updateContentFragment() {
        
    }

    //TODO:
    public void updateCommentFragment() {
        
    }

    private void loadContent(){
        new ArticleContentAsyncTask(){

            @Override
            protected int getSid() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void showProgressUI() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void dismissProgressUI() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext)getApplicationContext();
            }

            @Override
            protected void onPostExecute(AsyncResult r) {
                super.onPostExecute(r);
                //TODO: update content in ContentActivity
                //TODO: load info and comments by ArticleCommentsLoader Async
            }
        }.executeMultiThread();
    }


}
