package org.jandroid.cnbeta.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.jandroid.cnbeta.CnBetaApplicationContext;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ProgressDialogAsyncTask<R> extends BaseAsyncTask<R> {

    public abstract void showProgressUI();
    public abstract void dismissProgressUI();

    @Override
    protected void onPreExecute() {
        showProgressUI();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(AsyncResult<R> rAsyncResult) {
        dismissProgressUI();
        super.onPostExecute(rAsyncResult);
    }
}
