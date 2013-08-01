package org.jandroid.cnbeta.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.jandroid.cnbeta.CnBetaApplicationContext;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ProgressDialogAsyncTask<Params, Progress, Result> extends BaseAsyncTask<Params, Progress, Result> {

    private ProgressDialog progressDialog;

    public abstract ProgressDialog getProgressDialog();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = getProgressDialog();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Result r) {
        super.onPostExecute(r);
        progressDialog.dismiss();
    }

    @SuppressWarnings("unchecked")
    public AsyncTask<Params, Progress, Result> executeMultiThread() {
        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
