package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Topic;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.TopicListLoader;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
    public abstract class TopicListAsyncTask extends AbstractLoaderAsyncTask<List<Topic>> {

    protected abstract int getPage();

    @Override
    protected boolean isLocalLoadFirst() {
        return true;
    }

    @Override
    public AbstractLoader<List<Topic>> getLoader() {
        return new TopicListLoader(getPage());
    }
}
