package org.jandroid.cnbeta.async;

import android.accounts.NetworkErrorException;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.ArticleCommentsLoader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleCommentsAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {

    protected abstract Content getArticleContent();

    @Override
    protected AsyncResult doInBackground(Object... params) {
        try {
            Content content = loadArticleContent();
            return AsyncResult.successResult(content);
        }
        catch (Exception e) {
            e.printStackTrace();
            return AsyncResult.errorResult(e.toString());
        }
    }
    
    protected Content loadArticleContent() throws Exception {
        boolean hasNetwork = getCnBetaApplicationContext().isNetworkConnected();

        ArticleCommentsLoader commentsLoader = new ArticleCommentsLoader(getArticleContent());
        
        // 优先从磁盘加载
        if(commentsLoader.isCached(getCnBetaApplicationContext().getBaseDir())){
            Content content = commentsLoader.fromDisk(getCnBetaApplicationContext().getBaseDir());
            return content;
        }
        else {
            if(hasNetwork) {
                Content content = commentsLoader.fromHttp();
                commentsLoader.toDisk(getCnBetaApplicationContext().getBaseDir(), content);
                return content;
            }
            throw new NetworkErrorException("no network");
        }
    }

}
