package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.HistoryArticle;
import org.jandroid.cnbeta.entity.HistoryComment;
import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.HistoryArticleListLoader;
import org.jandroid.cnbeta.loader.RealtimeArticleListLoader;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class HistoryArticleListAsyncTask extends LoadingAsyncTask<List<HistoryArticle>> {

    @Override
    protected boolean isLocalLoadOnly() {
        return true;
    }

    @Override
    protected List<HistoryArticle> defaultResult() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public AbstractLoader<List<HistoryArticle>> getLoader() {
        return new HistoryArticleListLoader();
    }

}
