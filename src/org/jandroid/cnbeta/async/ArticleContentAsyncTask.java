package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.ArticleListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleContentAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {

    protected abstract long getSid();

    @Override
    protected AsyncResult doInBackground(Object... params) {
        try {
            Content content = loadContent();
            return AsyncResult.successResult(content);
        }
        catch (Exception e) {
            e.printStackTrace();
            return AsyncResult.errorResult(e.toString());
        }
    }
    
    protected Content loadContent() throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        boolean hasSdCard = getCnBetaApplicationContext().isSdCardMounted();
/*
        ArticleListLoader articleListLoader = new ArticleListLoader(getCategory(), getPage());
        if(hasNetwork) {
            List<Article> articles = articleListLoader.fromHttp();
            if(hasSdCard) {
                articleListLoader.toDisk(getCnBetaApplicationContext().getBaseDir(), articles);
            }
            return articles;
        }
        else {
            List<Article> articles = new ArticleListLoader(getCategory(), getPage()).fromDisk(getCnBetaApplicationContext().getBaseDir());
            return articles;
        }
*/
        return null;

    }

}
