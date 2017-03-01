package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.common.async.AsyncResult;

/**
 * TODO: 需要调用 AsyncTask 的 Activity 实现该接口
 *
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HasAsyncDelegate<R> implements HasAsync<R> {

    private HasAsync<R> delegate;

    public HasAsyncDelegate(HasAsync<R> delegate) {
        this.delegate = delegate;
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return delegate.getCnBetaApplicationContext();
    }

    public void onProgressShow() {
        delegate.onProgressShow();
    }

    public void onProgressDismiss() {
        delegate.onProgressDismiss();
    }

    public void onSuccess(AsyncResult<R> rAsyncResult) {
        delegate.onSuccess(rAsyncResult);
    }

    public void onFailure(AsyncResult<R> rAsyncResult) {
        delegate.onFailure(rAsyncResult);
    }

}
