package org.jandroid.cnbeta.loader;

import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.entity.HistoryArticle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HistoryArticleListLoader extends AbstractLoader<List<HistoryArticle>> {

    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<HistoryArticle> fromHttp(File baseDir) throws Exception {
        throw new UnsupportedOperationException("load history article list from http.");
    }

    private List<HistoryArticle> parseArticleListJSON(JSONArray articleListJSONArray){
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
            return parseArticleListJSON(loadFromDisk(baseDir));
    }

    private JSONArray loadFromDisk(File baseDir) throws Exception {
        String articleListJSONString = FileUtils.readFileToString(getCacheFile(baseDir));
        return (JSONArray)JSONValue.parse(articleListJSONString);
    }

    /**
     * write a new historyArticle to disk
     * @param baseDir
     * @param historyArticle
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected void toDisk(File baseDir, HistoryArticle historyArticle) throws Exception {
        File file = getCacheFile(baseDir);
        if(!file.exists()) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(historyArticle.toJSONObject());
            FileUtils.write(file, jsonArray.toJSONString());
        }
        else {
            JSONArray jsonArray = loadFromDisk(baseDir);
            if(jsonArray.contains(historyArticle)) {
                // add to top
                jsonArray.add(0, historyArticle.toJSONObject());
                FileUtils.write(file, jsonArray.toJSONString());
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void toDisk(File baseDir, Object obj) throws Exception {
/*
        if(articles.size() == 1) {
            toDisk(baseDir, articles.get(0));
        }
        else {
            throw new UnsupportedOperationException("One article one time, use toDisk(File baseDir, HistoryArticle historyArticle) instead");
        }
*/
    }
    
    public File getCacheFile(File dir){
        String date = dateFormat.format(new Date());
        return new File(dir , "history_" + date);
    }
}
