package org.jandroid.cnbeta.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.async.LoadImageAsyncTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ListView.setAdapter();
 * ListView.setOnScrollListener();
 *
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:23 PM
 */
public abstract class AsyncImageBaseAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    //TODO: don't need to use memory cache, use disk cache in ImageLoader instead

    private final Handler postHandler = new Handler();

    private final Map<Integer, QueueImageView> queuedImageViews = new ConcurrentHashMap<Integer, QueueImageView>();

    //TODO: 需要解决 重复加载 的问题, 需要 cancel 之前的 AsyncTask
//    private final List<String> loadingImages = new ArrayList<String>();

    public abstract int getCount();

    public abstract Object getItem(int position);

    public abstract long getItemId(int position);

    //TODO:
//    public abstract void onImageLoadFailure();
    
    private volatile int firstVisiblePosition = 0;
    private volatile int lastVisiblePosition = 0;
    private volatile int scrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                loadQueuedImages(firstVisiblePosition, lastVisiblePosition);
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
            default:
                break;
        }
        this.scrollState = scrollState;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisiblePosition = firstVisibleItem;
        this.lastVisiblePosition = firstVisibleItem + visibleItemCount;
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            loadQueuedImages(firstVisiblePosition, lastVisiblePosition);
        }
    }

    /**
     * call this method on getView
     *
     * @param position, the position in the ListView, used to determine if really need to load after scroll stopped
     * @param imageView
     * @param imageUrl
     */
    protected void queueImageView(final int position, final ImageView imageView, final String imageUrl) {
        final QueueImageView queueImageView = new QueueImageView(imageView, imageUrl);
        synchronized (queuedImageViews) {
            queuedImageViews.put(position, queueImageView);
        }
    }

    private void loadQueuedImages(int firstPosition, int lastPosition) {
        for (int pos = firstPosition; pos < lastPosition; pos++) {
            final int position = pos;
            QueueImageView tempImageLoadInfo;
            synchronized (queuedImageViews) {
                tempImageLoadInfo = queuedImageViews.remove(position);
                if (tempImageLoadInfo == null) continue;
            }
            final QueueImageView imageLoadInfo = tempImageLoadInfo;

            //TODO ?? why null, maybe done by other thread or don't need to load because have loaded
            final String imageUrl = imageLoadInfo.getImageUrl();
            if (imageUrl == null) continue; //TODO: why null???

/*
            synchronized (loadingImages) {
                if (loadingImages.contains(imageLoadInfo.getSrcUrl())) {
                    // 正在加载，跳过
                    continue;
                }

                loadingImages.add(imageUrl);
            }
*/

            new LoadImageAsyncTask(imageUrl) {
                @Override
                protected void onPostExecute(final AsyncResult bitmapAsyncResult) {
                    super.onPostExecute(bitmapAsyncResult);
                    if (bitmapAsyncResult.isSuccess()) {
                        postHandler.post(new Runnable() {
                            public void run() {
                                imageLoadInfo.getImageView().setImageBitmap((Bitmap) bitmapAsyncResult.getResult());
                            }
                        });
/*
                        synchronized (loadingImages) {
                            loadingImages.remove(imageUrl);
                        }
*/
                    }
                    else {
                        //TODO: toast or anything
                        Log.w(this.getClass().getSimpleName(), bitmapAsyncResult.getErrorMsg());
                    }
                }

                @Override
                protected void onCancelled() {
/*
                    synchronized (loadingImages) {
                        loadingImages.remove(position);
                    }
*/
                }

                @Override
                protected void onCancelled(AsyncResult bitmapAsyncResult) {
/*
                    synchronized (loadingImages) {
                        loadingImages.remove(position);
                    }
*/
                }
            }.executeMultiThread();
        }
        // clear all queued loaders
//        queuedImageLoaders.clear();
    }

    static class QueueImageView {
        private ImageView imageView;
        private String imageUrl;

        QueueImageView(ImageView imageView, String imageUrl) {
            this.imageView = imageView;
            this.imageUrl = imageUrl;
        }

        ImageView getImageView() {
            return imageView;
        }

        String getImageUrl() {
            return imageUrl;
        }
    }
}
