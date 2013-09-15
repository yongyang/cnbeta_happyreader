package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.RealtimeArticleListLoader;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class RealtimeArticleListAsyncTask extends AbstractLoaderAsyncTask<List<RealtimeArticle>> {

    @Override
    public AbstractLoader<List<RealtimeArticle>> getLoader() {
        return new RealtimeArticleListLoader();
    }
}
