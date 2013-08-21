package org.jandroid.cnbeta.async;

import android.accounts.NetworkErrorException;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.loader.ArticleContentLoader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleContentAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {

    protected abstract Article getArticle();

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
        
        ArticleContentLoader contentLoader = new ArticleContentLoader(getArticle());
        
        // 优先从磁盘加载
        if(contentLoader.isCached(getCnBetaApplicationContext().getBaseDir())){
            Content content = contentLoader.fromDisk(getCnBetaApplicationContext().getBaseDir());
            return content;
        }
        else {
            if(hasNetwork) {
                Content content = contentLoader.fromHttp();
                contentLoader.toDisk(getCnBetaApplicationContext().getBaseDir(), content);
                return content;
            }
            throw new NetworkErrorException("no network");
        }
    }

}
