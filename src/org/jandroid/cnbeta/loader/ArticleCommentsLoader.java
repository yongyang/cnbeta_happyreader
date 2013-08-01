package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Comment;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
class ArticleCommentsLoader extends AbstractLoader<Comment> {


    @Override
    public Comment fromHttp() throws Exception {
        return null;
    }

    @Override
    public Comment fromDisk(File baseDir) throws Exception {
        return null;
    }

    @Override
    public void toDisk(File baseDir, Comment comment) throws Exception {

    }
}
