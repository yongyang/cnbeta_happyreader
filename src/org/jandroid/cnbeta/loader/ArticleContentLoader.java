package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Content;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentLoader extends LoaderTask<Content>  {


    @Override
    public Content fromHttp() throws Exception {
        return null;
    }

    @Override
    public Content fromDisk() throws Exception {
        return null;
    }

    @Override
    public void toDisk(Content content) throws Exception {

    }
}
