package org.jandroid.cnbeta.async;

import android.app.Application;
import org.jandroid.cnbeta.client.RequestContext;
import org.jandroid.cnbeta.exception.InfoException;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.common.Logger;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;
import org.jandroid.common.async.BaseAsyncTask;

import java.io.File;
import java.net.ConnectException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class AbstractLoaderAsyncTask<R> extends BaseAsyncTask<R> implements RequestContext {

    protected Logger logger = Logger.getLogger(this.getClass());

    private final ReentrantLock locker = new ReentrantLock();

    protected boolean isRemoteLoadOnly() {
        return false;
    }

    // 是否只能本地加载
    protected boolean isLocalLoadOnly() {
        return false;
    }

    //是否优先本地加载
    protected boolean isLocalLoadFirst() {
        return false;
    }

    public abstract AbstractLoader<R> getLoader();

    public abstract HasAsync<R> getAsyncContext();

//    public abstract CnBetaApplicationContext getCnBetaApplicationContext();

    protected File getLocalCacheDir() {
        return getAsyncContext().getCnBetaApplicationContext().getLocalCacheDir();
    }

    protected R run(Object... params) throws Exception {
        if(isCancelled()) {
            return null;
        }
        boolean hasNetwork = getAsyncContext().getCnBetaApplicationContext().isNetworkConnected();
        AbstractLoader loader = getLoader();

        if(hasNetwork) {
            if(isLocalLoadOnly() || (isLocalLoadFirst() && loader.isCached(getLocalCacheDir()))) {
                //优先从Disk装载
                return (R) loader.diskLoad(getLocalCacheDir());
            }
            else {
                return (R) loader.httpLoad(getLocalCacheDir(), this);
            }
        }
        else {
            if(isRemoteLoadOnly()) {
                throw new ConnectException("没有网络连接，无法加载数据！");
            }
            else {
                return (R) loader.diskLoad(getLocalCacheDir());
            }
        }
    }

    @Override
    protected void onFailure(AsyncResult<R> asyncResult) {
        logger.w(asyncResult.getErrorMsg(), asyncResult.getException());
        if(!isCancelled()) {
            // 只有处理底层错误类异常, 所有信息提示类异常继承自 InfoException
            if((asyncResult.getException() != null) && !(asyncResult.getException() instanceof InfoException)) {
                ToastUtils.showShortToast((Application) getAsyncContext().getCnBetaApplicationContext(), asyncResult.getException().toString());
            }
            locker.lock(); //防止快速重复点击造成 ListView data 数据不一致
            try {
                getAsyncContext().onFailure(asyncResult);
            }
            finally {
                locker.unlock();
            }
        }
    }

    @Override
    protected void onSuccess(AsyncResult<R> rAsyncResult) {
        if(!isCancelled()) {
            locker.lock(); //防止快速重复点击造成 ListView data 数据不一致
            try {
                getAsyncContext().onSuccess(rAsyncResult);
            }
            finally {
                locker.unlock();
            }
        }
    }

    @Override
    protected void onPreExecute() {
        if(!isCancelled()) {
            getAsyncContext().onProgressShow();
            super.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(AsyncResult<R> rAsyncResult) {
        if(!isCancelled()) {
            getAsyncContext().onProgressDismiss();
            super.onPostExecute(rAsyncResult);
        }
    }

    public boolean needAbort() {
        return isCancelled();
    }
}
