package org.jandroid.cnbeta.async;

import android.accounts.NetworkErrorException;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ArticleContentLoader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleContentAsyncTask extends ProgressDialogAsyncTask<Content> {

    protected abstract long getSid();

    @Override
    protected boolean isLocalLoadFirst() {
        return true;
    }

    @Override
    public AbstractLoader<Content> getLoader() {
        return new ArticleContentLoader(getSid());
    }


}
