package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.MRankArticleListAsyncTask;
import org.jandroid.cnbeta.entity.MRankArticle;
import org.jandroid.cnbeta.loader.MRankListLoader;
import org.jandroid.cnbeta.view.RefreshView;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class MRankListFragment extends AbstractAsyncListFragment<MRankArticle> {

    private RefreshView footerRefreshView;

    private MRankListLoader.Type type;

    public MRankListFragment() {

    }

    private void setType(MRankListLoader.Type type) {
        this.type = type;
    }

    public MRankListLoader.Type getType() {
        return type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        setType((MRankListLoader.Type)args.getSerializable("type"));

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        footerRefreshView = RefreshView.load(getActivity().getLayoutInflater(), R.layout.listview_footbar_refresh);
        ((ListView)mListView).addFooterView(footerRefreshView.getRootView());
        footerRefreshView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reloadData();
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MRankArticle article = (MRankArticle) getAdapter().getItem(position);
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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_mrank_item, null);
                }
                MRankArticle article = getData(position);
                TextView tvRank = (TextView) convertView.findViewById(R.id.rank);
                String rank = "" + (position + 1);
                if(rank.length() == 1) {
                    rank = " " + rank;
                }
                tvRank.setText(rank + ". ");
                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(article.getTitle());
                return convertView;
            }
        };
    }

    @Override
    public void loadData() {
        executeAsyncTaskMultiThreading(new MRankArticleListAsyncTask() {

            @Override
            protected MRankListLoader.Type getType() {
                return MRankListFragment.this.getType();
            }

            @Override
            public HasAsync<List<MRankArticle>> getAsyncContext() {
                return MRankListFragment.this;
            }
        });
    }

    @Override
    public void onSuccess(AsyncResult<List<MRankArticle>> listAsyncResult) {
        clearData();
        super.onSuccess(listAsyncResult);
        mListView.setSelection(0);
    }

    @Override
    public void reloadData() {
        loadData();
    }

    @Override
    public void onProgressShow() {
        super.onProgressShow();
        footerRefreshView.onProgressShow();
    }

    @Override
    public void onProgressDismiss() {
        super.onProgressDismiss();
        footerRefreshView.onProgressDismiss();
    }

}
