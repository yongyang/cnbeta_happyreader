package org.jandroid.cnbeta.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public class LoadImageAsyncTask extends AsyncTask<String, Integer, AsyncResult<Bitmap>> {

    @Override
    protected AsyncResult<Bitmap> doInBackground(String... params) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @SuppressWarnings("unchecked")
    public AsyncTask<String, Integer, AsyncResult<Bitmap>> executeMultiThread() {
        //TODO: check network status here

        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
