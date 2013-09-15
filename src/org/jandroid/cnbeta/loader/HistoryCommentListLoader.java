package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.HistoryArticle;
import org.jandroid.cnbeta.entity.HistoryComment;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HistoryCommentListLoader extends AbstractLoader<List<HistoryComment>> {

    private final static Object LOCK = new Object();

    @Override
    public List<HistoryComment> httpLoad(File baseDir) throws Exception {
        throw new UnsupportedOperationException("load history article list from http.");
    }

    private List<HistoryComment> parseHistoryArticleListJSON(JSONArray articleListJSONArray){
        List<HistoryComment> commentList = new ArrayList<HistoryComment>(articleListJSONArray.size());
        for(int i=0; i<articleListJSONArray.size(); i++){
            JSONObject jsonObject = (JSONObject)articleListJSONArray.get(i);
            HistoryComment comment = new HistoryComment(jsonObject);
            commentList.add(comment);
        }
        return commentList;
    }

    @Override
    public List<HistoryComment> fromDisk(File baseDir) throws Exception {
        //read json file from SD Card
        JSONArray historyCommentJSONArray = readDiskJSONArray(baseDir);
        return parseHistoryArticleListJSON(historyCommentJSONArray);
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


    public void writeHistory(File baseDir, HistoryComment historyComment) throws Exception{
        synchronized (LOCK) {
            JSONArray historyArticlesJSONArray = readDiskJSONArray(baseDir);
            for(Iterator<JSONObject> it= historyArticlesJSONArray.iterator(); it.hasNext(); ){
                JSONObject article = it.next();
                if(article.get("sid").equals(historyComment.getSid())) {
                    it.remove();
                    break;
                }
            }
            historyArticlesJSONArray.add(historyArticleToJSONObject(historyComment));
            writeDisk(baseDir, historyArticlesJSONArray.toJSONString());
        }
    }

    private JSONObject historyArticleToJSONObject( HistoryComment historyComment) throws Exception {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("sid", historyComment.getSid());
        jSONObject.put("title", historyComment.getTitle());
        jSONObject.put("date", historyComment.getDate());
        jSONObject.put("comment", historyComment.getComment());
        return jSONObject;
    }

    @Override
    public String getFileName() {
        return "history_comment";
    }

    @Override
    protected List<HistoryComment> noCache() throws Exception {
        return Collections.emptyList();
    }

}
