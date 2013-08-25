package org.jandroid.cnbeta.async;

import android.accounts.NetworkErrorException;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ArticleCommentsLoader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleCommentsAsyncTask extends ProgressDialogAsyncTask<Content> {

    protected abstract Content getArticleContent();

    @Override
    public AbstractLoader<Content> getLoader() {
        return new ArticleCommentsLoader(getArticleContent());
    }
}
