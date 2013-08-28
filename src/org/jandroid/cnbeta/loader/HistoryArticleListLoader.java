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
        String articleListJSONString = FileUtils.readFileToString(getFile(baseDir));
        return (JSONArray)JSONValue.parse(articleListJSONString);
    }

    @Override
    public String getFileName() {
        String date = dateFormat.format(new Date());
        return "history_" + date;
    }

}
