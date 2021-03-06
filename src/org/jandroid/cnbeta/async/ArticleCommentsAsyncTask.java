package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ArticleCommentsLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleCommentsAsyncTask extends AbstractLoaderAsyncTask<List<Comment>> {

    protected abstract Content getArticleContent();

    @Override
    public AbstractLoader<List<Comment>> getLoader() {
        return new ArticleCommentsLoader(getArticleContent());
    }
}
