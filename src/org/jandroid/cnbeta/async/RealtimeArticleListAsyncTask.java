package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.cnbeta.loader.RealtimeArticleListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class RealtimeArticleListAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {


    @Override
    protected AsyncResult doInBackground(Object... params) {
        try {
            List<RealtimeArticle> articles = loadRealtimeArticleList();
            return AsyncResult.successResult(articles);
        }
        catch (Exception e) {
            e.printStackTrace();
            return AsyncResult.errorResult(e.toString());
        }
    }
    
    protected  List<RealtimeArticle> loadRealtimeArticleList() throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        boolean hasSdCard = getCnBetaApplicationContext().isSdCardMounted();
        RealtimeArticleListLoader articleListLoader = new RealtimeArticleListLoader();
        if(hasNetwork) {
            List<RealtimeArticle> articles = articleListLoader.fromHttp();
            if(hasSdCard) {
                articleListLoader.toDisk(getCnBetaApplicationContext().getBaseDir(), articles);
            }
            return articles;
        }
        else {
            List<RealtimeArticle> articles = new RealtimeArticleListLoader().fromDisk(getCnBetaApplicationContext().getBaseDir());
            return articles;
        }

    }

}
