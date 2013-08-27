package org.jandroid.cnbeta.entity;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Comment {
    
    private long tid;
    private long pid;
    private long sid;
    private String date;
    private String name;
    private String hostName;
    private int score;
    private int reason;
    
    private String token;


    public Comment(Map<String, Object> jSonObject) {
        parse(jSonObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        this.setSid(Long.parseLong(jSONObject.get("sid").toString()));
        this.setTitleShow(jSONObject.get("title_show").toString());
        this.setUrlShow(jSONObject.get("url_show").toString());
        this.setHometextShowShort(jSONObject.get("hometext_show_short").toString());
        this.setLogo(jSONObject.get("logo").toString());
        this.setComments(Integer.parseInt(jSONObject.get("comments").toString()));
        this.setCounter(Integer.parseInt(jSONObject.get("counter").toString()));
        this.setTime(jSONObject.get("time").toString());
    }


    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
