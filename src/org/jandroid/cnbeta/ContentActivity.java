package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import org.jandroid.cnbeta.async.ArticleCommentsAsyncTask;
import org.jandroid.cnbeta.async.ArticleContentAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.ImageBytesLoadingAsyncTask;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.fragment.ArticleCommentsFragment;
import org.jandroid.cnbeta.fragment.ArticleContentFragment;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

public class ContentActivity extends BaseActivity implements HasAsync<Content> {

    private ArticleContentFragment contentFragment;
    private ArticleCommentsFragment commentsFragment;

    private long sid;
    private String title;
    private Content content = null;

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
            if (position != actionBar.getSelectedNavigationIndex()) {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
//        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);

        sid = getIntent().getExtras().getLong("sid");
        title = getIntent().getExtras().getString("title");

        setContentView(R.layout.content);
        setupViewPager();
        setupActionBar();

        // load content only once
        loadArticleContent();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getMenuInflater().inflate(R.menu.content_menu, menu);
        //add  refresh actionitem
        getMenuInflater().inflate(R.menu.search_refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        ((CnBetaApplication)getApplicationContext()).onOptionsItemSelected(this, mi);
        switch (mi.getItemId()) {
            case R.id.refresh_item:
                loadArticleContent(); // will load content and comments
                break;
            case R.id.comment_item:
                Utils.openPublishCommentActivityForResult(this, getContent().getSid());
                break;
        }

        return true;
    }

    public static abstract class ActionTabFragmentPagerAdapter extends FragmentPagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        protected ActionTabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
    }

    private void loadArticleContent() {
        executeAsyncTaskMultiThreading(new ArticleContentAsyncTask() {

            @Override
            protected long getSid() {
                return getArticleSid();
            }

            @Override
            public HasAsync<Content> getAsyncContext() {
                return ContentActivity.this;
            }
        }
        );
    }

    public void loadImages() {
        for (String image : content.getImages()) {
            loadImage(image);
        }
    }

    private void loadImage(final String imgSrc) {
        executeAsyncTaskMultiThreading(new ImageBytesLoadingAsyncTask() {
            @Override
            protected String getImageUrl() {
                return imgSrc;
            }

            @Override
            public HasAsync<byte[]> getAsyncContext() {
                return new HasAsync<byte[]>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplicationContext) getApplicationContext();
                    }

                    public void onProgressShow() {

                    }

                    public void onProgressDismiss() {

                    }

                    public void onSuccess(AsyncResult<byte[]> asyncResult) {
                        String id = Base64.encodeToString(imgSrc.getBytes(), Base64.NO_WRAP);
                        //update image in WebView by javascript
                        contentFragment.updateImage(id, asyncResult.getResult());
                    }

                    public void onFailure(AsyncResult<byte[]> asyncResult) {

                    }
                };
            }
        }
        );

    }

    public void loadComments() {
        executeAsyncTaskMultiThreading(new ArticleCommentsAsyncTask() {
            @Override
            protected Content getArticleContent() {
                return content;
            }

            @Override
            public HasAsync<List<Comment>> getAsyncContext() {
                return new HasAsync<List<Comment>>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplicationContext) getApplicationContext();
                    }

                    public void onProgressShow() {
                        ContentActivity.this.onProgressShow();
                    }

                    public void onProgressDismiss() {
                        ContentActivity.this.onProgressDismiss();
                    }

                    public void onSuccess(AsyncResult<List<Comment>> listAsyncResult) {
                        List<Comment> comments = listAsyncResult.getResult();
                        //update view_number, update comment fragment
                        contentFragment.updateCommentNumbers(content);
                        commentsFragment.updateComments(comments);
                    }

                    public void onFailure(AsyncResult<List<Comment>> listAsyncResult) {

                    }
                };
            }
        }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==0 && resultCode ==0) {
            if(data != null && data.hasExtra("comment")) { // 直接 finish 时， data==null
                Comment comment = (Comment)data.getSerializableExtra("comment");
                appendComment(comment);
            }
        }
    }

    // 发布/回复一个新评论时调用该方法即使显示
    private void appendComment(Comment comment){
        commentsFragment.appendComment(comment);
        if(this.getActionBar().getSelectedNavigationIndex() == 0) { // 选中 comment fragment
            this.getActionBar().setSelectedNavigationItem(1);
        }
    }

    public Content getContent() {
        return content;
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) getApplicationContext();
    }

    public void onProgressShow() {
        setProgressBarIndeterminate(true);
        setProgressBarVisibility(true);
    }

    public void onProgressDismiss() {
        setProgressBarVisibility(false);
    }

    public void onSuccess(AsyncResult<Content> contentAsyncResult) {
        content = contentAsyncResult.getResult();
        //update content in ContentActivity
        if(content == null) { //TODO: 偶尔 content == null
            logger.w("Content is null???", new NullPointerException());
        }
        contentFragment.updateArticleContent(content);
    }

    public void onFailure(AsyncResult<Content> contentAsyncResult) {

    }
}
