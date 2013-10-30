package org.jandroid.cnbeta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.jandroid.cnbeta.fragment.AbstractAsyncListFragment;
import org.jandroid.cnbeta.fragment.ArticleListFragment;
import org.jandroid.cnbeta.loader.AbstractListLoader;
import org.jandroid.cnbeta.loader.ArticleListLoader;

public class TypesActivity extends AbstractActionTabFragmentActivity {

    public final static int[] tabs = new int[]{R.string.tab_dig, R.string.tab_software, R.string.tab_industry, R.string.tab_interact};
    private final AbstractAsyncListFragment[] fragments = new AbstractAsyncListFragment[tabs.length];

    @Override
    protected Fragment getTabFragmentByItem(int position) {
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

    @Override
    protected int[] getTabResourceIds() {
        return tabs;
    }

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
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
