package org.jandroid.cnbeta.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import org.jandroid.cnbeta.loader.LoaderManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class LoadImageAsyncTask extends BaseAsyncTask<String, Integer, AsyncResult> {

    private String imageUrl;

    public LoadImageAsyncTask(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    @Override
    protected AsyncResult doInBackground(String... params) {
        try {

            if(imageUrl.toLowerCase().endsWith(".gif")) {
                Log.d(this.getClass().getSimpleName(), imageUrl);
            }

            Bitmap bitmap = LoaderManager.getInstance().loadImage(imageUrl);
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
