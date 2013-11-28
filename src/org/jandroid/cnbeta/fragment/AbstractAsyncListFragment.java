package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.exception.InfoException;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.ThemeFragment;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public abstract class AbstractAsyncListFragment<T> extends ThemeFragment implements HasAsync<List<T>>, AdapterView.OnItemClickListener {

    public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected AbsListView mListView;

    private final List<T> loadedDatas = new ArrayList<T>();

    protected BaseAdapter adapter;

    public AbstractAsyncListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview_default, container, false);
        mListView = (ListView) rootView.findViewById(R.id.article_listview);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // repaint in case font changed
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onThemeChanged() {
        super.onThemeChanged();
        getAdapter().notifyDataSetChanged();
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

        //init reload data
        reloadData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) (getActivity().getApplicationContext());
    }


    public void onProgressShow() {
        // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
        getActivity().setProgressBarIndeterminate(true);
        getActivity().setProgressBarVisibility(true);
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    public void onProgressDismiss() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        getActivity().setProgressBarVisibility(false);
        //Stop refresh animation anyway
    }

    public synchronized void onFailure(AsyncResult<List<T>> listAsyncResult) {
//        getAdapter().notifyDataSetChanged();
        Exception e = listAsyncResult.getException();
        if (e != null && e instanceof InfoException) {
            ToastUtils.showShortToast(getActivity(), listAsyncResult.getErrorMsg());
        }
    }

    public synchronized void onSuccess(AsyncResult<List<T>> listAsyncResult) {
        loadedDatas.addAll(listAsyncResult.getResult());
        getAdapter().notifyDataSetChanged();
    }

    public synchronized void clearData() {
        loadedDatas.clear();
    }

    public synchronized T getData(int index) {
        return loadedDatas.get(index);
    }

    public int getDataSize() {
        return loadedDatas.size();
    }

    public void addData(int location, T data) {
        loadedDatas.add(location, data);
    }

    public abstract void loadData();

    public abstract void reloadData();

    protected abstract BaseAdapter newAdapter();

    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);

    public void redraw() {

    }
}
