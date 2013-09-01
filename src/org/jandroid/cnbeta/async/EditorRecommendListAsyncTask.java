package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.EditorRecommend;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.EditorRecommendListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class EditorRecommendListAsyncTask extends LoadingAsyncTask<List<EditorRecommend>> {

    protected abstract int getPage();

    @Override
    public AbstractLoader<List<EditorRecommend>> getLoader() {
        return new EditorRecommendListLoader(getPage());
    }

}
