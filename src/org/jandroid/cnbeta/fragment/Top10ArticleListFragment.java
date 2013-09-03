package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.ImageAsyncTask;
import org.jandroid.cnbeta.async.LoadingAsyncTask;
import org.jandroid.cnbeta.entity.RankArticle;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.adapter.AsyncImageAdapter;
import org.jandroid.common.async.AsyncResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class Top10ArticleListFragment extends AbstractListFragment<RankArticle> {

    // onReselect tab 的时候进行切换
    private Top10Activity.RankType[] rankTypes;
    private int currentRankTypeIndex = 0;

    private Top10Activity top10Activity;

    public Top10ArticleListFragment(Top10Activity.RankType... rankTypes) {
        this.rankTypes = rankTypes;
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
        super.onAttach(activity);
        this.top10Activity = (Top10Activity)activity;
    }

    @Override
    protected BaseAdapter newAdapter() {
        return new AsyncImageAdapter() {
                    @Override
                    public int getCount() {
                        return loadedDatas.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return loadedDatas.get(position);
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
                        executeAsyncTaskMultiThreading(new ImageAsyncTask() {

                            @Override
                            protected String getImageUrl() {
                                return imageUrl;
                            }

                            @Override
                            public HasAsync<Bitmap> getAsyncContext() {
                                return new HasAsync<Bitmap>() {
                                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                                        return (CnBetaApplicationContext)getActivity().getApplicationContext();
                                    }

                                    public void onProgressShow() {

                                    }

                                    public void onProgressDismiss() {

                                    }

                                    public void onSuccess(AsyncResult<Bitmap> bitmapAsyncResult) {
                                        onAsyncImageLoadListener.onLoaded(bitmapAsyncResult.getResult());

                                    }

                                    public void onFailure(AsyncResult<Bitmap> bitmapAsyncResult) {
                                        onAsyncImageLoadListener.onLoadFailed(bitmapAsyncResult.getErrorMsg(), bitmapAsyncResult.getException());
                                    }

                                };
                            }

                            @Override
                            protected void onCancelled() {
                                onAsyncImageLoadListener.onLoadFailed("Cancelled", null);
                            }

                            @Override
                            protected void onCancelled(AsyncResult bitmapAsyncResult) {
                                onAsyncImageLoadListener.onLoadFailed("Cancelled", null);
                            }
                        }
                        );
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RankArticle article = (RankArticle)getAdapter().getItem(position);
        Utils.openContentActivity(getActivity(), article.getSid(), article.getTitle());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        top10Activity.loadRanks(this);
    }

    public void updateData(Map<String, List<RankArticle>>  rankArticlesMap){
        loadedDatas.clear();
        appendDatas(rankArticlesMap.get(rankTypes[currentRankTypeIndex].getType()));
//        getAdapter().notifyDataSetChanged();
//        tvLastTimeRefresh.setText(top10Activity.getLastLoadTimeText());
    }


    public void switchRankType(Map<String, List<RankArticle>>  rankArticlesMap){
        if(rankTypes.length > 1) {
            if(rankTypes.length > currentRankTypeIndex+1){
                currentRankTypeIndex++;
            }
            else {
                currentRankTypeIndex=0;
            }
            updateData(rankArticlesMap);
        }
    }
}
