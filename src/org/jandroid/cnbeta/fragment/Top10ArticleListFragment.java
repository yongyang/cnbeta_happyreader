package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import org.jandroid.cnbeta.CnBetaApplication;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Top10Activity;
import org.jandroid.cnbeta.adapter.AsyncImageAdapter;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.async.LoadImageAsyncTask;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.RankArticle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class Top10ArticleListFragment extends Fragment {

    // onReselect tab 的时候进行切换
    private Top10Activity.RankType[] rankTypes;
    private int currentRankTypeIndex = 0;

    // all articles, RankType=>List of articles
    private final Map<String, List<RankArticle>> allRankArticlesMap = new HashMap<String, List<RankArticle>>();

    private int loadTimes = 0;

    private ListView lvArticleList;

    private final Handler handler = new Handler();

    private AsyncImageAdapter asyncImageAdapter;

    private ProgressBar progressBarRefresh;
    private LinearLayout lineLayoutRefresh;
    private TextView tvLastTimeRefresh;

    private MenuItem refreshMenuItem;
    private ImageView refreshActionView;
    private Animation clockWiseRotationAnimation;

    private LinearLayout footbarRefresh;

    private Top10Activity top10Activity;

    public Top10ArticleListFragment(Top10Activity.RankType... rankTypes) {
        this.rankTypes = rankTypes;
        this.currentRankTypeIndex = 0;
    }

    public Top10Activity.RankType getCurrentRankType() {
        return rankTypes[currentRankTypeIndex];
    }

    public int getCurrentRankTypeIndex() {
        return currentRankTypeIndex;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        if(!(activity instanceof Top10Activity)) {

        }
        this.top10Activity = (Top10Activity)activity;
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

        footbarRefresh.setClickable(false);

        footbarRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reloadArticles();
            }
        });

        asyncImageAdapter = new AsyncImageAdapter() {
            @Override
            public int getCount() {
                if(allRankArticlesMap.containsKey(rankTypes[currentRankTypeIndex].getType())) {
                    return allRankArticlesMap.get(rankTypes[currentRankTypeIndex].getType()).size();
                }
                else {
                    return 0;
                }
            }

            @Override
            public Object getItem(int position) {
                if(allRankArticlesMap.containsKey(rankTypes[currentRankTypeIndex].getType())) {
                    return allRankArticlesMap.get(rankTypes[currentRankTypeIndex].getType()).get(position);
                }
                else {
                    return null;
                }
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Bitmap getDefaultBitmap() {
                return BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_img);
            }

            @Override
            protected void loadImageAsync(final String imageUrl, final OnAsyncImageLoadListener onAsyncImageLoadListener) {
                new LoadImageAsyncTask() {

                    @Override
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplication)getActivity().getApplication();
                    }

                    @Override
                    protected String getImageUrl() {
                        return imageUrl;
                    }

                    @Override
                    protected void onPostExecute(final AsyncResult bitmapAsyncResult) {
                        super.onPostExecute(bitmapAsyncResult);
                        if (bitmapAsyncResult.isSuccess()) {
                            Bitmap bitmap = (Bitmap) bitmapAsyncResult.getResult();
                            onAsyncImageLoadListener.onLoaded(bitmap);
                        }
                        else {
                            onAsyncImageLoadListener.onLoadFailed(bitmapAsyncResult.getErrorMsg(), bitmapAsyncResult.getException());
                        }
                    }

                    @Override
                    protected void onCancelled() {
                        onAsyncImageLoadListener.onLoadFailed("Cancelled", null);
                    }

                    @Override
                    protected void onCancelled(AsyncResult bitmapAsyncResult) {
                        onAsyncImageLoadListener.onLoadFailed("Cancelled", null);
                    }
                }.executeMultiThread();
            }

            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.lv_rank_article_item, null);
                }
                RankArticle article = (RankArticle)getItem(position);
                TextView tvTitle = (TextView)convertView.findViewById(R.id.tile);
                tvTitle.setText(article.getTitle());
                TextView tvComment = (TextView)convertView.findViewById(R.id.comment);
                tvComment.setText("" + article.getComment());

                TextView tvHometext = (TextView)convertView.findViewById(R.id.hometext);
                tvHometext.setText(article.getHometext());
                TextView tvTime = (TextView)convertView.findViewById(R.id.time);
                tvTime.setText("" + article.getTime());
                ImageView ivLogo = (ImageView) convertView.findViewById(R.id.logo);
                // queue to image load list or set a cached bitmap if has been cached
                ivLogo.setImageBitmap(queueImageView(position, ivLogo, article.getLogo()));
                return convertView;
            }
        };
        lvArticleList.setAdapter(asyncImageAdapter);
        lvArticleList.setOnScrollListener(asyncImageAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 只有第一次打开第一个tab才自动重装载
        if(loadTimes == 0 && rankTypes[currentRankTypeIndex].equals(rankTypes[0])) {
            reloadArticles();
        }
        else {
            loadArticles();
        }
        loadTimes++;
    }

    public void switchRankType(){
        if(rankTypes.length > 1) {
            if(rankTypes.length > currentRankTypeIndex+1){
                currentRankTypeIndex++;
            }
            else {
                currentRankTypeIndex=0;
            }
            asyncImageAdapter.notifyDataSetChanged();
        }
    }
    
    private void reloadArticles(){

        ((Top10Activity)getActivity()).reloadRanks(new RankLoadCallback() {
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

            public void dismissProgressUI() {
                getActivity().setProgressBarVisibility(false);
                progressBarRefresh.setVisibility(View.GONE);
                lineLayoutRefresh.setVisibility(View.VISIBLE);
                //Stop refresh animation anyway
                dismissRefreshActionView();
                footbarRefresh.setClickable(true);
            }

            public void onRankLoadFinished(Map<String, List<RankArticle>> allRankArticlesMap) {
                Top10ArticleListFragment.this.allRankArticlesMap.clear();
                Top10ArticleListFragment.this.allRankArticlesMap.putAll(allRankArticlesMap);
                asyncImageAdapter.notifyDataSetChanged();
                tvLastTimeRefresh.setText(top10Activity.getLastLoadTimeText());
                lvArticleList.smoothScrollToPosition(0);
            }
        });

    }

    private void loadArticles(){
        ((Top10Activity)getActivity()).loadRanks(new RankLoadCallback() {
            public void showProgressUI() {
                // do nothing
            }

            public void dismissProgressUI() {
                // do nothing
            }

            public void onRankLoadFinished(Map<String, List<RankArticle>> allRankArticlesMap) {
                Top10ArticleListFragment.this.allRankArticlesMap.clear();
                Top10ArticleListFragment.this.allRankArticlesMap.putAll(allRankArticlesMap);
                asyncImageAdapter.notifyDataSetChanged();
                tvLastTimeRefresh.setText(top10Activity.getLastLoadTimeText());
            }
        });
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

    public interface RankLoadCallback {
        public void showProgressUI();

        public void dismissProgressUI();
        void onRankLoadFinished(Map<String, List<RankArticle>> allRankArticlesMap);        
    }
}
