package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HistoryArticleListAsyncTask;
import org.jandroid.cnbeta.entity.HistoryArticle;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class HistoryArticleListFragment extends AbstractAsyncListFragment<HistoryArticle> {

    public HistoryArticleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HistoryArticle article = (HistoryArticle) getAdapter().getItem(position);
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
                HistoryArticle article = getData(position);
                TextView tvTitle = (TextView) convertView.findViewById(R.id.tile);
                tvTitle.setText(article.getTitle());
                TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
                dateTextView.setText(article.getDate());
                return convertView;
            }
        };
    }

    @Override
    protected void loadData() {
        executeAsyncTaskMultiThreading(new HistoryArticleListAsyncTask() {

                    @Override
                    public HasAsync<List<HistoryArticle>> getAsyncContext() {
                        return HistoryArticleListFragment.this;
                    }
                });
    }

    @Override
    protected void reloadData() {
        loadData();
    }
}
