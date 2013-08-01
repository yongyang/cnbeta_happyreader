package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Content;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentLoader extends AbstractLoader<Content> {


    @Override
    public Content fromHttp() throws Exception {
        return null;
    }

    @Override
    public Content fromDisk(File baseDir) throws Exception {
        return null;
    }

    @Override
    public void toDisk(File baseDir, Content content) throws Exception {

    }
}
