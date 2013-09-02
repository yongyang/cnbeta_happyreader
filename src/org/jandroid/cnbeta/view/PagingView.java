package org.jandroid.cnbeta.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jandroid.cnbeta.R;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class PagingView {

    private LinearLayout footerbar;
    private LinearLayout pageBar;
    private ProgressBar progressBar;
    private TextView tvPage;

    private int page = 0;

    public static PagingView load(LayoutInflater inflater, int resource) {
        return new PagingView((LinearLayout) inflater.inflate(resource, null, false));
    }

    private PagingView(LinearLayout rootView) {
        footerbar = rootView;
        pageBar = (LinearLayout) footerbar.findViewById(R.id.lineLayout_next_page);
        progressBar = (ProgressBar) footerbar.findViewById(R.id.progressBar_next_page);
        tvPage = (TextView) footerbar.findViewById(R.id.tv_page);
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
        progressBar.setVisibility(View.VISIBLE);
        pageBar.setVisibility(View.GONE);
    }

    public void onProgressDismiss() {
        progressBar.setVisibility(View.GONE);
        pageBar.setVisibility(View.VISIBLE);
        footerbar.setClickable(true);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        footerbar.setOnClickListener(onClickListener);
    }

}
