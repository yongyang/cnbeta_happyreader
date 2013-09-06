package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.HistoryArticle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HistoryArticleListLoader extends AbstractLoader<List<HistoryArticle>> {

    private final static Object LOCK = new Object();

    @Override
    public List<HistoryArticle> fromHttp(File baseDir) throws Exception {
        throw new UnsupportedOperationException("load history article list from http.");
    }

    private List<HistoryArticle> parseHistoryArticleListJSON(JSONArray articleListJSONArray){
        List<HistoryArticle> articleList = new ArrayList<HistoryArticle>(articleListJSONArray.size());
        for(int i=0; i<articleListJSONArray.size(); i++){
            JSONObject jsonObject = (JSONObject)articleListJSONArray.get(i);
            HistoryArticle article = new HistoryArticle(jsonObject);
            articleList.add(article);
        }
        return articleList;
    }

    @Override
    public List<HistoryArticle> fromDisk(File baseDir) throws Exception {
        //read json file from SD Card
        JSONArray historyArticlesJSONArray = readDiskJSONArray(baseDir);
        return parseHistoryArticleListJSON(historyArticlesJSONArray);
    }

    protected JSONArray readDiskJSONArray(File baseDir) throws Exception {
        // 创建文件
        if(!isCached(baseDir)) {
            writeDisk(baseDir, "[]");
        }
        //read json file from SD Card
        String jSONObjectString = readDisk(baseDir);
        JSONArray historyArticlesJSONArray = (JSONArray)JSONValue.parse(jSONObjectString);
        return historyArticlesJSONArray;
    }


    public void writeHistory(File baseDir, HistoryArticle historyArticle) throws Exception{
        synchronized (LOCK) {
            JSONArray historyArticlesJSONArray = readDiskJSONArray(baseDir);
            for(Iterator<JSONObject> it= historyArticlesJSONArray.iterator(); it.hasNext(); ){
                JSONObject article = it.next();
                if(article.get("sid").equals(historyArticle.getSid())) {
                    it.remove();
                    break;
                }
            }
            historyArticlesJSONArray.add(historyArticleToJSONObject(historyArticle));
            writeDisk(baseDir, historyArticlesJSONArray.toJSONString());
        }
    }

    private JSONObject historyArticleToJSONObject( HistoryArticle historyArticle) throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("sid", historyArticle.getSid());
        jSONObject.put("title", historyArticle.getTitle());
        jSONObject.put("date", historyArticle.getDate());
        return jSONObject;
    }

    @Override
    public String getFileName() {
        return "history_article";
    }

}
