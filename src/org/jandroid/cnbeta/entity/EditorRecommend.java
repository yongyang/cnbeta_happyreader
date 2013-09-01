package org.jandroid.cnbeta.entity;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class EditorRecommend implements Serializable {
/*
    {"title":"\u5e7f\u4e1c\u4f5b\u5c71\u653610\u5143\u4ee3\u4eba\u8d2d\u7968\u592b\u59bb\u4eca\u65e5\u88ab\u91ca\u653e",
     "sid":"223581",
     "url_show":"\/articles\/223581.htm",
     "hometext_show_short":"\u5728\u7126\u6025\u7b49\u5f85\u534a\u5e74\u4e4b\u540e\uff0c\u66fe\u7ecf\u8f70\u52a8\u5168\u56fd\u7684\u201c\u5e7f\u4e1c\u6700\u5927\u5012\u5356\u706b\u8f66\u7968\u9ed1\u7a9d\u70b9\u201d\u4f5b\u5c71\u5c0f\u592b\u59bb\u4e00\u6848\u76ee\u524d\u7ec8\u4e8e\u6709\u4e86\u7ed3\u679c\u3002\u8b66\u65b9\u51b3\u5b9a\u5bf9\u4e8c\u4eba\u5904\u4ee5\u884c\u653f\u62d8\u755912\u5929\u7684\u5904\u7f5a\uff0c\u5e76..."}
*/
    private long sid;
    private String title;
    private String hometextShowShort;
    private String urlShow;


    public EditorRecommend(Map<String, Object> jSONObject) {
        parse(jSONObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        this.setSid(Long.parseLong(jSONObject.get("sid").toString()));
        this.setTitle(jSONObject.get("title").toString());
        this.setUrlShow(jSONObject.get("url_show").toString());
        this.setHometextShowShort(jSONObject.get("hometext_show_short").toString());
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlShow() {
        return urlShow;
    }

    public void setUrlShow(String urlShow) {
        this.urlShow = urlShow;
    }

    public String getHometextShowShort() {
        return hometextShowShort;
    }

    public void setHometextShowShort(String hometextShowShort) {
        this.hometextShowShort = hometextShowShort;
    }
}
