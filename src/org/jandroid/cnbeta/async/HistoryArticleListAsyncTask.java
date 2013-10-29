package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.HistoryArticle;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.HistoryArticleListLoader;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class HistoryArticleListAsyncTask extends AbstractLoaderAsyncTask<List<HistoryArticle>> {

    @Override
    protected boolean isLocalLoadOnly() {
        return true;
    }

    @Override
    public AbstractLoader<List<HistoryArticle>> getLoader() {
        return new HistoryArticleListLoader();
    }

    @Override
    protected File getLocalCacheDir() {
        return getAsyncContext().getCnBetaApplicationContext().getHistoryDir();
    }
}
