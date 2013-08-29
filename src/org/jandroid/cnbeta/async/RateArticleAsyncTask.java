package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.RateArticlePoster;
import org.jandroid.cnbeta.loader.SupportCommentPoster;
import org.json.simple.JSONObject;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class RateArticleAsyncTask extends ProgressDialogAsyncTask<JSONObject> {

    protected abstract Content getContent();
    
    protected abstract int getScore();
    
    @Override
    protected boolean isRemoteLoadOnly() {
        return true;
    }

    @Override
    public AbstractLoader<JSONObject> getLoader() {
        return new RateArticlePoster(getContent(), getScore());
    }
}
