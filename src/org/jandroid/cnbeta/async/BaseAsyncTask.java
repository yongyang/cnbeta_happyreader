package org.jandroid.cnbeta.async;

import android.os.AsyncTask;
import org.jandroid.cnbeta.CnBetaApplicationContext;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class BaseAsyncTask<Params, Progress, Result>  extends AsyncTask<Params, Progress, Result> {

    // if only load from local storage, when List init, need to load cache data first from local
    private boolean localLoad;

    public boolean isLocalLoad() {
        return localLoad;
    }

    public BaseAsyncTask setLocalLoad(boolean localLoad) {
        this.localLoad = localLoad;
        return this;
    }

    public abstract CnBetaApplicationContext getCnBetaApplicationContext();

    @SuppressWarnings("unchecked")
    public AsyncTask<Params, Progress, Result> executeMultiThread() {
        return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
