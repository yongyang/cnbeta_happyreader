package org.jandroid.cnbeta.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Comment implements Serializable{

    //thread-id, comment id
    private long tid;
    // parent-id
    private long pid;
    // article id
    private long sid;
    private String date;
    // user-name
    private String name;
    // host-name
    private String hostName;
    // comment content
    private String comment;
    // support
    private int score;
    //against
    private int reason;
    
    private String token;

    public Comment() {
    }

    public Comment(Map<String, Object> jSonObject) {
        parse(jSonObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        this.setTid(Long.parseLong(jSONObject.get("tid").toString()));
        this.setPid(Long.parseLong(jSONObject.get("pid").toString()));
        this.setSid(Long.parseLong(jSONObject.get("sid").toString()));
        this.setDate(jSONObject.get("date").toString());
        this.setName(jSONObject.get("name").toString());
        this.setHostName(jSONObject.get("host_name").toString());
        this.setComment(jSONObject.get("comment").toString());
        this.setScore(Integer.parseInt(jSONObject.get("score").toString()));
        this.setReason(Integer.parseInt(jSONObject.get("reason").toString()));
        this.setToken(jSONObject.get("token").toString());
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
