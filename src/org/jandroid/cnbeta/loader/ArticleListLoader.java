package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Article;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
class ArticleListLoader extends LoaderTask<List<Article>> {

    public static String URL_FORMAT = "http://www.cnbeta.com/more.htm?jsoncallback=jQuery_{0}&type={1}&_={2}";
    private Type type;
    private int page;

    public static enum Type {
        ALL("all"),
        REALTIME("realtime"),
        DIG("dig"),
        SOFT("soft"),
        INDUSTRY("industry"),
        INTERACT("interact");
        private String type;

        private Type(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }
    }

    public ArticleListLoader(Type type, int page) {
        this.type = type;
        this.page = page;
    }

    public Type getType() {
        return type;
    }

    public int getPage() {
        return page;
    }

    @Override
    public List<Article> fromHttp() throws Exception {
        String url = String.format(URL_FORMAT, System.currentTimeMillis(), getType().getTypeString(), System.currentTimeMillis()+1);
        //TODO: user json-simple to parse returned json string
        String articleListJSON = CnBetaHttpClient.getInstance().httpGet(url);
        JSONArray jsonArray = (JSONArray)JSONValue.parse(articleListJSON);

        List<Article> articleList = new ArrayList<Article>(jsonArray.size());
        for(int i=0; i<jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject)jsonArray.get(i);
            Article article = new Article();
            article.bReaded = (Boolean)jsonObject.get("bReaded");
        }

        return null;
    }

    @Override
    public List<Article> fromDisk() throws Exception {
        return null;
    }

    @Override
    public void toDisk(List<Article> articles) throws Exception {

    }
}
/*
http://www.cnbeta.com/more.htm?jsoncallback=jQuery18007005875239260606_1373612093876&type=realtime&sid=244461&_=1373612267852

http://www.cnbeta.com/more.htm?jsoncallback=jQuery180028785929968214596_1373611841741&type=all&page=1&_=1373611882757
*/