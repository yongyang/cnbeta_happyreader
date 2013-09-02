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

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class RealtimeArticleListFragment extends AbstractAsyncListFragment<RealtimeArticle> {

    public RealtimeArticleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //refresh actionitem
        inflater.inflate(R.menu.search_refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
        }
        getCnBetaApplicationContext().onOptionsItemSelected(getActivity(), item);
        return true;
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
                return loadedDatas.size();
            }

            public Object getItem(int position) {
                return loadedDatas.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.lv_realtime_article_item, null);
                }
                RealtimeArticle article = loadedDatas.get(position);
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
    public LoadingAsyncTask<List<RealtimeArticle>> newAsyncTask() {
        return new RealtimeArticleListAsyncTask() {

            @Override
            public HasAsync<List<RealtimeArticle>> getAsyncContext() {
                return RealtimeArticleListFragment.this;
            }
        };
    }
}
