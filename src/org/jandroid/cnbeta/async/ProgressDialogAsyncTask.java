package org.jandroid.cnbeta.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.jandroid.cnbeta.CnBetaApplicationContext;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ProgressDialogAsyncTask<Params, Progress, Result> extends BaseAsyncTask<Params, Progress, Result> {

    public abstract void showProgressUI();
    public abstract void dismissProgressUI();

    @Override
    protected void onPreExecute() {
        showProgressUI();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Result r) {
        dismissProgressUI();
        super.onPostExecute(r);
    }

    @SuppressWarnings("unchecked")
    public AsyncTask<Params, Progress, Result> executeMultiThread() {
        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
