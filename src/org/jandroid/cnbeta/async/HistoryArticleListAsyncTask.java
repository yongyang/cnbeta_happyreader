package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.RealtimeArticleListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class HistoryArticleListAsyncTask extends ProgressDialogAsyncTask<List<RealtimeArticle>> {

    //TODO: use history

    @Override
    public AbstractLoader<List<RealtimeArticle>> getLoader() {
        return new RealtimeArticleListLoader();
    }

}
