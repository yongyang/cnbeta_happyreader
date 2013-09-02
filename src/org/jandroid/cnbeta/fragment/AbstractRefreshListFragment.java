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

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractRefreshListFragment<T> extends AbstractAsyncListFragment<T> {

    private ProgressBar progressBarRefresh;
    private LinearLayout lineLayoutRefresh;
    private TextView tvLastTimeRefresh;
    private LinearLayout footbarRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        footbarRefresh = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.lv_footbar_refresh, mListView, false);
        progressBarRefresh = (ProgressBar) footbarRefresh.findViewById(R.id.progressBar_refresh);
        lineLayoutRefresh = (LinearLayout) footbarRefresh.findViewById(R.id.linelayout_refresh);
        tvLastTimeRefresh = (TextView) footbarRefresh.findViewById(R.id.refresh_last_time);

        mListView.addFooterView(footbarRefresh);

        footbarRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reloadData();
            }
        });
    }

    @Override
    public void onProgressShow() {
        super.onProgressShow();
        footbarRefresh.setClickable(false);
        progressBarRefresh.setVisibility(View.VISIBLE);
        lineLayoutRefresh.setVisibility(View.GONE);
    }

    @Override
    public void onProgressDismiss() {
        super.onProgressDismiss();
        progressBarRefresh.setVisibility(View.GONE);
        lineLayoutRefresh.setVisibility(View.VISIBLE);
        footbarRefresh.setClickable(true);
    }

    @Override
    public void onSuccess(AsyncResult<List<T>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        tvLastTimeRefresh.setText(dateFormat.format(new Date()));
    }
}
