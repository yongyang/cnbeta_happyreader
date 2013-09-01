package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.RankArticle;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.Top10Loader;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class RankArticleListAsyncTask extends LoadingAsyncTask<Map<String, List<RankArticle>>> {

    @Override
    public AbstractLoader<Map<String, List<RankArticle>>> getLoader() {
        return new Top10Loader();
    }

}
