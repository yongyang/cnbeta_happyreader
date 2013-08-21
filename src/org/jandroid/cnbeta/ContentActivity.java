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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import org.jandroid.cnbeta.async.ArticleContentAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.fragment.ArticleCommentsFragment;
import org.jandroid.cnbeta.fragment.ArticleContentFragment;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends Activity {
    
    private ArticleContentFragment contentFragment;
    private ArticleCommentsFragment commentsFragment;

    private Article article = null;
    private Content content = null;

    private MenuItem refreshMenuItem;
    private ImageView refreshActionView;
    private Animation clockWiseRotationAnimation;


    private ViewPager mViewPager;
    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        public int getCount() {
            return ContentActivity.this.getActionBar().getTabCount();
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? contentFragment : commentsFragment;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        public void onPageSelected(int position) {
            final ActionBar actionBar = getActionBar();
            //未选中时才调用setSelectedNavigationItem,
            if(position != actionBar.getSelectedNavigationIndex()){
                actionBar.setSelectedNavigationItem(position);
            }

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
        article = (Article)getIntent().getExtras().getSerializable("article");

   		setContentView(R.layout.content);
        setupViewPager();
        setupActionBar();

        refreshActionView = (ImageView) getLayoutInflater().inflate(R.layout.iv_refresh_action_view, null);
        clockWiseRotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation_clockwise_refresh);
        clockWiseRotationAnimation.setRepeatCount(Animation.INFINITE);

    }

    private void setupActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab().setText(R.string.tab_zhengwen).setTabListener(pagerAdapter));
        contentFragment = new ArticleContentFragment();
        actionBar.addTab(actionBar.newTab().setText(R.string.tab_pinglun).setTabListener(pagerAdapter));
        commentsFragment = new ArticleCommentsFragment();
        pagerAdapter.notifyDataSetChanged();
    }

    private void setupViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.content_viewpager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(pagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadContent();
    }

    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   		getMenuInflater().inflate(R.menu.main_menu, menu);
        //refresh actionitem
        getMenuInflater().inflate(R.menu.article_list_fragment_menu, menu);
        refreshMenuItem = menu.findItem(R.id.refresh_item);

        return super.onCreateOptionsMenu(menu);
   	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载文章中...");

        new ArticleContentAsyncTask(){

            @Override
            protected long getSid() {
                return article.getSid();
            }

            @Override
            public void showProgressUI() {
                // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
                setProgressBarIndeterminate(true);
                setProgressBarVisibility(true);

                progressDialog.show();
                rotateRefreshActionView();
            }

            @Override
            public void dismissProgressUI() {
                setProgressBarVisibility(false);
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext)getApplicationContext();
            }

            @Override
            protected void onPostExecute(AsyncResult asyncResult) {
                super.onPostExecute(asyncResult);
                if(asyncResult.isSuccess()) {
                    content = (Content)asyncResult.getResult();
                    //TODO: update content in ContentActivity
                    //TODO: load info and comments by ArticleCommentsLoader Async
                    contentFragment.updateContent(content);
                    //loadComments();
                }
                else {
                    Toast.makeText(ContentActivity.this, asyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }.executeMultiThread();
    }
    
    
    private void loadComments(){
        
    }


    private void rotateRefreshActionView() {
        if(refreshMenuItem != null) {
            /* Attach a rotating ImageView to the refresh item as an ActionView */
            refreshActionView.startAnimation(clockWiseRotationAnimation);
            refreshMenuItem.setActionView(refreshActionView);
        }
    }

    private void dismissRefreshActionView() {
        if(refreshMenuItem != null) {
            View actionView = refreshMenuItem.getActionView();
            if(actionView != null) {
                actionView.clearAnimation();
                refreshMenuItem.setActionView(null);
            }
        }
    }

}
