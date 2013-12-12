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
import org.jandroid.cnbeta.async.ArticleListAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.ImageBytesAsyncTask;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.AbstractListLoader;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.adapter.AsyncImageAdapter;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class ArticleListFragment extends AbstractAsyncListFragment<Article> {

    // 新闻分类
    private AbstractListLoader.Type type;

    private PagingView footerPagingView;

    public ArticleListFragment() {

    }

    private void setType(ArticleListLoader.Type type) {
        this.type = type;
    }

    public ArticleListLoader.Type getType() {
        return type;
    }

    @Override
    public void loadData() {
        executeAsyncTaskMultiThreading(new ArticleListAsyncTask() {
            @Override
            protected ArticleListLoader.Type getCategory() {
                return ArticleListFragment.this.getType();
            }

            @Override
            protected int getPage() {
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<Article>> getAsyncContext() {
                return ArticleListFragment.this;
            }
        });
    }

    public void reloadData() {
        executeAsyncTaskMultiThreading(new ArticleListAsyncTask() {
            @Override
            protected ArticleListLoader.Type getCategory() {
                return ArticleListFragment.this.getType();
            }

            @Override
            protected int getPage() {
                footerPagingView.resetPage();
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<Article>> getAsyncContext() {
                return new HasAsyncDelegate<List<Article>>(ArticleListFragment.this) {
                    @Override
                    public void onSuccess(AsyncResult<List<Article>> listAsyncResult) {
                        clearData();
                        super.onSuccess(listAsyncResult);
                        // scroll to top
                        mListView.setSelection(0);
                    }
                };
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arg = getArguments();
        setType((AbstractListLoader.Type)arg.getSerializable("type"));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), R.layout.listvew_footbar_paging);

        ((ListView)mListView).addFooterView(footerPagingView.getRootView());

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
                                return ArticleListFragment.this.getCnBetaApplicationContext();
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
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.listview_article_item, null);
                }
                Article article = getData(position);
                TextView tvTitleShow = (TextView) convertView.findViewById(R.id.title_show);
                tvTitleShow.setText(article.getTitleShow());
                TextView tvHometextShowShort = (TextView) convertView.findViewById(R.id.hometext);
                tvHometextShowShort.setText(article.getHometextShowShort());
                TextView tvComments = (TextView) convertView.findViewById(R.id.comments);
                tvComments.setText("" + article.getComments());
                TextView tvCounter = (TextView) convertView.findViewById(R.id.counter);
                tvCounter.setText("" + article.getCounter());
                TextView tvTime = (TextView) convertView.findViewById(R.id.time);
                tvTime.setText("" + article.getTime());
        /*
                        TextView tvScore = (TextView)convertView.findViewById(R.id.score);
                        tvScore.setText(""+article.getScore());
        */

                ImageView ivLogo = (ImageView) convertView.findViewById(R.id.item_logo);
                // queue to image load list or set a cached bitmap if has been cached
                ivLogo.setImageBitmap(queueImageView(position, ivLogo, article.getLogo()));

                updateTextSize(
                        new TextView[]{
                                tvTitleShow,
                                tvHometextShowShort,
                                tvComments,
                                tvCounter,
                                tvTime},
                        new int[]{
                                R.dimen.listitem_title_text_size,
                                R.dimen.listitem_description_text_size,
                                R.dimen.listitem_status_text_size,
                                R.dimen.listitem_status_text_size,
                                R.dimen.listitem_status_text_size}
                        );

                updateTypeFace(convertView);
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Article article = (Article) getAdapter().getItem(position);
//        Utils.openContentActivity(getActivity(), article.getSid(), article.getTitleShow());
        Utils.openContentActivity(getActivity(), article, getAllDatas());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    ////////////////////////////////


    @Override
    public void onSuccess(AsyncResult<List<Article>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        footerPagingView.increasePage();
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
