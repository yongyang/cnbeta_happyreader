package org.jandroid.cnbeta.async;

import android.os.AsyncTask;
import org.jandroid.cnbeta.CnBetaApplicationContext;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class BaseAsyncTask<Params, Progress, Result>  extends AsyncTask<Params, Progress, Result> {

    public abstract CnBetaApplicationContext getCnBetaApplicationContext();

    @SuppressWarnings("unchecked")
    public AsyncTask<Params, Progress, Result> executeMultiThread() {
        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
