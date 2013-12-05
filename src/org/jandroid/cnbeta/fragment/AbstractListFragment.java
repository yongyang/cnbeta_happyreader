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
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.ThemeFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public abstract class AbstractListFragment<T> extends ThemeFragment implements AdapterView.OnItemClickListener {

    public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected AbsListView mListView;

    protected View emptyView;

    protected final List<T> loadedDatas = new ArrayList<T>();

    protected BaseAdapter adapter;


    public AbstractListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview_default, container, false);
        mListView = (ListView) rootView.findViewById(R.id.article_listview);
        emptyView = rootView.findViewById(R.id.lv_emptyTextView);
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

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) (theActivity.getApplicationContext());
    }

    public void setDatas(List<T> datas){
        loadedDatas.clear();
        loadedDatas.addAll(datas);
        if(getAdapter().isEmpty()) { // add empty view if no data
            emptyView.setVisibility(View.VISIBLE);
            mListView.setEmptyView(emptyView);
        }
        else {
            emptyView.setVisibility(View.GONE);
        }
        getAdapter().notifyDataSetChanged();
    }

    public void appendDatas(List<T> datas){
        loadedDatas.addAll(datas);
        if(getAdapter().isEmpty()) { // add empty view if no data
            emptyView.setVisibility(View.VISIBLE);
            mListView.setEmptyView(emptyView);
        }
        else {
            emptyView.setVisibility(View.GONE);
        }
        getAdapter().notifyDataSetChanged();
    }

    public synchronized void clearData() {
        loadedDatas.clear();
        if(getAdapter().isEmpty()) { // add empty view if no data
            emptyView.setVisibility(View.VISIBLE);
            mListView.setEmptyView(emptyView);
        }
        else {
            emptyView.setVisibility(View.GONE);
        }
        getAdapter().notifyDataSetChanged();
    }

    public synchronized T getData(int index) {
        return loadedDatas.get(index);
    }

    public int getDataSize() {
        return loadedDatas.size();
    }

    public void addData(int location, T data) {
        loadedDatas.add(location, data);
        getAdapter().notifyDataSetChanged();
    }

    protected abstract BaseAdapter newAdapter();

    public abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}
