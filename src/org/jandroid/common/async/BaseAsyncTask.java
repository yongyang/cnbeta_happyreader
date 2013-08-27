package org.jandroid.common.async;

import android.os.AsyncTask;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class BaseAsyncTask<R>  extends AsyncTask<Object, Integer, AsyncResult<R>> {

    protected abstract R run() throws Exception;

    @Override
    protected AsyncResult doInBackground(Object... params) {
        try {
            R result = run();
            return AsyncResult.successResult(result);
        }
        catch (Exception e) {
            return AsyncResult.errorResult(e.getMessage(), null, e);
        }

    }
}
