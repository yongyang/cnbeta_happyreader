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

    private String localFileName;

    private byte[] imageData;

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

    public byte[] getImageData() {
        return imageData;
    }

    @SuppressWarnings("unchecked")
    public AsyncTask<String, Integer, AsyncResult> executeMultiThread() {
        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String getLocalFileName() {
        return localFileName;
    }

    protected Bitmap loadImage(String url) throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        ImageLoader imageLoader = new ImageLoader(url);
        localFileName = imageLoader.getFileName();
        //优先从Disk装载
        if(imageLoader.isCached(getCnBetaApplicationContext().getBaseDir())) {
            Bitmap bitmap = imageLoader.fromDisk(getCnBetaApplicationContext().getBaseDir());
            imageData = imageLoader.getImageData();
            return bitmap;
        }
        else {
            if(hasNetwork) {
                Bitmap bitmap = imageLoader.fromHttp(getCnBetaApplicationContext().getBaseDir());
                imageData = imageLoader.getImageData();
                return bitmap;
            }
            else {
                //TODO: 返回一张默认图片，显示没有网络
                return null;
            }
        }
    }

}
