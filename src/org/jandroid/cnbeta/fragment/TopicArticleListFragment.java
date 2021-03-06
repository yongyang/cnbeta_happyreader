package org.jandroid.cnbeta.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.ImageBytesAsyncTask;
import org.jandroid.cnbeta.async.TopicArticleListAsyncTask;
import org.jandroid.cnbeta.entity.TopicArticle;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.FontUtils;
import org.jandroid.common.adapter.AsyncImageAdapter;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class TopicArticleListFragment extends AbstractAsyncListFragment<TopicArticle> {

    protected long topicId;
    protected String topicName;

    protected int splitPage = 0;
    public static final int splitCount = 3;

    private PagingView footerPagingView;

    public TopicArticleListFragment() {
    }

    private void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    private void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getSplitPage() {
        return splitPage;
    }

    public long getTopicId() {
        return topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    @Override
    public void loadData() {
        executeAsyncTaskMultiThreading(new TopicArticleListAsyncTask() {

            @Override
            protected long getId() {
                return TopicArticleListFragment.this.getTopicId();
            }

            @Override
            protected int getSplitPage() {
                return TopicArticleListFragment.this.getSplitPage() + 1;
            }

            @Override
            protected int getPage() {
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<TopicArticle>> getAsyncContext() {
                return TopicArticleListFragment.this;
            }
        });
    }

    public void updateTopicId(long topicId) {
        this.topicId = topicId;
        clearData();
        getAdapter().notifyDataSetChanged();
        reloadData();
    }

    public void reloadData() {
        splitPage = 0;
        executeAsyncTaskMultiThreading(new TopicArticleListAsyncTask() {
            @Override
            protected long getId() {
                return TopicArticleListFragment.this.getTopicId();
            }

            @Override
            protected int getSplitPage() {
                return TopicArticleListFragment.this.getSplitPage() + 1;
            }

            @Override
            protected int getPage() {
                footerPagingView.resetPage();
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<TopicArticle>> getAsyncContext() {
                return new HasAsyncDelegate<List<TopicArticle>>(TopicArticleListFragment.this) {
                    @Override
                    public void onSuccess(AsyncResult<List<TopicArticle>> listAsyncResult) {
                        clearData();
                        super.onSuccess(listAsyncResult);
                        mListView.setSelection(0);
                    }
                };
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        setTopicId(args.getLong("id"));
        setTopicName(args.getString("name"));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), R.layout.listview_footbar_paging);
        ((ListView) mListView).addFooterView(footerPagingView.getRootView());

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
                ((BaseActivity) getActivity()).executeAsyncTaskMultiThreading(new ImageBytesAsyncTask() {

                    @Override
                    public HasAsync<byte[]> getAsyncContext() {
                        return new HasAsync<byte[]>() {
                            public CnBetaApplicationContext getCnBetaApplicationContext() {
                                return TopicArticleListFragment.this.getCnBetaApplicationContext();
                            }

                            public void onProgressShow() {
                            }

                            public void onProgressDismiss() {
                            }

                            public void onFailure(AsyncResult<byte[]> bitmapAsyncResult) {
                                onAsyncImageLoadListener.onLoadFailed(bitmapAsyncResult.getErrorMsg(), bitmapAsyncResult.getException());
                            }

                            public void onSuccess(AsyncResult<byte[]> bitmapAsyncResult) {
                                byte[] bytes = bitmapAsyncResult.getResult();
                                onAsyncImageLoadListener.onLoaded(BitmapFactory.decodeByteArray(bytes, 0 , bytes.length));
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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_topic_article_item, null);
                }
                TopicArticle article = getData(position);
                TextView tvTitleShow = (TextView) convertView.findViewById(R.id.title_show);
                tvTitleShow.setText(article.getTitleShow());
                checkRead(article, tvTitleShow);

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

                int fontSizeOffset = ((CnBetaApplicationContext)getActivity().getApplicationContext()).getPrefsObject().getFontSizeOffset();
                FontUtils.updateTextSize(getActivity(), tvTitleShow, R.dimen.listitem_title_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvHometextShowShort2, R.dimen.listitem_description_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvComments, R.dimen.listitem_status_text_size, fontSizeOffset);
                FontUtils.updateTextSize(getActivity(), tvTime, R.dimen.listitem_status_text_size, fontSizeOffset);

                updateTypeFace(convertView);
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TopicArticle article = (TopicArticle) getAdapter().getItem(position);
        Utils.openContentActivity(getActivity(), article, getAllDatas());
    }

    ////////////////////////////////

    @Override
    public void onSuccess(AsyncResult<List<TopicArticle>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        //需要控制显示正确的页面，第一页时  1/3 2/3 1
        if (footerPagingView.getPage() + 1 == 1 && splitPage + 1 < splitCount) {
            splitPage++;
            footerPagingView.setPage(footerPagingView.getPage(), splitPage + "/" + splitCount);
        }
        else {
            splitPage = 0;
            footerPagingView.increasePage();
        }
    }

    @Override
    public void onProgressShow() {
        super.onProgressShow();
        footerPagingView.onProgressShow();
    }

    @Override
    public void onProgressDismiss() {
        super.onProgressDismiss();
        footerPagingView.onProgressDismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTypeFace(footerPagingView.getRootView());
    }

}
