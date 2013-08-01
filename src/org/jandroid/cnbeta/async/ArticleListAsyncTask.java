package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.CnBetaApplication;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.cnbeta.CnBetaApplication;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleListAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {

    protected abstract ArticleListLoader.Type getCategory();

    protected abstract int getPage();

    @Override
    protected AsyncResult doInBackground(Object... params) {
        try {
            List<Article> articles = loadArticleList();
            return AsyncResult.successResult(articles);
        }
        catch (Exception e) {
            e.printStackTrace();
            return AsyncResult.errorResult(e.toString());
        }
    }
    
    protected List<Article> loadArticleList() throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        boolean hasSdCard = getCnBetaApplicationContext().isSdCardMounted();
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

    }

}
