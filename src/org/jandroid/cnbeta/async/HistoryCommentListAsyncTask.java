package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.HistoryComment;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.HistoryCommentListLoader;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class HistoryCommentListAsyncTask extends AbstractLoaderAsyncTask<List<HistoryComment>> {

    @Override
    protected boolean isLocalLoadOnly() {
        return true;
    }

    @Override
    public AbstractLoader<List<HistoryComment>> getLoader() {
        return new HistoryCommentListLoader();
    }

    @Override
    protected File getLocalCacheDir() {
        return getAsyncContext().getCnBetaApplicationContext().getHistoryDir();
    }

}
