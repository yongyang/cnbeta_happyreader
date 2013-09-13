package org.jandroid.cnbeta.entity;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HotComment implements Serializable {
/*
{"type": "comment", "id": "7839426", "time": "2013-09-13 08:03:21", "comment_show": {
    "sid": "252390",
    "host_name_show": "上海市电信",
    "allow": "0",
    "reason": "30",
    "arguetype": "0",
    "subject": "Re:指纹识别器那些你不知道的事",
    "userid": "0",
    "score": "202",
    "pid": "0",
    "host_name": "101.86.173.94",
    "date": "2013-09-12 11:09:34",
    "article_url": "\/articles\/252390.htm",
    "url": null,
    "last_moderation_ip": "0",
    "email": null,
    "name": "",
    "report": "1",
    "title_show": "指纹识别器那些你不知道的事",
    "tid": "7839426",
    "comment": "小孩趁你睡着把手机在你手上放一下就能付费成功。老婆趁你睡着把手机在你手上放一下就能看到你全部短信。 在这么继续发展下去：  \n强盗把你绑起来，在你手上放一下就能让你银行转账。"
}}
*/
    private long tid;
    private long sid;
    private String titleShow;
    private String comment;
    private String hostNameShow;
    private String urlShow;
    private String name;
    private String date;

    public HotComment(Map<String, Object> jSONObject) {
        parse(jSONObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        Map<String, Object> commentShowJSONObject = (Map<String, Object>)jSONObject.get("comment_show");
        this.setTid(Long.parseLong(commentShowJSONObject.get("tid").toString()));
        this.setSid(Long.parseLong(commentShowJSONObject.get("sid").toString()));
        this.setComment(commentShowJSONObject.get("comment").toString());
        this.setHostNameShow(commentShowJSONObject.get("host_name_show").toString());
        this.setTitleShow(commentShowJSONObject.get("title_show").toString());
        this.setDate(commentShowJSONObject.get("date").toString());
        String name = commentShowJSONObject.get("name").toString();
        this.setName(name.isEmpty() ? "匿名人士" : name);

//        this.setTitle(jSONObject.get("title").toString());
//        this.setUrlShow(jSONObject.get("url_show").toString());
//        this.setHometextShowShort(jSONObject.get("hometext_show_short").toString());
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getTitleShow() {
        return titleShow;
    }

    public void setTitleShow(String titleShow) {
        this.titleShow = titleShow;
    }

    public String getUrlShow() {
        return urlShow;
    }

    public void setUrlShow(String urlShow) {
        this.urlShow = urlShow;
    }

    public String getHostNameShow() {
        return hostNameShow;
    }

    public void setHostNameShow(String hostNameShow) {
        this.hostNameShow = hostNameShow;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
