package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.RankArticle;
import org.jandroid.cnbeta.loader.Top10Loader;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class RankArticleListAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {


    @Override
    protected AsyncResult doInBackground(Object... params) {
        try {
            Map<String, List<RankArticle>> articles = loadRankArticleList();
            return AsyncResult.successResult(articles);
        }
        catch (Exception e) {
            e.printStackTrace();
            return AsyncResult.errorResult(e.toString());
        }
    }
    
    protected  Map<String, List<RankArticle>>  loadRankArticleList() throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();
        Top10Loader articleListLoader = new Top10Loader();
        if(hasNetwork) {
            Map<String, List<RankArticle>> articlesMap = articleListLoader.fromHttp(getCnBetaApplicationContext().getBaseDir());
            return articlesMap;
        }
        else {
            Map<String, List<RankArticle>> articlesMap = articleListLoader.fromDisk(getCnBetaApplicationContext().getBaseDir());
            return articlesMap;
        }

    }

}
