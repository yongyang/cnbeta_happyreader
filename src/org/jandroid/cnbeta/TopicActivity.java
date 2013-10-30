package org.jandroid.cnbeta;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.jandroid.cnbeta.fragment.AbstractAsyncListFragment;
import org.jandroid.cnbeta.fragment.TopicArticleListFragment;
import org.jandroid.cnbeta.fragment.TopicListFragment;

public class TopicActivity extends AbstractActionTabFragmentActivity {

    public final static int[] tabs = new int[]{R.string.tab_topics, R.string.tab_topic_articles};
    private final AbstractAsyncListFragment[] fragments = new AbstractAsyncListFragment[tabs.length];

    @Override
    protected Fragment getTabFragmentByItem(int position) {
        switch (position) {
            case 0:
                if (fragments[0] == null) {
                    fragments[0] = new TopicListFragment();
                }
                return fragments[0];
            case 1:
                if (fragments[1] == null) {
                    //默认显示"苹果"主题文章
                    fragments[1] = newTopicArticleListFragment(getTopicId(), getTopicName());
                }
                return fragments[1];
            default:
                // only 2 tabs
                return null;
        }
    }

    @Override
    protected int[] getTabResourceIds() {
        return tabs;
    }

    private long topicId = 0;
    private String topicName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        topicId = getIntent().getLongExtra("id", 9); // 9 is apple
        topicName = getIntent().getStringExtra("name");
        if(topicName == null) {
            topicName = "Apple 苹果";
        }
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("id")) { // 由阅读文章中点击topic图片打开
            getActionBar().setSelectedNavigationItem(1);
        }
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void openTopic(long topicId, String topicName) {
        setTopicId(topicId);
        setTopicName(topicName);
        TopicArticleListFragment fragment = (TopicArticleListFragment)fragments[1];
        fragment.updateTopicId(getTopicId());
        getActionBar().setSelectedNavigationItem(1);
    }

    private TopicArticleListFragment newTopicArticleListFragment(long topicId, String topicName) {
        TopicArticleListFragment fragment = new TopicArticleListFragment();
        Bundle args = new Bundle();
        args.putLong("id", topicId);
        args.putString("name", topicName);
        fragment.setArguments(args);
        return fragment;
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
