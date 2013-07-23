package org.jandroid.cnbeta.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ProgressDialogAsyncTask<P, I, R> extends AsyncTask<P, I ,R> {

    public abstract ProgressDialog getProgressDialog();

    @SuppressWarnings("unchecked")
    public AsyncTask<P, I ,R> executeMultiThread() {
        //TODO: check network status here

        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
