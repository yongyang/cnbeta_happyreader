package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.PublishCommentPoster;
import org.jandroid.cnbeta.loader.RateArticlePoster;
import org.json.simple.JSONObject;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class PublishCommentAsyncTask extends ProgressDialogAsyncTask<JSONObject> {

    protected abstract Content getContent();

    protected abstract String getCommentContent();

    //如果是回复，则需填入 Pid
    protected long getPid() {
        return 0;
    }

    protected abstract String getSeccode();
    
    @Override
    protected boolean isRemoteLoadOnly() {
        return true;
    }

    @Override
    public AbstractLoader<JSONObject> getLoader() {
        return new PublishCommentPoster(getContent(), getCommentContent(), getPid(), getSeccode());
    }
}
