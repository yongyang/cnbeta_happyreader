package org.jandroid.cnbeta.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    public static PagingView load(LayoutInflater inflater, ViewGroup root, int resource) {
        return new PagingView((LinearLayout) inflater.inflate(resource, root, true));
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

    public int getNextPage() {
        return page + 1;
    }


    public void increasePage(){
        page++;
        tvPage.setText("" + page);
    }

    public void resetPage(){
        page = 0;
        tvPage.setText("" + page);
    }

    public void setPage(int page) {
        this.page = page;
        tvPage.setText("" + page);
    }

    public void setPage(int page, String displayPage) {
        this.page = page;
        tvPage.setText(displayPage);
    }

    public void onProgressShow() {
        footerbar.setEnabled(false);
        progressBarLineLinearLayout.setVisibility(View.VISIBLE);
        pageBar.setVisibility(View.GONE);
    }

    public void onProgressDismiss() {
        progressBarLineLinearLayout.setVisibility(View.GONE);
        pageBar.setVisibility(View.VISIBLE);
        footerbar.setEnabled(true);
        tvLastTimeRefresh.setText(dateFormat.format(new Date()));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        footerbar.setOnClickListener(onClickListener);
    }

    public void setEnable(boolean enable) {
//        footerbar.setClickable(clickable);
        footerbar.setEnabled(enable);
    }

}
