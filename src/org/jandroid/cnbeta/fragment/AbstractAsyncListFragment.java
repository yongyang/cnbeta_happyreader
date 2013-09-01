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
import org.jandroid.common.async.AsyncResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public abstract class AbstractAsyncListFragment<T> extends BaseFragment implements HasAsync<List<T>>, AdapterView.OnItemClickListener {

    public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected ListView mListView;

    protected final List<T> loadedDatas = new ArrayList<T>();

    protected BaseAdapter adapter;

    protected MenuItem refreshMenuItem;
    protected ImageView refreshActionView;

    public AbstractAsyncListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        refreshActionView = (ImageView) inflater.inflate(R.layout.iv_refresh_action_view, null);

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
    public void onStart() {
        super.onStart();
        reloadDatas();
    }

    protected void loadDatas() {
        EnvironmentUtils.checkNetworkConnected(getActivity());
        ((BaseActivity) getActivity()).executeAsyncTaskMultiThreading(newAsyncTask());
    }

    protected void reloadDatas() {
        loadedDatas.clear();
        loadDatas();
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

        switch (item.getItemId()) {
            case R.id.more_item:
                break;
            case R.id.refresh_item:
                loadDatas();
            default:
        }
        return true;
    }

    protected void startRotateRefreshActionView() {
        if (refreshMenuItem != null) {
            /* Attach a rotating ImageView to the refresh item as an ActionView */
            AnimateUtils.rotate(refreshActionView);
            refreshMenuItem.setActionView(refreshActionView);
        }
    }

    protected void stopRotateRefreshActionView() {
        if (refreshMenuItem != null) {
            View actionView = refreshMenuItem.getActionView();
            if (actionView != null) {
                actionView.clearAnimation();
                refreshMenuItem.setActionView(null);
            }
        }
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) (getActivity().getApplicationContext());
    }


    public void onProgressShow() {
        // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
        getActivity().setProgressBarIndeterminate(true);
        getActivity().setProgressBarVisibility(true);
        // rotate refresh item
        startRotateRefreshActionView();
    }

    public void onProgressDismiss() {
        getActivity().setProgressBarVisibility(false);
        //Stop refresh animation anyway
        stopRotateRefreshActionView();
    }

    public void onFailure(AsyncResult<List<T>> listAsyncResult) {

    }

    public void onSuccess(AsyncResult<List<T>> listAsyncResult) {
        loadedDatas.addAll(listAsyncResult.getResult());
        getAdapter().notifyDataSetChanged();

    }

    protected abstract BaseAdapter newAdapter();

    public abstract LoadingAsyncTask<List<T>> newAsyncTask();

    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
