package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.RealtimeArticle;
import org.jandroid.cnbeta.loader.RealtimeArticleListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class HistoryArticleListAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {


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
        RealtimeArticleListLoader articleListLoader = new RealtimeArticleListLoader();
        if(hasNetwork) {
            List<RealtimeArticle> articles = articleListLoader.fromHttp(getCnBetaApplicationContext().getBaseDir());
            return articles;
        }
        else {
            List<RealtimeArticle> articles = new RealtimeArticleListLoader().fromDisk(getCnBetaApplicationContext().getBaseDir());
            return articles;
        }

    }

}
