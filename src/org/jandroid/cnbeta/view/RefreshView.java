package org.jandroid.cnbeta.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jandroid.cnbeta.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class RefreshView {
    public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ProgressBar progressBarRefresh;
    private LinearLayout lineLayoutRefresh;
    private TextView tvLastTimeRefresh;
    private LinearLayout footbarRefresh;

    private int page = 0;

    public static RefreshView load(LayoutInflater inflater, int resource) {
        return new RefreshView((LinearLayout) inflater.inflate(resource, null, false));
    }

    private RefreshView(LinearLayout rootView) {
        footbarRefresh = rootView;
        progressBarRefresh = (ProgressBar) footbarRefresh.findViewById(R.id.progressBar_refresh);
        lineLayoutRefresh = (LinearLayout) footbarRefresh.findViewById(R.id.linelayout_refresh);
        tvLastTimeRefresh = (TextView) footbarRefresh.findViewById(R.id.refresh_last_time);
    }

    public LinearLayout getRootView() {
        return footbarRefresh;
    }

    public void setPage(int page) {
        this.page = page;
        tvLastTimeRefresh.setText(dateFormat.format(new Date()));
    }

    public void onProgressShow() {
        footbarRefresh.setClickable(false);
        progressBarRefresh.setVisibility(View.VISIBLE);
        lineLayoutRefresh.setVisibility(View.GONE);
    }

    public void onProgressDismiss() {
        progressBarRefresh.setVisibility(View.GONE);
        lineLayoutRefresh.setVisibility(View.VISIBLE);
        footbarRefresh.setClickable(true);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        footbarRefresh.setOnClickListener(onClickListener);
    }

}
