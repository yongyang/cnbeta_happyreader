package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.SupportCommentPoster;
import org.json.simple.JSONObject;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class SupportCommentAsyncTask extends AbstractLoaderAsyncTask<JSONObject> {

    protected abstract Comment getComment();
    
    protected abstract SupportCommentPoster.Op getOp();

    protected abstract String getToken();

    @Override
    protected boolean isRemoteLoadOnly() {
        return true;
    }

    @Override
    public AbstractLoader<JSONObject> getLoader() {
        return new SupportCommentPoster(getComment(), getOp(), getToken());
    }
}
