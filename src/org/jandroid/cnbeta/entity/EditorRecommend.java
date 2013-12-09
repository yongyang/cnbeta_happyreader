package org.jandroid.cnbeta.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class EditorRecommend extends BaseArticle {
/*
{"news_show": {
    "sid": "252369",
    "time": "2013-09-12 09:18:31",
    "title": "苹果新品提前入华：运营商各怀心思",
    "collectid": "0",
    "hometext": "一天前的苹果发布会引发了一场中国用户空前热烈的大吐槽。但即使被评论指陈为令人失望...",
    "counter": "4512",
    "score": "-24",
    "dig": "2",
    "comments": "23"
}, "type": "news", "id": "252369", "time": "2013-09-12 09:55:11"}
*/
    private String hometextShowShort;
    private String time;


    public EditorRecommend(Map<String, Object> jSONObject) {
        parse(jSONObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        Map<String, Object> newsJSNObject = (Map<String, Object>)jSONObject.get("news_show");
        this.setSid(Long.parseLong(newsJSNObject.get("sid").toString()));
        this.setTitle(newsJSNObject.get("title").toString());
        this.setTime(newsJSNObject.get("time").toString());
        this.setHometextShowShort(newsJSNObject.get("hometext").toString());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHometextShowShort() {
        return hometextShowShort;
    }

    public void setHometextShowShort(String hometextShowShort) {
        this.hometextShowShort = hometextShowShort;
    }
}
