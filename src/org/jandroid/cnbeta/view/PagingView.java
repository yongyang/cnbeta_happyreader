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
public class PagingView {
    public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private LinearLayout footerbar;
    private LinearLayout pageBar;
    private LinearLayout progressBarLineLinearLayout;
    private TextView tvPage;
    private TextView tvLastTimeRefresh;

    private int page = 0;

    public static PagingView load(LayoutInflater inflater, int resource) {
        return new PagingView((LinearLayout) inflater.inflate(resource, null, false));
    }

    private PagingView(LinearLayout rootView) {
        footerbar = rootView;
        progressBarLineLinearLayout = (LinearLayout) footerbar.findViewById(R.id.linelayout_progressbar);
        pageBar = (LinearLayout) footerbar.findViewById(R.id.lineLayout_next_page);
        tvPage = (TextView) footerbar.findViewById(R.id.tv_page);

        tvLastTimeRefresh = (TextView) footerbar.findViewById(R.id.refresh_last_time);
    }

    public LinearLayout getRootView() {
        return footerbar;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
        tvPage.setText("" + page);
    }

    public void onProgressShow() {
        footerbar.setClickable(false);
        progressBarLineLinearLayout.setVisibility(View.VISIBLE);
        pageBar.setVisibility(View.GONE);
    }

    public void onProgressDismiss() {
        progressBarLineLinearLayout.setVisibility(View.GONE);
        pageBar.setVisibility(View.VISIBLE);
        footerbar.setClickable(true);
        tvLastTimeRefresh.setText(dateFormat.format(new Date()));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        footerbar.setOnClickListener(onClickListener);
    }

}
