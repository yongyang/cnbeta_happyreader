package org.jandroid.cnbeta.async;

import android.os.AsyncTask;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.loader.AbstractLoader;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class BaseAsyncTask<R>  extends AsyncTask<Object, Integer, AsyncResult<R>> {

    // if only load from local storage, when List init, need to load cache data first from local
    protected boolean isLocalLoadOnly() {
        return false;
    }

    //是否优先本地加载
    protected boolean isLocalLoadFirst(){
        return false;
    }

    public abstract AbstractLoader<R> getLoader();

    public abstract CnBetaApplicationContext getCnBetaApplicationContext();

    protected R load() throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        AbstractLoader imageLoader = getLoader();
        //优先从Disk装载
        if(isLocalLoadFirst() || isLocalLoadOnly()) {
            if(imageLoader.isCached(getCnBetaApplicationContext().getBaseDir())) {
                return (R)imageLoader.fromDisk(getCnBetaApplicationContext().getBaseDir());
            }
        }
        if(!isLocalLoadOnly()) {
            return (R)imageLoader.fromHttp(getCnBetaApplicationContext().getBaseDir());
        }
        return null;
    }

    @Override
    protected AsyncResult doInBackground(Object... params) {
        try {
            R result = load();
            return AsyncResult.successResult(result);
        }
        catch (Exception e) {
            return AsyncResult.errorResult(e.getMessage());
        }

    }
}
