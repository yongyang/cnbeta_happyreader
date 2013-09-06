package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.TopicArticle;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.TopicArticleListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
    public abstract class TopicArticleListAsyncTask extends LoadingAsyncTask<List<TopicArticle>> {

    protected abstract long getId();

    protected abstract int getPage();

    protected abstract int getSplitPage();

    @Override
    public AbstractLoader<List<TopicArticle>> getLoader() {
        return new TopicArticleListLoader(getId(), getPage(), getSplitPage());
    }
}
