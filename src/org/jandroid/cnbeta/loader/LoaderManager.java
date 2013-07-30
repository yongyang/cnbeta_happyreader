package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class LoaderManager {
    //TODO: 不需要使用线程池，因为 aysnTask 和 httpClient 都已经有线程池

    //TODO: 检查某个load任务是否已经在执行，如果在执行，则需等待以避免重复执行
    private List<LoaderTask> runningTasks = new ArrayList<LoaderTask>();

    private static final LoaderManager INSTANCE = new LoaderManager();

    private LoaderManager() {
    }

    public static LoaderManager getInstance() {
        return INSTANCE;
    }

    public List<Article> loadArticleList(String category, int page) throws Exception {
        //TODO:
        /*
         * 1. 判断是否有网络连接
         * 2. 没有网络，则直接从磁盘 load
         * 3. 有网络，http load
         * 3.1 parse，然后写入磁盘
         * 4. 返回结果
         */
        //TODO:
        return new ArticleListLoader(ArticleListLoader.Type.ALL, page).fromHttp();
    }

    public Article loadArticleContent(String id) {
        return null;
    }

    public List<Comment> loadArticleComments(String articleId) {
        return null;
    }
    
    public Bitmap loadImage(String url) throws Exception{
        return new ImageLoader(url).fromHttp();
    }
}
