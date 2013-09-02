package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.LoadingAsyncTask;
import org.jandroid.cnbeta.async.RealtimeArticleListAsyncTask;
import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.cnbeta.view.RefreshView;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class RealtimeArticleListFragment extends AbstractAsyncListFragment<RealtimeArticle> {

    private RefreshView refreshView;

    public RealtimeArticleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        refreshView = RefreshView.load(getActivity().getLayoutInflater(), R.layout.lv_footbar_refresh);

        mListView.addFooterView(refreshView.getRootView());

        refreshView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reloadData();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RealtimeArticle article = (RealtimeArticle) getAdapter().getItem(position);
        Utils.openContentActivity(getActivity(), article.getSid(), article.getTitle());
    }

    @Override
    protected BaseAdapter newAdapter() {
        return new BaseAdapter() {
            public int getCount() {
                return getDataSize();
            }

            public Object getItem(int position) {
                return getData(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.lv_realtime_article_item, null);
                }
                RealtimeArticle article = getData(position);
                TextView tvTitle = (TextView) convertView.findViewById(R.id.tile);
                tvTitle.setText(article.getTitle());
                TextView tvHometextShowShort2 = (TextView) convertView.findViewById(R.id.hometext_show_short2);
                tvHometextShowShort2.setText(article.getHometextShowShort2());
                TextView tvTime = (TextView) convertView.findViewById(R.id.time);
                tvTime.setText("" + article.getTime());

                TextView tvTimeShow = (TextView) convertView.findViewById(R.id.time_show);
                tvTimeShow.setText("" + article.getTimeShow());

                return convertView;

            }
        };
    }

    @Override
    protected void loadData() {
        executeAsyncTaskMultiThreading(new RealtimeArticleListAsyncTask() {

            @Override
            public HasAsync<List<RealtimeArticle>> getAsyncContext() {
                return RealtimeArticleListFragment.this;
            }
        });
    }

    @Override
    protected void reloadData() {
        loadData();
    }

    @Override
    public void onProgressShow() {
        super.onProgressShow();
        refreshView.onProgressShow();
    }

    @Override
    public void onProgressDismiss() {
        super.onProgressDismiss();
        refreshView.onProgressDismiss();
    }
}