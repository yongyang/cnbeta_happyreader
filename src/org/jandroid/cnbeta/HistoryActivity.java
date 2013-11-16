package org.jandroid.cnbeta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import org.jandroid.cnbeta.fragment.AbstractAsyncListFragment;
import org.jandroid.cnbeta.fragment.HistoryArticleListFragment;
import org.jandroid.cnbeta.fragment.HistoryCommentListFragment;

public class HistoryActivity extends AbstractActionTabFragmentActivity {

    private final static int[] tabs = new int[]{R.string.tab_history_article, R.string.tab_history_comment};
    private final AbstractAsyncListFragment[] fragments = new AbstractAsyncListFragment[tabs.length];

    @Override
    protected Fragment getTabFragmentByItem(int position) {
        switch (position) {
            case 0:
                if (fragments[0] == null) {
                    fragments[0] = new HistoryArticleListFragment();
                }
                return fragments[0];
            case 1:
                if (fragments[1] == null) {
                    fragments[1] = new HistoryCommentListFragment();
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (!super.onOptionsItemSelected(mi) && !((CnBetaApplicationContext) getApplicationContext()).onOptionsItemSelected(this, mi)) {
            switch (mi.getItemId()) {
                case R.id.refresh_item:
                    fragments[getActionBar().getSelectedNavigationIndex()].reloadData();
                    break;
            }
        }
        return true;
    }

}
