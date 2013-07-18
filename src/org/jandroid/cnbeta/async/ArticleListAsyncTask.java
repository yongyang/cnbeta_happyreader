package org.jandroid.cnbeta.async;

import android.os.AsyncTask;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.LoaderManager;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class ArticleListAsyncTask extends ProgressDialogAsyncTask<Void, Integer, AsyncResult<List<Article>>> {

    private String category;
    private int page;

    protected ArticleListAsyncTask(String category, int page) {
        this.category = category;
        this.page = page;
    }

    public String getCategory() {
        return category;
    }

    public int getPage() {
        return page;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected AsyncResult<List<Article>> doInBackground(Void... params) {
        List<Article> articles = LoaderManager.getInstance().loadArticleList(getCategory(),getPage());
        return AsyncResult.successResult(articles);
    }

}
