package org.jandroid.cnbeta.entity;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Content {
    
    //阅读次数
    private int viewNum;
    //评论次数
    private int commentNum;
    // 稿源
    private String where;
    
    //用于发起请求的验证信息
    private String token;
    private String sn;
    //所属分类主题
    private int topic;
    private String topicImage;
    
    //文章中的图片地址列表
    private List<String> images = new ArrayList<String>();

    // 重新 format 之后的 html 内容
    private String content;
    
    private List<Comment> comments = new ArrayList<Comment>();

    public Content(JSONObject jsonObject) {

    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getTopicImage() {
        return topicImage;
    }

    public void setTopicImage(String topicImage) {
        this.topicImage = topicImage;
    }

    public int getViewNum() {
        return viewNum;
    }

    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}
