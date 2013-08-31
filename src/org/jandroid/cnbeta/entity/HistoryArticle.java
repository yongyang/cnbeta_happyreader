package org.jandroid.cnbeta.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HistoryArticle implements Serializable {

    private long sid;
    private String title;
    private String hometext;
    private String hometextShowShort2;
    private String urlShow;
    private String time;
    private String timeShow;

    public HistoryArticle(Map<String, Object> jSONObject) {
        parse(jSONObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        this.setSid(Long.parseLong(jSONObject.get("sid").toString()));
        this.setTitle(jSONObject.get("title").toString());
        this.setUrlShow(jSONObject.get("url_show").toString());
        this.setHometext(jSONObject.get("hometext").toString());
        this.setHometextShowShort2(jSONObject.get("hometext_show_short2").toString());
        this.setTime(jSONObject.get("time").toString());
        this.setTimeShow(jSONObject.get("time_show").toString());
    }


    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }


    public String getUrlShow() {
        return urlShow;
    }

    public void setUrlShow(String urlShow) {
        this.urlShow = urlShow;
    }

    public String getHometext() {
        return hometext;
    }

    public void setHometext(String hometext) {
        this.hometext = hometext;
    }

    public String getHometextShowShort2() {
        return hometextShowShort2;
    }

    public void setHometextShowShort2(String hometextShowShort2) {
        this.hometextShowShort2 = hometextShowShort2;
    }

    public String getTimeShow() {
        return timeShow;
    }

    public void setTimeShow(String timeShow) {
        this.timeShow = timeShow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RealtimeArticle{" +
                "sid=" + sid +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryArticle that = (HistoryArticle) o;

        if (sid != that.sid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (sid ^ (sid >>> 32));
    }
}

/*

{"status": "success", "result": [
    {
        "title": "微软乌龙：将自家网站内容误当侵权内容 请求谷歌删除",
        "hometext": "<p>本周一，微软委托一家负责帮助审查网上盗版内容的外包公司LeakID，向搜索巨头谷歌发出了一个奇怪的侵权查处请求，<strong>请求中表示，微软自己的Microsoft.com上面有6个特定网址，侵犯了微软自家产品的版权，请求谷歌移除搜索结果中的这些链接<\/strong>。微软后来证实这是该外包公司的工作失误。<\/p>",
        "sid": "246775",
        "time": "2013-07-31 23:24:44",
        "time_show": "1小时前",
        "hometext_show_short2": "本周一，微软委托一家负责帮助审查网上盗版内容的外包公司LeakID，向搜索巨...",
        "url_show": "\/articles\/246775.htm"
    }
]}

*/