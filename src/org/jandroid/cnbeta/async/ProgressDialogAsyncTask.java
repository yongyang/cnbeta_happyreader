package org.jandroid.cnbeta.async;

import org.jandroid.common.async.AsyncResult;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
    public abstract class ProgressDialogAsyncTask<R> extends LoadingAsyncTask<R> {

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
