package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.ArticleListLoader;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleListAsyncTask extends ProgressDialogAsyncTask<Object, Integer, AsyncResult> {

    private ArticleListLoader.Type category;
    private int page;

    protected ArticleListAsyncTask(ArticleListLoader.Type category, int page) {
        this.category = category;
        this.page = page;
    }

    public ArticleListLoader.Type getCategory() {
        return category;
    }

    public int getPage() {
        return page;
    }

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
    
    protected abstract List<Article> loadArticleList() throws Exception ;

}
