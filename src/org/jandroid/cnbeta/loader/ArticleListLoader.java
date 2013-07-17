package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.LoaderTask;

import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleListLoader extends LoaderTask<List<Article>> {

    private String category;
    private int page;

    public ArticleListLoader(String category, int page) {
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
    public List<Article> fromHttp() {
        //TODO: user json-simple to parse returned json string
        return null;
    }

    @Override
    public List<Article> fromDisk() {
        return null;
    }

    @Override
    public void toDisk(List<Article> articles) {

    }
}
/*
http://www.cnbeta.com/more.htm?jsoncallback=jQuery18007005875239260606_1373612093876&type=realtime&sid=244461&_=1373612267852

http://www.cnbeta.com/more.htm?jsoncallback=jQuery180028785929968214596_1373611841741&type=all&page=1&_=1373611882757
*/