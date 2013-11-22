package org.jandroid.cnbeta.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.CnBetaPreferences;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.TopicActivity;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.HasAsyncDelegate;
import org.jandroid.cnbeta.async.ImageAsyncTask;
import org.jandroid.cnbeta.async.TopicListAsyncTask;
import org.jandroid.cnbeta.entity.Topic;
import org.jandroid.cnbeta.view.PagingView;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.FontUtils;
import org.jandroid.common.adapter.AsyncImageAdapter;
import org.jandroid.common.async.AsyncResult;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public class TopicListFragment extends AbstractAsyncListFragment<Topic> {


    private PagingView footerPagingView;

    private LinearLayout footerContainer;

    public TopicListFragment() {
    }

    @Override
    public void loadData() {
/*
        if(footerPagingView.getPage() == TopicListLoader.MAX_PAGE) { //已经达到最大页
            return;
        }
*/
        executeAsyncTaskMultiThreading(new TopicListAsyncTask() {

            @Override
            protected int getPage() {
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<Topic>> getAsyncContext() {
                return TopicListFragment.this;
            }
        });
    }

    public void reloadData() {
        executeAsyncTaskMultiThreading(new TopicListAsyncTask() {
            @Override
            protected int getPage() {
                footerPagingView.resetPage();
                return footerPagingView.getNextPage();
            }

            @Override
            public HasAsync<List<Topic>> getAsyncContext() {
                return new HasAsyncDelegate<List<Topic>>(TopicListFragment.this) {
                    @Override
                    public void onSuccess(AsyncResult<List<Topic>> listAsyncResult) {
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
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.topics, container, false);
        mListView = (GridView) rootView.findViewById(R.id.gridview_topics);
        footerContainer = (LinearLayout) rootView.findViewById(R.id.footer_container);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        footerPagingView = PagingView.load(getActivity().getLayoutInflater(), footerContainer, R.layout.listvew_footbar_paging);

//        ((ListView)mListView).addFooterView(footerPagingView.getRootView());

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
                                return TopicListFragment.this.getCnBetaApplicationContext();
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
                Topic article = getData(position);
                TextView tvName = (TextView) convertView.findViewById(R.id.name);
                tvName.setText(article.getName());

                ImageView ivLogo = (ImageView) convertView.findViewById(R.id.logo);
                // queue to image load list or set a cached bitmap if has been cached
                ivLogo.setImageBitmap(queueImageView(position, ivLogo, article.getLogo()));

                int fontSizeOffset = ((CnBetaApplicationContext)getActivity().getApplicationContext()).getCnBetaPreferences().getFontSizeOffset();
                FontUtils.updateTextSize(getActivity(), tvName, R.dimen.listitem_status_text_size, fontSizeOffset);

                updateTypeFace(convertView);
                return convertView;
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Topic article = (Topic) getAdapter().getItem(position);
        TopicActivity topicActivity = (TopicActivity) getActivity();
        topicActivity.openTopic(article.getId(), article.getName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO: 保存当前article列表
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSuccess(AsyncResult<List<Topic>> listAsyncResult) {
        super.onSuccess(listAsyncResult);
        footerPagingView.increasePage();
//            mListView.smoothScrollToPosition(mListView.getAdapter().getCount());
/*
        if(footerPagingView.getPage() == TopicListLoader.MAX_PAGE) {
            footerPagingView.setEnable(false);
        }
*/
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
        updateTypeFace(footerPagingView.getRootView());
        super.onResume();
    }

}
