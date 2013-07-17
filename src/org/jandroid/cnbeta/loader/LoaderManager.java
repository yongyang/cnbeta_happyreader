package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.entity.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class LoaderManager {

    //TODO: 检查某个load任务是否已经在执行，如果在执行，则需等待以避免重复执行
    private List<LoaderTask> runningTasks = new ArrayList<LoaderTask>();

    public List<Article> loadArticleList(int page) {
        //TODO:
        /*
         * 1. 判断是否有网络连接
         * 2. 没有网络，则直接从磁盘 load
         * 3. 有网络，http load
         * 3.1 parse，然后写入磁盘
         * 4. 返回结果
         */
        return null;
    }

    public Article loadArticle(String id) {
        return null;
    }

    public List<Comment> loadComments(String articleId) {
        return null;
    }

}
