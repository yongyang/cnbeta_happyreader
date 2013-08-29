package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.common.async.BaseAsyncTask;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class LoadingAsyncTask<R>  extends BaseAsyncTask<R> {

    protected boolean isRemoteLoadOnly() {
        return false;
    }
    
    // 是否只能本地加载
    protected boolean isLocalLoadOnly() {
        return false;
    }

    //是否优先本地加载
    protected boolean isLocalLoadFirst(){
        return false;
    }

    public abstract AbstractLoader<R> getLoader();

    public abstract CnBetaApplicationContext getCnBetaApplicationContext();

    protected R run() throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        
        if(!hasNetwork && isRemoteLoadOnly()) {
            throw new Exception("No network!");
        }
        
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

}
