package org.jandroid.cnbeta.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Article extends BaseArticle{
// {"sid":"245736",
// "title_show":"金色iPhone5S与廉价iPhone、iPhone5对比照曝光",
// "hometext_show_short":"感谢T客在线的投递前几天微博用户@C科技为我们带来了两张金色iPhone5S的谍照，今天他又放出了一组iPhone5S谍照，其中包括廉价iPhone、iPhone5、金色iPhone5S后...",
// "logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374586704.jpg_w600.jpg_180x132.jpg",
// "url_show":"/articles/245736.htm",
// "counter":"1959",
// "comments":"5",
// "score":"-20",
// "time":"2013-07-23 21:38:27"}

    private String hometextShowShort;
    private String logo;
    private String urlShow;
    private int counter;
    private int comments;
    private int score;
    private String time;

    public Article(Map<String, Object> jSONObject) {
        parse(jSONObject);
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

    public String getTitleShow() {
        return getTitle();
    }

    public void setTitleShow(String titleShow) {
        setTitle(titleShow);
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getHometextShowShort() {
        return hometextShowShort;
    }

    public void setHometextShowShort(String hometextShowShort) {
        this.hometextShowShort = hometextShowShort;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUrlShow() {
        return urlShow;
    }

    public void setUrlShow(String urlShow) {
        this.urlShow = urlShow;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
