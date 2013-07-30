package org.jandroid.cnbeta.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import org.jandroid.cnbeta.loader.LoaderManager;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class LoadImageAsyncTask extends BaseAsyncTask<String, Integer, AsyncResult> {

    private String imgUrl;

    public LoadImageAsyncTask(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    
    @Override
    protected AsyncResult doInBackground(String... params) {
        try {
            Bitmap bitmap = LoaderManager.getInstance().loadImage(imgUrl);
            return AsyncResult.successResult(bitmap);
        }
        catch (Exception e) {
            return AsyncResult.errorResult(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public AsyncTask<String, Integer, AsyncResult> executeMultiThread() {
        //TODO: check network status here
        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
