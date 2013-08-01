package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.util.EnvironmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class LoaderManager {
    //TODO: 不需要使用线程池，因为 aysnTask 和 httpClient 都已经有线程池

    //TODO: 检查某个load任务是否已经在执行，如果在执行，则需等待以避免重复执行
    private List<AbstractLoader> runningTasks = new ArrayList<AbstractLoader>();

    private static final LoaderManager INSTANCE = new LoaderManager();

    private LoaderManager() {
    }

    public static LoaderManager getInstance() {
        return INSTANCE;
    }

    public List<Article> loadArticleList(ArticleListLoader.Type category, int page, boolean online) throws Exception {
        List<Article> articles;
        ArticleListLoader loader = new ArticleListLoader(category, page);
        if(online) {
            articles = loader.fromHttp();
            // write to disk
            loader.toDisk(articles);
        }
        else {
            articles = loader.fromDisk();
        }
        return articles;    
    }
    
    public List<Article> loadArticleList(ArticleListLoader.Type category, int page) throws Exception {        
        //TODO:
        /*
         * 1. 判断是否有网络连接
         * 2. 没有网络，则直接从磁盘 load
         * 3. 有网络，http load
         * 3.1 parse，然后写入磁盘
         * 4. 返回结果
         */
        //TODO:
        List<Article> articles;
        ArticleListLoader loader = new ArticleListLoader(category, page);
        if(EnvironmentUtils.hasNetworkConnection()) {
            articles = loader.fromHttp();
            // write to disk
            loader.toDisk(articles);
        }
        else {
            articles = loader.fromDisk();
        }
        return articles;

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
