package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.HotCommentListAsyncTask;
import org.jandroid.cnbeta.async.LoadingAsyncTask;
import org.jandroid.cnbeta.entity.HotComment;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HotCommentListFragment extends AbstractAsyncListFragment<HotComment> {

    protected int page = 0;

    private PagingView footerPagingView;

    public int getPage() {
        return page;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), R.layout.listvew_footbar_paging);
        mListView.addFooterView(footerPagingView.getRootView());

        footerPagingView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData();
            }
        });

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void loadData() {
        executeAsyncTaskMultiThreading(new HotCommentListAsyncTask() {
                    @Override
                    protected int getPage() {
                        return HotCommentListFragment.this.getPage() + 1;
                    }

                    @Override
                    public HasAsync<List<HotComment>> getAsyncContext() {
                        return HotCommentListFragment.this;
                    }
                });
    }

    protected void reloadData() {
        page = 0;
        executeAsyncTaskMultiThreading(new HotCommentListAsyncTask() {
                            @Override
                            protected int getPage() {
                                return HotCommentListFragment.this.getPage()+1;
                            }

                            @Override
                            public HasAsync<List<HotComment>> getAsyncContext() {
                                return new HasAsyncDelegate<List<HotComment>>(HotCommentListFragment.this) {
                                    @Override
                                    public void onSuccess(AsyncResult<List<HotComment>> listAsyncResult) {
                                        clearData();
                                        super.onSuccess(listAsyncResult);
                                    }
                                };
                            }
                        });
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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_hot_commend_item, null);
                }
                HotComment hotComment = getData(position);
                TextView tvTitle = (TextView) convertView.findViewById(R.id.comment);
                tvTitle.setText("" + hotComment.getTid());
/*
                tvTitle.setText(article.getTitle());
                TextView tvHometextShowShort = (TextView) convertView.findViewById(R.id.hometext_show_short);
                tvHometextShowShort.setText(article.getHometextShowShort());
*/
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HotComment article = getData(position);
        Utils.openContentActivity(getActivity(), article.getSid(), article.getTitle());
    }


    @Override
    public void onSuccess(AsyncResult<List<HotComment>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        page++;
        footerPagingView.setPage(page);
    }

    @Override
    public void onProgressShow() {
        //TODO: refresh action view only page=1
        super.onProgressShow();
        footerPagingView.onProgressShow();
        if (getPage() == 1) { //page 1 is reload
//            startRotateRefreshActionView();
        }

    }

    @Override
    public void onProgressDismiss() {
        //TODO: refresh action view only page=1
        super.onProgressDismiss();
        footerPagingView.onProgressDismiss();
        // stop refresh rotation anyway
        if (getPage() == 1) { //page 1 is reload
//            stopRotateRefreshActionView();
        }
    }

}
