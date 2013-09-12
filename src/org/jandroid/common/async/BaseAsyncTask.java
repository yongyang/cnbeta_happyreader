package org.jandroid.common.async;

import android.os.AsyncTask;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class BaseAsyncTask<R>  extends AsyncTask<Object, Integer, AsyncResult<R>> {

    protected abstract R run() throws Exception;

    @Override
    protected AsyncResult<R> doInBackground(Object... params) {
        if(!isCancelled()) {
            try {
                R result = run();
                return AsyncResult.successResult(result);
            }
            catch (Exception e) {
                return AsyncResult.errorResult(e.getMessage(), (R)null, e);
            }
        }
        else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(AsyncResult<R> rAsyncResult) {
        if(!isCancelled()) {
            super.onPostExecute(rAsyncResult);
            if(rAsyncResult.isSuccess()) {
                onSuccess(rAsyncResult);
            }
            else {
                onFailure(rAsyncResult);
            }
        }
    }

    protected  abstract void onSuccess(AsyncResult<R> rAsyncResult);

    protected  abstract void onFailure(AsyncResult<R> rAsyncResult);
}
