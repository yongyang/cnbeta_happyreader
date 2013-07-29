package org.jandroid.cnbeta.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.async.LoadImageAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ListView.setAdapter();
 * ListView.setOnScrollListener();
 * 
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:23 PM
 */
public abstract class AsyncImageBaseAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    //TODO: don't need to use memory canche, use disk cache in ImageLoader instead
    
    private final Handler postHandler = new Handler(); 

    private Map<Integer, QueueImageLoader> queuedImageLoaders = new HashMap<Integer, QueueImageLoader>();

    //TODO: 需要解决 重复加载 的问题
    private List<Integer> loadingPositions = new ArrayList<Integer>();
    
    public abstract int getCount();
    public abstract Object getItem(int position);
    public abstract long getItemId(int position);

    public abstract int getFirstVisibleItemPosition();
    public abstract int getLastVisibleItemPosition();

//    public abstract void onImageLoadFailure();

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                loadQueuedImages();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
            default:
                break;
        }
    }
    
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * call this method on getView
     * 
     * @param position, the position in the ListView, used to determine if really need to load after scroll stopped
     * @param imageView
     * @param srcUrl
     */
    protected void queueLoadImage(final int position, final ImageView imageView, final String srcUrl) {
        final QueueImageLoader queueImageLoader = new QueueImageLoader(position, imageView, srcUrl);
        queuedImageLoaders.put(position, queueImageLoader);

        //load first page immediately
        if(position < 20) { //TODO: 20 or other number
            new LoadImageAsyncTask(queueImageLoader.getSrcUrl()){
                @Override
                protected void onPostExecute(final AsyncResult bitmapAsyncResult) {
                    super.onPostExecute(bitmapAsyncResult);
                    if(bitmapAsyncResult.isSuccess()) {
                        //TODO: synchronize
                    postHandler.post(new Runnable() {
                        public void run() {
                            queueImageLoader.getImageView().setImageDrawable(new BitmapDrawable((Bitmap)bitmapAsyncResult.getResult()));
                        }
                    });
                    }
                    else {
                        //TODO: toast or anything
                    }
                }
                @Override
                protected void onCancelled() {
                }

                @Override
                protected void onCancelled(AsyncResult bitmapAsyncResult) {
                }
            }.executeMultiThread();

        }
        //TODO: queue to loading
    }
    
    protected void loadQueuedImages(){      
        // only load image for visible lines of ListView
        for(int pos = getFirstVisibleItemPosition(); pos<= getLastVisibleItemPosition(); pos++){
            final int position = pos;
            if(loadingPositions.contains(position)) {
                // 正在加载，跳过
                continue;
            }
            loadingPositions.add(position);
            final QueueImageLoader imageLoadInfo = queuedImageLoaders.get(position);            
            new LoadImageAsyncTask(imageLoadInfo.getSrcUrl()){
                @Override
                protected void onPostExecute(final AsyncResult bitmapAsyncResult) {
                    super.onPostExecute(bitmapAsyncResult);
                    if(bitmapAsyncResult.isSuccess()) {
                        //TODO: synchronize
//                    loadingPositions.remove((Object)position);
                    postHandler.post(new Runnable() {
                        public void run() {
                          imageLoadInfo.getImageView().setImageDrawable(new BitmapDrawable((Bitmap)bitmapAsyncResult.getResult()));
                        }
                    });
                    }
                    else {
                        //TODO: toast or anything
                    }
                }
                @Override
                protected void onCancelled() {
                    loadingPositions.remove(position);
                }

                @Override
                protected void onCancelled(AsyncResult bitmapAsyncResult) {
                    loadingPositions.remove(position);
                }
            }.executeMultiThread();
        }
        // clear all queued loaders
        queuedImageLoaders.clear();
    }
    
    static class QueueImageLoader {
        int position;
        ImageView imageView;
        String srcUrl;

        QueueImageLoader(int position, ImageView imageView, String srcUrl) {
            this.position = position;
            this.imageView = imageView;
            this.srcUrl = srcUrl;
        }

        ImageView getImageView() {
            return imageView;
        }

        int getPosition() {
            return position;
        }

        String getSrcUrl() {
            return srcUrl;
        }
    }
    
    
}
