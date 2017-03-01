package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.HotComment;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.HotCommentListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class HotCommentListAsyncTask extends AbstractLoaderAsyncTask<List<HotComment>> {

    protected abstract int getPage();

    @Override
    public AbstractLoader<List<HotComment>> getLoader() {
        return new HotCommentListLoader(getPage());
    }

}
