package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ArticleListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
    public abstract class ArticleListAsyncTask extends AbstractLoaderAsyncTask<List<Article>> {

    protected abstract ArticleListLoader.Type getCategory();

    protected abstract int getPage();

    @Override
    public AbstractLoader<List<Article>> getLoader() {
        return new ArticleListLoader(getCategory(), getPage());
    }

}
