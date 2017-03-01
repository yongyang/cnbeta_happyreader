package org.jandroid.cnbeta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import org.jandroid.cnbeta.fragment.AbstractAsyncListFragment;
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.fragment.EditorRecommendListFragment;
import org.jandroid.cnbeta.fragment.HotCommentListFragment;
import org.jandroid.cnbeta.fragment.RealtimeArticleListFragment;
import org.jandroid.cnbeta.loader.AbstractListLoader;

public class MainActivity extends CnBetaActionTabFragmentActivity {

    private final static int[] tabs = new int[]{R.string.tab_quanbuzixun, R.string.tab_shishigengxin, R.string.tab_bianjituijian, R.string.tab_jingcaipinglun};
    private final AbstractAsyncListFragment[] fragments = new AbstractAsyncListFragment[tabs.length];

    @Override
    protected Fragment getTabFragmentByItem(int position) {
        switch (position) {
            case 0:
                if (fragments[0] == null) {
                    fragments[0] = newArticleListFragment(AbstractListLoader.Type.ALL);
                }
                return fragments[0];
            case 1:
                if (fragments[1] == null) {
                    fragments[1] = new RealtimeArticleListFragment();
                }
                return fragments[1];
            case 2:
                //编辑推荐 tab
                if (fragments[2] == null) {
                    fragments[2] = new EditorRecommendListFragment();
                }
                return fragments[2];
            case 3:
                //精彩评论 tab
                if (fragments[3] == null) {
                    fragments[3] = new HotCommentListFragment();
                }
                return fragments[3];

            default:
                // only 4 tabs
                return null;
        }
    }

    @Override
    protected int[] getTabResourceIds() {
        return tabs;
    }

    private ArticleListFragment newArticleListFragment(AbstractListLoader.Type type) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        if (!super.onOptionsItemSelected(mi) && !((CnBetaApplicationContext) getApplicationContext()).onOptionsItemSelected(this, mi)) {
            switch (mi.getItemId()) {
                case R.id.refresh_item:
                    if(!isFinishing()){
                        AbstractAsyncListFragment currentFragment = fragments[getActionBar().getSelectedNavigationIndex()];
                        if(currentFragment != null) {
                            currentFragment.reloadData();
                        }
                    }
                    break;
            }
        }
        return true;

    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else {
            new Thread() {
                @Override
                public void run() {
                    ((CnBetaApplication) getApplicationContext()).onExit();
                }
            }.start();
            super.onBackPressed();
        }
    }
}
