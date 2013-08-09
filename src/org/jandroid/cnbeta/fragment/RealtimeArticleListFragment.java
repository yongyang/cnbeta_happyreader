package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.async.RealtimeArticleListAsyncTask;
import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.util.EnvironmentUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class RealtimeArticleListFragment extends Fragment {

    private ListView lvArticleList;

    private final List<RealtimeArticle> loadedArticles = new ArrayList<RealtimeArticle>();

    private final Handler handler = new Handler();

    private BaseAdapter adapter;

    private ProgressBar progressBarRefresh;
    private LinearLayout lineLayoutRefresh;
    private TextView tvLastTimeRefresh;

    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private MenuItem refreshMenuItem;
    private ImageView refreshActionView;
    private Animation clockWiseRotationAnimation;

    private LinearLayout footbarRefresh;

    public RealtimeArticleListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        if(!(activity instanceof ArticleListListener)) {

        }
        super.onAttach(activity);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        refreshActionView = (ImageView) inflater.inflate(R.layout.iv_refresh_action_view, null);
        clockWiseRotationAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation_clockwise_refresh);
        clockWiseRotationAnimation.setRepeatCount(Animation.INFINITE);

        View rootView = inflater.inflate(R.layout.lv_article_list, container, false);
        lvArticleList = (ListView)rootView.findViewById(R.id.article_listview);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        footbarRefresh = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.lv_footbar_refresh, lvArticleList,false);
        progressBarRefresh = (ProgressBar) footbarRefresh.findViewById(R.id.progressBar_refresh);
        lineLayoutRefresh = (LinearLayout)footbarRefresh.findViewById(R.id.linelayout_refresh);
        tvLastTimeRefresh = (TextView)footbarRefresh.findViewById(R.id.refresh_last_time);

        lvArticleList.addFooterView(footbarRefresh);

        footbarRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reloadArticles();
            }
        });

        adapter = new BaseAdapter() {
            public int getCount() {
                return loadedArticles.size();
            }

            public Object getItem(int position) {
                return loadedArticles.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.lv_realtime_article_item, null);
                }
                RealtimeArticle article = loadedArticles.get(position);
                TextView tvTitle = (TextView)convertView.findViewById(R.id.tile);
                tvTitle.setText(article.getTitle());
                TextView tvHometextShowShort2 = (TextView)convertView.findViewById(R.id.hometext_show_short2);
                tvHometextShowShort2.setText(article.getHometextShowShort2());
                TextView tvTime = (TextView)convertView.findViewById(R.id.time);
                tvTime.setText("" + article.getTime());

                TextView tvTimeShow = (TextView)convertView.findViewById(R.id.time_show);
                tvTimeShow.setText("" + article.getTimeShow());

                return convertView;

            }
        };
        lvArticleList.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        reloadArticles();
    }
    
    private void reloadArticles(){
        EnvironmentUtils.checkNetworkConnected(getActivity());

        new RealtimeArticleListAsyncTask(){
            @Override
            public CnBetaApplicationContext getCnBetaApplicationContext() {
                return (CnBetaApplicationContext)getActivity().getApplication();
            }

            @Override
            public void showProgressUI() {
                // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
                getActivity().setProgressBarIndeterminate(true);
                getActivity().setProgressBarVisibility(true);
                footbarRefresh.setClickable(false);
                progressBarRefresh.setVisibility(View.VISIBLE);
                lineLayoutRefresh.setVisibility(View.GONE);
                // rotate refresh item
                rotateRefreshActionView();
            }

            @Override
            public void dismissProgressUI() {
                getActivity().setProgressBarVisibility(false);
                progressBarRefresh.setVisibility(View.GONE);
                lineLayoutRefresh.setVisibility(View.VISIBLE);
                //Stop refresh animation anyway
                dismissRefreshActionView();
                footbarRefresh.setClickable(true);
            }

            @Override
            protected void onPostExecute(AsyncResult listAsyncResult) {
                super.onPostExecute(listAsyncResult);
                if(listAsyncResult.isSuccess()) {
                    List<RealtimeArticle> articles = (List<RealtimeArticle>)listAsyncResult.getResult();
                    if(articles != null) {
                        tvLastTimeRefresh.setText(dateFormat.format(new Date()));
                        loadedArticles.clear();
                        loadedArticles.addAll(articles);
                        adapter.notifyDataSetChanged();
                        lvArticleList.smoothScrollToPosition(0);
                    }
                }
                else {
                    Toast.makeText(getActivity(), listAsyncResult.getErrorMsg(), Toast.LENGTH_LONG).show();
                }
            }
        }.executeMultiThread();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //refresh actionitem
        inflater.inflate(R.menu.article_list_fragment_menu, menu);
        refreshMenuItem = menu.findItem(R.id.refresh_item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable()) {
            item.setChecked(true);
        }
        switch (item.getItemId()) {
            case R.id.more_item:
                break;
            case R.id.refresh_item:
                reloadArticles();
            default:
        }
        return true;
    }

    private void rotateRefreshActionView() {
        if(refreshMenuItem != null) {
            /* Attach a rotating ImageView to the refresh item as an ActionView */
            refreshActionView.startAnimation(clockWiseRotationAnimation);
            refreshMenuItem.setActionView(refreshActionView);
        }
    }

    private void dismissRefreshActionView() {
        if(refreshMenuItem != null) {
            View actionView = refreshMenuItem.getActionView();
            if(actionView != null) {
                actionView.clearAnimation();
                refreshMenuItem.setActionView(null);
            }
        }
    }
    public static interface ArticleListListener {
        void onArticleItemClick(long articleId);
    }
}
