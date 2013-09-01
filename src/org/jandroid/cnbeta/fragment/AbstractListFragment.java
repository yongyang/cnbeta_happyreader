package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.LoadingAsyncTask;
import org.jandroid.common.AnimateUtils;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.EnvironmentUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public abstract class AbstractListFragment<T> extends BaseFragment implements AdapterView.OnItemClickListener {

    public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected ListView mListView;

    protected final List<T> loadedDatas = new ArrayList<T>();

    protected BaseAdapter adapter;

    protected MenuItem refreshMenuItem;
    protected ImageView refreshActionView;

    public AbstractListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lv_article_list, container, false);
        mListView = (ListView) rootView.findViewById(R.id.article_listview);
        return rootView;
    }

    protected BaseAdapter getAdapter() {
        if (adapter == null) {
            adapter = newAdapter();
        }
        return adapter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = getAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        if (adapter instanceof AbsListView.OnScrollListener) {
            mListView.setOnScrollListener((AbsListView.OnScrollListener) adapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //refresh actionitem
        inflater.inflate(R.menu.article_list_fragment_menu, menu);
        refreshMenuItem = menu.findItem(R.id.refresh_item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
        }

        getCnBetaApplicationContext().onOptionsItemSelected(getActivity(), item);

        return true;
    }


    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) (getActivity().getApplicationContext());
    }


    protected void appendDatas(List<T> datas){
        loadedDatas.addAll(datas);
        getAdapter().notifyDataSetChanged();
    }

    protected abstract BaseAdapter newAdapter();

    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
