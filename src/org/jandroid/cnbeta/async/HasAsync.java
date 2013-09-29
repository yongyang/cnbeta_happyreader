package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.common.async.AsyncResult;

/**
 * 需要调用 AsyncTask 的 Activity 实现该接口
 *
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface HasAsync<R> {

    CnBetaApplicationContext getCnBetaApplicationContext();

    void onProgressShow();

    void onProgressDismiss();

    void onSuccess(AsyncResult<R> rAsyncResult);

    void onFailure(AsyncResult<R> rAsyncResult);

}
