package org.jandroid.cnbeta.fragment;

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
import org.jandroid.cnbeta.async.LoadingAsyncTask;
import org.jandroid.common.BaseFragment;
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
        reloadData();
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) (getActivity().getApplicationContext());
    }


    public void onProgressShow() {
        // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
        getActivity().setProgressBarIndeterminate(true);
        getActivity().setProgressBarVisibility(true);
    }

    public void onProgressDismiss() {
        getActivity().setProgressBarVisibility(false);
        //Stop refresh animation anyway
    }

    public void onFailure(AsyncResult<List<T>> listAsyncResult) {
        getAdapter().notifyDataSetChanged();
        logger.e("Failure: ", listAsyncResult.getException());
    }

    public void onSuccess(AsyncResult<List<T>> listAsyncResult) {
        loadedDatas.addAll(listAsyncResult.getResult());
        getAdapter().notifyDataSetChanged();
    }

    public void onSuccess(AsyncResult<List<T>> listAsyncResult , boolean refresh) {
        loadedDatas.addAll(listAsyncResult.getResult());
        getAdapter().notifyDataSetChanged();
    }

    public void clearData(){
        loadedDatas.clear();
    }

    public T getData(int index){
        return loadedDatas.get(index);
    }

    public int getDataSize() {
        return loadedDatas.size();
    }

    protected abstract void loadData();

    protected abstract void reloadData();

    protected abstract BaseAdapter newAdapter();

    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
