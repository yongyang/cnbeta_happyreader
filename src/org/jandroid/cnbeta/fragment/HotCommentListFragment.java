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
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.HotCommentListAsyncTask;
import org.jandroid.cnbeta.entity.HotComment;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HotCommentListFragment extends AbstractAsyncListFragment<HotComment> {

    private PagingView footerPagingView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), R.layout.listvew_footbar_paging);
        ((ListView)mListView).addFooterView(footerPagingView.getRootView());

        footerPagingView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData();
            }
        });

        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void loadData() {
        executeAsyncTaskMultiThreading(new HotCommentListAsyncTask() {
                    @Override
                    protected int getPage() {
                        return footerPagingView.getNextPage();
                    }

                    @Override
                    public HasAsync<List<HotComment>> getAsyncContext() {
                        return HotCommentListFragment.this;
                    }
                });
    }

    public void reloadData() {
        executeAsyncTaskMultiThreading(new HotCommentListAsyncTask() {
                            @Override
                            protected int getPage() {
                                footerPagingView.resetPage();
                                return footerPagingView.getNextPage();
                            }

                            @Override
                            public HasAsync<List<HotComment>> getAsyncContext() {
                                return new HasAsyncDelegate<List<HotComment>>(HotCommentListFragment.this) {
                                    @Override
                                    public void onSuccess(AsyncResult<List<HotComment>> listAsyncResult) {
                                        clearData();
                                        super.onSuccess(listAsyncResult);
                                        mListView.setSelection(0);
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
                TextView commentTextView = (TextView) convertView.findViewById(R.id.comment);
                commentTextView.setText("" + hotComment.getComment());
                TextView titleShowTextView = (TextView) convertView.findViewById(R.id.titleShow);
                titleShowTextView.setText("" + hotComment.getTitleShow());

                TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
                dateTextView.setText(hotComment.getDate());

                TextView hostNameShowTextView = (TextView) convertView.findViewById(R.id.hostNameShow);
                hostNameShowTextView.setText(hotComment.getHostNameShow());
                hostNameShowTextView.getPaint().setTextSkewX(-0.25f);

                TextView nameTextView = (TextView) convertView.findViewById(R.id.name);
                nameTextView.setText(hotComment.getName());
                nameTextView.getPaint().setTextSkewX(-0.25f);

                TextView fromTextView = (TextView) convertView.findViewById(R.id.from);
                fromTextView.getPaint().setTextSkewX(-0.25f);

                TextView toTextView = (TextView) convertView.findViewById(R.id.to);
                toTextView.getPaint().setFakeBoldText(true);

/*
                tvTitle.setText(article.getTitle());
                TextView tvHometextShowShort = (TextView) convertView.findViewById(R.id.hometext_show_short);
                tvHometextShowShort.setText(article.getHometextShowShort());
*/

                Utils.updateTextSize(getActivity(), commentTextView, R.dimen.listitem_comment_text_size);
                Utils.updateTextSize(getActivity(), titleShowTextView, R.dimen.listitem_description_text_size);
                Utils.updateTextSize(getActivity(), dateTextView, R.dimen.listitem_status_text_size);
                Utils.updateTextSize(getActivity(), hostNameShowTextView, R.dimen.listitem_status_text_size);
                Utils.updateTextSize(getActivity(), nameTextView, R.dimen.listitem_status_text_size);
                Utils.updateTextSize(getActivity(), fromTextView, R.dimen.listitem_status_text_size);
                Utils.updateTextSize(getActivity(), toTextView, R.dimen.listitem_description_text_size);
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HotComment hotComment = getData(position);
        Utils.openContentActivity(getActivity(), hotComment.getSid(), hotComment.getTitleShow());
    }


    @Override
    public void onSuccess(AsyncResult<List<HotComment>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        footerPagingView.increasePage();
    }

    @Override
    public void onProgressShow() {
        super.onProgressShow();
        footerPagingView.onProgressShow();
    }

    @Override
    public void onProgressDismiss() {
        super.onProgressDismiss();
        footerPagingView.onProgressDismiss();
    }
}
