package org.jandroid.cnbeta.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class TopicArticle extends BaseArticle {
//{"logo":"http:\/\/static.cnbetacdn.com\/newsimg\/2013\/0903\/45_1378207057.jpg_w600.jpg_180x132.jpg","sid":"251146","time":"2013-09-03 19:17:48","hometext_show_short2":"未来的iOS设备可能会支持独特的安全功能，可以根据用户手势输入的不同开启...","url_show":"\/articles\/251146.htm","title_show":"苹果新专利：通过特定手势解锁应用，设备功能","aid":"ugmbbc","comments":"4"}
    private String hometextShowShort2;
    private String logo;
    private String urlShow;
    private int comments;
    private String time;

    public TopicArticle(Map<String, Object> jSONObject) {
        parse(jSONObject);
    }
    
    private void parse(Map<String, Object> jSONObject) {
        this.setSid(Long.parseLong(jSONObject.get("sid").toString()));
        this.setTitleShow(jSONObject.get("title_show").toString());
        this.setUrlShow(jSONObject.get("url_show").toString());
        this.setHometextShowShort2(jSONObject.get("hometext_show_short2").toString());
        this.setLogo(jSONObject.get("logo").toString());
        this.setComments(Integer.parseInt(jSONObject.get("comments").toString()));
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

    public String getHometextShowShort2() {
        return hometextShowShort2;
    }

    public void setHometextShowShort2(String hometextShowShort2) {
        this.hometextShowShort2 = hometextShowShort2;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
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
