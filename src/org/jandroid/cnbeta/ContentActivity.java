package org.jandroid.cnbeta;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.entity.HistoryComment;
import org.jandroid.cnbeta.fragment.ArticleCommentsFragment;
import org.jandroid.cnbeta.fragment.ArticleContentFragment;
import org.jandroid.cnbeta.fragment.ArticleHotCommentsFragment;
import org.jandroid.cnbeta.loader.HistoryCommentListLoader;
import org.jandroid.common.DateFormatUtils;
import org.jandroid.common.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentActivity extends CnBetaActionTabFragmentActivity {

    private final static int[] tabs = new int[]{R.string.tab_zhengwen, R.string.tab_zuirepinglun, R.string.tab_pinglun};

    private ArticleContentFragment contentFragment;
    private ArticleHotCommentsFragment hotCommentsFragment;
    private ArticleCommentsFragment commentsFragment;

    private long sid;
    private String title;

    @Override
    protected Fragment getTabFragmentByItem(int position) {
        if(position == 0) {
            return contentFragment;
        }
        else if(position == 1) {
            return hotCommentsFragment;
        }
        else if(position == 2) {
            return commentsFragment;
        }
        else {
            return null;
        }
    }

    @Override
    protected int[] getTabResourceIds() {
        return tabs;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        sid = getIntent().getExtras().getLong("sid");
        title = getIntent().getExtras().getString("title");

        contentFragment = new ArticleContentFragment();
        commentsFragment = new ArticleCommentsFragment();
        hotCommentsFragment = new ArticleHotCommentsFragment();

        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        // set limit to 2 so comments(tab index 2) can be loaded automaticaly
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void onThemeChanged() {
        super.onThemeChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add  refresh actionitem
        getMenuInflater().inflate(R.menu.content_action_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (!super.onOptionsItemSelected(mi) && !((CnBetaApplicationContext) getApplicationContext()).onOptionsItemSelected(this, mi)) {
            switch (mi.getItemId()) {
                case R.id.refresh_item:
                    reload(); // will reload comments, and update comment Numbers
                    break;
                case R.id.comment_item:
                    Utils.openPublishCommentActivityForResult(this, getContent().getSid());
                    break;
            }
        }
        return true;
    }

    private void reload() {
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

    // 发布/回复一个新评论时调用该方法及时显示
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

    public void updateHotComments(List<Comment> comments) {
        List<Comment> hotComments = new ArrayList<Comment>();
        for(Comment comment : comments){
            if(comment.isHot()) {
                hotComments.add(comment);
            }
        }
        if(!hotComments.isEmpty()) {
            hotCommentsFragment.clearData();
            hotCommentsFragment.appendDatas(hotComments);
        }
    }

    public Content getContent() {
        return contentFragment.getContent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
