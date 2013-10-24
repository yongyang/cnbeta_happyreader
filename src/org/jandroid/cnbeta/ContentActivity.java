package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.entity.HistoryComment;
import org.jandroid.cnbeta.fragment.ArticleCommentsFragment;
import org.jandroid.cnbeta.fragment.ArticleContentFragment;
import org.jandroid.cnbeta.loader.HistoryCommentListLoader;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.DateFormatUtils;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.adapter.ActionTabFragmentPagerAdapter;

import java.util.Date;

public class ContentActivity extends BaseActivity {

    private ArticleContentFragment contentFragment;
    private ArticleCommentsFragment commentsFragment;

    private long sid;
    private String title;

    private ViewPager mViewPager;
    private ActionTabFragmentPagerAdapter pagerAdapter = new ActionTabFragmentPagerAdapter(this.getFragmentManager()) {

        @Override
        protected ActionBar getActionBar() {
            return ContentActivity.this.getActionBar();
        }

        @Override
        protected ViewPager getViewPager() {
            return mViewPager;
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? contentFragment : commentsFragment;
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_PROGRESS);
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        this.setProgressBarIndeterminate(true);

        sid = getIntent().getExtras().getLong("sid");
        title = getIntent().getExtras().getString("title");

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

    public boolean isPageLoaded() {
        return contentFragment.getContent() != null;
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
        getMenuInflater().inflate(R.menu.default_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        ((CnBetaApplication) getApplicationContext()).onOptionsItemSelected(this, mi);
        switch (mi.getItemId()) {
            case R.id.refresh_item:
                reload(); // will reload comments, and update comment Numbers
                break;
            case R.id.comment_item:
                Utils.openPublishCommentActivityForResult(this, getContent().getSid());
                break;
        }

        return true;
    }

    public void reload() {
        contentFragment.reloadContent();
    }

    // call by ArticleContentFragment
    public void reloadComments() {
        commentsFragment.reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            if (data != null && data.hasExtra("comment")) { // 直接 finish 时， data==null
                final Comment newComment = (Comment) data.getSerializableExtra("comment");
                newPostedComment(newComment);
                // write history
                new Thread() {
                    @Override
                    public void run() {
                        HistoryComment historyComment = new HistoryComment();
                        historyComment.setSid(getArticleSid());
                        historyComment.setComment(newComment.getComment());
                        historyComment.setTitle(getArticleTitle());
                        historyComment.setDate(DateFormatUtils.getDefault().format(new Date()));

                        try {
                            new HistoryCommentListLoader().writeHistory(((CnBetaApplicationContext) getApplicationContext()).getHistoryDir(), historyComment);
                        }
                        catch (final Exception e) {
                            handler.post(new Runnable() {
                                public void run() {
                                    ToastUtils.showShortToast(getApplicationContext(), e.toString());
                                }
                            });
                        }
                    }
                }.start();

            }
        }
    }

    // 发布/回复一个新评论时调用该方法即使显示
    private void newPostedComment(Comment comment) {
        contentFragment.newPostedComment(comment);
        commentsFragment.newPostedComment(comment);
        if (this.getActionBar().getSelectedNavigationIndex() == 0) { // 选中 comment fragment
            this.getActionBar().setSelectedNavigationItem(1);
        }
    }

    public void updateCommentNumbers() {
        contentFragment.updateCommentNumbers();
    }

    public Content getContent() {
        return contentFragment.getContent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
