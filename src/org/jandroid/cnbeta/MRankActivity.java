package org.jandroid.cnbeta;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import org.jandroid.cnbeta.fragment.MRankListFragment;
import org.jandroid.cnbeta.loader.MRankListLoader;

public class MRankActivity extends AbstractActionTabFragmentActivity {

    private final static int[] tabs = new int[]{R.string.tab_hot, R.string.tab_argue, R.string.tab_recommend};

    @Override
    protected Fragment getTabFragmentByItem(int position) {
        switch (position) {
            case 0:
                if (fragments[0] == null) {
                    fragments[0] = newMRankListFragment(MRankListLoader.Type.HOT);
                }
                return fragments[0];
            case 1:
                if (fragments[1] == null) {
                    fragments[1] = newMRankListFragment(MRankListLoader.Type.ARGUE);
                }
                return fragments[1];
            case 2:
                if (fragments[2] == null) {
                    fragments[2] = newMRankListFragment(MRankListLoader.Type.RECOMMEND);
                }
                return fragments[2];
            default:
                // 3 tabs
                return null;
        }

    }

    @Override
    protected int[] getTabResourceIds() {
        return tabs;
    }

    private final MRankListFragment[] fragments = new MRankListFragment[3];


    private MRankListFragment newMRankListFragment(MRankListLoader.Type type) {
        MRankListFragment fragment = new MRankListFragment();
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
