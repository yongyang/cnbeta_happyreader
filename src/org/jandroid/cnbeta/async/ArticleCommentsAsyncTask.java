package org.jandroid.cnbeta.async;

import android.accounts.NetworkErrorException;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ArticleCommentsLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleCommentsAsyncTask extends ProgressDialogAsyncTask<List<Comment>> {

    protected abstract Content getArticleContent();

    @Override
    public AbstractLoader<List<Comment>> getLoader() {
        return new ArticleCommentsLoader(getArticleContent());
    }
}
