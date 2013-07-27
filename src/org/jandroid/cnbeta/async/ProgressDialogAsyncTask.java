package org.jandroid.cnbeta.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ProgressDialogAsyncTask<P, I, R> extends AsyncTask<P, I ,R> {

    private ProgressDialog progressDialog;

    public abstract ProgressDialog getProgressDialog();

    @SuppressWarnings("unchecked")
    public AsyncTask<P, I ,R> executeMultiThread() {
        //TODO: check network status here

        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = getProgressDialog();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(R r) {
        super.onPostExecute(r);
        progressDialog.dismiss();
    }
}
