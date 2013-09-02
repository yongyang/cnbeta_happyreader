package org.jandroid.cnbeta.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jandroid.cnbeta.R;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractPagingListFragment<T> extends AbstractAsyncListFragment<T> {

    protected int page = 0;

    private ProgressBar progressBarNextPage;
    private LinearLayout lineLayoutNextPage;
    private TextView tvPage;

    private LinearLayout footbarNextPage;

    public int getPage() {
        return page;
    }

    public int getNextPage() {
        return page+1;
    }

    protected void reloadDatas() {
        page = 0;
        super.reloadDatas();
    }

    @Override
    public void onSuccess(AsyncResult<List<T>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        page++;
        tvPage.setText("" + page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        footbarNextPage = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.listvew_footbar_paging, mListView,false);
        lineLayoutNextPage = (LinearLayout)footbarNextPage.findViewById(R.id.lineLayout_next_page);
        progressBarNextPage = (ProgressBar)footbarNextPage.findViewById(R.id.progressBar_next_page);
        tvPage = (TextView)footbarNextPage.findViewById(R.id.tv_page);

        mListView.addFooterView(footbarNextPage);

        footbarNextPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadDatas();
            }
        });
    }

    @Override
    public void onProgressShow() {
        //TODO: refresh action view only page=1
        super.onProgressShow();
        footbarNextPage.setClickable(false);
        progressBarNextPage.setVisibility(View.VISIBLE);
        lineLayoutNextPage.setVisibility(View.GONE);
        if (getPage() == 1) { //page 1 is reload
//            startRotateRefreshActionView();
        }

    }

    @Override
    public void onProgressDismiss() {
        //TODO: refresh action view only page=1
        super.onProgressDismiss();
        progressBarNextPage.setVisibility(View.GONE);
        lineLayoutNextPage.setVisibility(View.VISIBLE);
        // stop refresh rotation anyway
        if (getPage() == 1) { //page 1 is reload
//            stopRotateRefreshActionView();
        }
        footbarNextPage.setClickable(true);
    }
}
