package org.jandroid.cnbeta.async;

import android.app.Application;
import org.jandroid.cnbeta.exception.InfoException;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.common.Logger;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;
import org.jandroid.common.async.BaseAsyncTask;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/30/13 4:15 PM
 */
public abstract class LoadingAsyncTask<R> extends BaseAsyncTask<R> {

    private final ReentrantLock locker = new ReentrantLock();

    protected Logger logger = Logger.getLogger(this.getClass());

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

    protected R run() throws Exception {
        boolean hasNetwork = getAsyncContext().getCnBetaApplicationContext().isNetworkConnected();

        if (!hasNetwork && isRemoteLoadOnly()) {
            throw new Exception("No network!");
        }

        AbstractLoader loader = getLoader();
        //优先从Disk装载
        if (isLocalLoadFirst() || isLocalLoadOnly()) {
            if (loader.isCached(getAsyncContext().getCnBetaApplicationContext().getBaseDir())) {
                return (R) loader.fromDisk(getAsyncContext().getCnBetaApplicationContext().getBaseDir());
            }
        }
        if (!isLocalLoadOnly()) {
            return (R) loader.fromHttp(getAsyncContext().getCnBetaApplicationContext().getBaseDir());
        }
        return defaultResult();
    }

    protected R defaultResult() {
        return null;
    }

    @Override
    protected void onFailure(AsyncResult<R> asyncResult) {
        logger.w(asyncResult.getErrorMsg(), asyncResult.getException());
        if(!isCancelled()) {
            // 只有处理底层错误类异常, 所有信息提示类异常继承自 InfoException
            if((asyncResult.getException() != null) && !(asyncResult.getException() instanceof InfoException)) {
                ToastUtils.showShortToast((Application) getAsyncContext().getCnBetaApplicationContext(), asyncResult.getException().getMessage());
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

}
