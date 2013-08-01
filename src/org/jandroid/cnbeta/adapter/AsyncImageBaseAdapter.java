package org.jandroid.cnbeta.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.async.LoadImageAsyncTask;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
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

    public static final int CACHE_SIZE = 60;
    //memory cache, for sync load in getView, url=>SoftReference
    private static Map<String, Bitmap> cachedBitmaps = new LinkedHashMap<String, Bitmap>(CACHE_SIZE) {
        @Override
        protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            return this.size() > CACHE_SIZE;
        }
    };

    private final Handler postHandler = new Handler();

    private final Map<Integer, QueueImageView> queuedImageViews = new ConcurrentHashMap<Integer, QueueImageView>();

    public abstract int getCount();

    public abstract Object getItem(int position);

    public abstract long getItemId(int position);

    public abstract Bitmap getDefaultBitmap();

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
    protected Bitmap queueImageView(final int position, final ImageView imageView, final String imageUrl) {
        Bitmap bitmap = cachedBitmaps.get(imageUrl);
/*
        if(cachedBitmaps.containsKey(imageUrl)) {
            SoftReference<Bitmap> bitmapSoftReference = cachedBitmaps.get(imageUrl);
            bitmap = bitmapSoftReference.get();
            if(bitmap == null) {
                cachedBitmaps.remove(imageUrl);
            }
        }
*/
        if(bitmap == null) { //not cached
            bitmap = getDefaultBitmap(); // return default bitmap temporarily
            final QueueImageView queueImageView = new QueueImageView(imageView, imageUrl);
            synchronized (queuedImageViews) {
                queuedImageViews.put(position, queueImageView);
            }
        }
        return bitmap;
    }

    private void loadQueuedImages(int firstPosition, int lastPosition) {
        for (int pos = firstPosition; pos < lastPosition; pos++) {
            final int position = pos;
            QueueImageView tempImageLoadInfo;
            synchronized (queuedImageViews) {
                tempImageLoadInfo = queuedImageViews.remove(position);
                //TODO ?? why null, maybe done by other thread or don't need to load because have loaded
                if (tempImageLoadInfo == null) continue;
            }
            final QueueImageView imageLoadInfo = tempImageLoadInfo;
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
                        Bitmap bitmap = (Bitmap) bitmapAsyncResult.getResult();
                        cachedBitmaps.put(imageUrl, (bitmap));
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
