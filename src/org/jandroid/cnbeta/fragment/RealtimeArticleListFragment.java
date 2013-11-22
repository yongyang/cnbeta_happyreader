package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.CnBetaPreferences;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.RealtimeArticleListAsyncTask;
import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.view.RefreshView;
import org.jandroid.common.FontUtils;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class RealtimeArticleListFragment extends AbstractAsyncListFragment<RealtimeArticle> {

    private RefreshView footerRefreshView;

    public RealtimeArticleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_realtime_article_item, null);
                }
                RealtimeArticle article = getData(position);
                TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
                tvTitle.setText(article.getTitle());
                TextView tvHometextShowShort2 = (TextView) convertView.findViewById(R.id.hometext_show_short2);
                tvHometextShowShort2.setText(article.getHometextShowShort2());
                TextView tvTime = (TextView) convertView.findViewById(R.id.time);
                tvTime.setText("" + article.getTime());

                TextView tvTimeShow = (TextView) convertView.findViewById(R.id.time_show);
                tvTimeShow.setText("" + article.getTimeShow());

                int fontSizeOffset = ((CnBetaApplicationContext)getActivity().getApplicationContext()).getCnBetaPreferences().getFontSizeOffset();
                FontUtils.updateTextSize(getActivity(), tvTitle, R.dimen.listitem_title_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvHometextShowShort2, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvTime, R.dimen.listitem_status_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvTimeShow, R.dimen.listitem_status_text_size, fontSizeOffset);

                updateTypeFace(convertView);
                return convertView;

            }
        };
    }

    @Override
    public void loadData() {
        reloadData();
    }

    @Override
    public void reloadData() {
        executeAsyncTaskMultiThreading(new RealtimeArticleListAsyncTask() {

            @Override
            public HasAsync<List<RealtimeArticle>> getAsyncContext() {
                return new HasAsyncDelegate<java.util.List<org.jandroid.cnbeta.entity.RealtimeArticle>>(RealtimeArticleListFragment.this){
                    @Override
                    public void onSuccess(AsyncResult<List<RealtimeArticle>> listAsyncResult) {
                        clearData();
                        super.onSuccess(listAsyncResult);
                        mListView.setSelection(0);
                    }
                };
            }
        });

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

    @Override
    public void onResume() {
        updateTypeFace(footerRefreshView.getRootView());
        super.onResume();
    }

}