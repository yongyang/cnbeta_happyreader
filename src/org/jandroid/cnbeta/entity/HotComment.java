package org.jandroid.cnbeta.entity;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HotComment implements Serializable {

    private long sid;
    private String title;
    private String hometextShowShort;
    private String urlShow;


    public HotComment(Map<String, Object> jSONObject) {
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