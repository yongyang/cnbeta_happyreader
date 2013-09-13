package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.MRankArticle;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.MRankListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class MRankArticleListAsyncTask extends AbstractLoaderAsyncTask<List<MRankArticle>> {

    protected abstract MRankListLoader.Type getType();

    @Override
    public AbstractLoader<List<MRankArticle>> getLoader() {
        return new MRankListLoader(getType());
    }
}
