package org.jandroid.cnbeta.async;

import android.os.AsyncTask;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.ArticleListLoader;
import org.jandroid.cnbeta.loader.LoaderManager;

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
            List<Article> articles = LoaderManager.getInstance().loadArticleList(getCategory(),getPage());
            return AsyncResult.successResult(articles);
        }
        catch (Exception e) {
            e.printStackTrace();
            return AsyncResult.errorResult(e.toString());
        }
    }

}
