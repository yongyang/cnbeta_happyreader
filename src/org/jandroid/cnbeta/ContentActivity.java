package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import org.jandroid.cnbeta.async.ArticleCommentsAsyncTask;
import org.jandroid.cnbeta.async.ArticleContentAsyncTask;
import org.jandroid.cnbeta.async.ImageBytesLoadingAsyncTask;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.common.async.AsyncResult;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.fragment.ArticleCommentsFragment;
import org.jandroid.cnbeta.fragment.ArticleContentFragment;
import org.jandroid.common.BaseActivity;

import java.util.List;

public class ContentActivity extends BaseActivity {
    
    private ArticleContentFragment contentFragment;
    private ArticleCommentsFragment commentsFragment;

    private long sid;
    private String title;
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
        sid = getIntent().getExtras().getLong("sid");
        title = getIntent().getExtras().getString("title");

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

    public long getArticleSid() {
        return sid;
    }

    public String getArticleTitle() {
        return title;
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

    private void loadContent(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载文章中...");

        executeAsyncTaskMultiThreading(new ArticleContentAsyncTask() {

            @Override
            protected long getSid() {
                return sid;
            }

            @Override
            public void showProgressUI() {
                // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
                setProgressBarIndeterminate(true);
                setProgressBarVisibility(true);

                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                    }
                });
                progressDialog.show();
                rotateRefreshActionView();
            }

            @Override
            public void dismissProgressUI() {
                setProgressBarVisibility(false);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                dismissRefreshActionView();
            }

            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext) getApplicationContext();
            }

            @Override
            protected void onPostExecute(AsyncResult<Content> asyncResult) {
                super.onPostExecute(asyncResult);
                if (asyncResult.isSuccess()) {
                    content = asyncResult.getResult();
                    //update content in ContentActivity
                    contentFragment.updateArticleContent(content);
                }
                else {
                    logger.w(asyncResult.getErrorMsg(), asyncResult.getException());
                    Toast.makeText(ContentActivity.this, asyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }
        );
    }

    public void loadImages(){
        for(String image : content.getImages()){
            loadImage(image);
        }
    }
    
    private void loadImage(final String imgSrc){
        executeAsyncTaskMultiThreading(new ImageBytesLoadingAsyncTask() {
            @Override
            protected String getImageUrl() {
                return imgSrc;
            }

            @Override
            protected void onPostExecute(AsyncResult<byte[]> asyncResult) {
                super.onPostExecute(asyncResult);
                if(asyncResult.isSuccess()) {
                    String id = Base64.encodeToString(imgSrc.getBytes(), Base64.NO_WRAP);
                    //update image in WebView by javascript
                    contentFragment.updateImage(id, asyncResult.getResult());
                }
                else {
                    logger.w(asyncResult.getErrorMsg());
                }
            }

            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext) getApplicationContext();
            }
        }
        );
        
    }
    
    public void loadComments(){
        executeAsyncTaskMultiThreading(new ArticleCommentsAsyncTask() {
            @Override
            protected Content getArticleContent() {
                return content;
            }

            @Override
            public void showProgressUI() {
            }

            @Override
            public void dismissProgressUI() {
            }

            @Override
            protected void onPostExecute(AsyncResult<List<Comment>> asyncResult) {
                super.onPostExecute(asyncResult);
                if (asyncResult.isSuccess()) {
                    List<Comment> comments = asyncResult.getResult();
                    //TODO: update view_number, update comment fragment
                    contentFragment.updateCommentNumbers(content);
                    commentsFragment.updateComments(comments);
                }
                else {
                    logger.w(asyncResult.getErrorMsg(), asyncResult.getException());
                    Toast.makeText(ContentActivity.this, asyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext) getApplicationContext();
            }
        }
        );
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

    public Content getContent() {
        return content;
    }
}
