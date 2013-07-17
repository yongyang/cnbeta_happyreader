package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Content;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentLoader extends LoaderTask<Content>  {


    @Override
    public Content fromHttp() {
        return null;
    }

    @Override
    public Content fromDisk() {
        return null;
    }

    @Override
    public void toDisk(Content content) {

    }
}
