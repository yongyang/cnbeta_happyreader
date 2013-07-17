package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsLoader extends LoaderTask<Comment>  {


    @Override
    public Comment fromHttp() {
        return null;
    }

    @Override
    public Comment fromDisk() {
        return null;
    }

    @Override
    public void toDisk(Comment comment) {

    }
}
