package org.jandroid.cnbeta.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.ArticleListAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.ImageAsyncTask;
import org.jandroid.cnbeta.async.TopicArticleListAsyncTask;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.TopicArticle;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.adapter.AsyncImageAdapter;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class TopicArticleListFragment extends AbstractAsyncListFragment<TopicArticle> {

    protected long topicId;
    protected int page = 1;
    protected int splitPage = 1;
    public static final int splitCount = 3;

    private PagingView footerPagingView;

    public TopicArticleListFragment(long topicId) {
        this.topicId = topicId;
    }

    public int getSplitPage() {
        return splitPage;
    }

    public long getTopicId() {
        return topicId;
    }

    public int getPage() {
        return page;
    }

    @Override
    protected void loadData() {
        executeAsyncTaskMultiThreading(new TopicArticleListAsyncTask() {

            @Override
            protected long getId() {
                return TopicArticleListFragment.this.getTopicId();
            }

            @Override
            protected int getSplitPage() {
                return TopicArticleListFragment.this.getSplitPage();
            }

            @Override
            protected int getPage() {
                return TopicArticleListFragment.this.getPage();
            }

            @Override
            public HasAsync<List<TopicArticle>> getAsyncContext() {
                return TopicArticleListFragment.this;
            }
        });
    }

    protected void reloadData() {
        page = 1;
        splitPage =1;
        executeAsyncTaskMultiThreading(new TopicArticleListAsyncTask() {
            @Override
            protected long getId() {
                return TopicArticleListFragment.this.getTopicId();
            }

            @Override
            protected int getSplitPage() {
                return TopicArticleListFragment.this.getSplitPage();
            }

            @Override
            protected int getPage() {
                return TopicArticleListFragment.this.getPage();
            }

            @Override
            public HasAsync<List<TopicArticle>> getAsyncContext() {
                return new HasAsyncDelegate<List<TopicArticle>>(TopicArticleListFragment.this) {
                    @Override
                    public void onSuccess(AsyncResult<List<TopicArticle>> listAsyncResult) {
                        clearData();
                        super.onSuccess(listAsyncResult);
                    }
                };
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), R.layout.listvew_footbar_paging);

        mListView.addFooterView(footerPagingView.getRootView());

        footerPagingView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadData();
            }
        });

        super.onActivityCreated(savedInstanceState);

    }


    @Override
    protected BaseAdapter newAdapter() {
        return new AsyncImageAdapter() {
            @Override
            public int getCount() {
                return getDataSize();
            }

            @Override
            public Object getItem(int position) {
                return getData(position);
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
                ((BaseActivity) getActivity()).executeAsyncTaskMultiThreading(new ImageAsyncTask() {

                    @Override
                    public HasAsync<Bitmap> getAsyncContext() {
                        return new HasAsync<Bitmap>() {
                            public CnBetaApplicationContext getCnBetaApplicationContext() {
                                return TopicArticleListFragment.this.getCnBetaApplicationContext();
                            }

                            public void onProgressShow() {
                            }

                            public void onProgressDismiss() {
                            }

                            public void onFailure(AsyncResult<Bitmap> bitmapAsyncResult) {
                                onAsyncImageLoadListener.onLoadFailed(bitmapAsyncResult.getErrorMsg(), bitmapAsyncResult.getException());
                            }

                            public void onSuccess(AsyncResult<Bitmap> bitmapAsyncResult) {
                                onAsyncImageLoadListener.onLoaded(bitmapAsyncResult.getResult());
                            }
                        };
                    }

                    @Override
                    protected String getImageUrl() {
                        return imageUrl;
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
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_topic_item, null);
                }
                TopicArticle article = getData(position);
                TextView tvTitleShow = (TextView) convertView.findViewById(R.id.tile_show);
                tvTitleShow.setText(article.getTitleShow());
                TextView tvHometextShowShort2 = (TextView) convertView.findViewById(R.id.hometext_show_short2);
                tvHometextShowShort2.setText(article.getHometextShowShort2());
                TextView tvComments = (TextView) convertView.findViewById(R.id.comments);
                tvComments.setText("" + article.getComments());
                TextView tvTime = (TextView) convertView.findViewById(R.id.time);
                tvTime.setText("" + article.getTime());
        /*
                        TextView tvScore = (TextView)convertView.findViewById(R.id.score);
                        tvScore.setText(""+article.getScore());
        */


                ImageView ivLogo = (ImageView) convertView.findViewById(R.id.item_logo);
                // queue to image load list or set a cached bitmap if has been cached
                ivLogo.setImageBitmap(queueImageView(position, ivLogo, article.getLogo()));
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TopicArticle article =  (TopicArticle)getAdapter().getItem(position);
        Utils.openContentActivity(getActivity(), article.getSid(), article.getTitleShow());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO: 保存当前article列表
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        //TODO: 恢复保存的article列表
        super.onViewStateRestored(savedInstanceState);
    }

    ////////////////////////////////


    @Override
    public void onSuccess(AsyncResult<List<TopicArticle>> listAsyncResult) {
        super.onSuccess(listAsyncResult);

        //需要控制显示正确的页面，第一页时  1/4 2/4 4/3 4/4
        if(page == 1 && splitPage < splitCount) {
            footerPagingView.setPage(page, splitPage + "/" + splitCount);
            splitPage++;
        }
        else {
            footerPagingView.setPage(page);
            page++;
            splitPage = 1;
        }
    }

    @Override
    public void onProgressShow() {
        //TODO: refresh action view only page=1
        super.onProgressShow();
        footerPagingView.onProgressShow();
        if (getPage() == 1) { //page 1 is reload
//            startRotateRefreshActionView();
        }

    }

    @Override
    public void onProgressDismiss() {
        //TODO: refresh action view only page=1
        super.onProgressDismiss();
        footerPagingView.onProgressDismiss();
        // stop refresh rotation anyway
        if (getPage() == 1) { //page 1 is reload
//            stopRotateRefreshActionView();
        }
    }


}
