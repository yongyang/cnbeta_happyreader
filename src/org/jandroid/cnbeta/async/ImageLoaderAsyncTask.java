package org.jandroid.cnbeta.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import org.jandroid.cnbeta.loader.ImageLoader;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class ImageLoaderAsyncTask extends BaseAsyncTask<String, Integer, AsyncResult> {

    protected abstract String getImageUrl();
    
    @Override
    protected AsyncResult doInBackground(String... params) {
        try {
            Bitmap bitmap = loadImage(getImageUrl());
            return AsyncResult.successResult(bitmap);
        }
        catch (Exception e) {
            return AsyncResult.errorResult(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public AsyncTask<String, Integer, AsyncResult> executeMultiThread() {
        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    
    protected Bitmap loadImage(String url) throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        ImageLoader imageLoader = new ImageLoader(url);
        //优先从Disk装载
        if(imageLoader.isCached(getCnBetaApplicationContext().getBaseDir())) {
            return imageLoader.fromDisk(getCnBetaApplicationContext().getBaseDir());
        }
        else {
            if(hasNetwork) {
                Bitmap bitmap = imageLoader.fromHttp(getCnBetaApplicationContext().getBaseDir());
                return bitmap;
            }
            else {
                //TODO: 返回一张默认图片，显示没有网络
                return null;
            }
        }
    }

}
