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
    
    
    private final Handler postHandler = new Handler(); 

    private Map<Integer, QueueImageLoader> queuedImageLoaders = new HashMap<Integer, QueueImageLoader>();

    //TODO: 需要解决 重复加载 的问题
    private List<Integer> loadingPositions = new ArrayList<Integer>();
    
    public abstract int getCount();
    public abstract Object getItem(int position);
    public abstract long getItemId(int position);
    public abstract int[] getVisiblePositions();


    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                loadQueuedImages();
                break;
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
        QueueImageLoader queueImageLoader = new QueueImageLoader(position, imageView, srcUrl);
        queuedImageLoaders.put(position, queueImageLoader);
        //TODO: queue to loading
    }
    
    protected void loadQueuedImages(){      
        // only load image for visible lines of ListView
        for(final int position : getVisiblePositions()){
            if(loadingPositions.contains(position)) {
                // 正在加载，跳过
                continue;
            }
            loadingPositions.add(position);
            final QueueImageLoader imageLoadInfo = queuedImageLoaders.get(position);            
            new LoadImageAsyncTask(){
                @Override
                protected void onPostExecute(final AsyncResult<Bitmap> bitmapAsyncResult) {
                    super.onPostExecute(bitmapAsyncResult);
                    loadingPositions.remove(position);
                    postHandler.post(new Runnable() {
                        public void run() {
                            imageLoadInfo.imageView.setImageDrawable(new BitmapDrawable(bitmapAsyncResult.getResult()));
                        }
                    });
                }
                @Override
                protected void onCancelled() {
                    loadingPositions.remove(position);
                }

                @Override
                protected void onCancelled(AsyncResult<Bitmap> bitmapAsyncResult) {
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
    }
    
    
}
