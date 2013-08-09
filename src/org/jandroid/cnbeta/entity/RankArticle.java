package org.jandroid.cnbeta.entity;

import org.json.simple.JSONObject;

import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class RankArticle {

    private JSONObject jSONObject;

    //排行序号
    private int number;
    private long sid;
    private String title;
    private String hometext;
    private String logo;
    private String url;
    private String time;
    private int comment;

    public RankArticle(JSONObject jSONObject) {
        this.jSONObject = jSONObject;
        parse();
    }

    private void parse() {
        this.setSid(Long.parseLong(jSONObject.get("sid").toString()));
        this.setNumber(Integer.parseInt(jSONObject.get("number").toString()));
        this.setTitle(jSONObject.get("title").toString());
        this.setLogo(jSONObject.get("logo").toString());
        this.setUrl(jSONObject.get("url").toString());
        this.setHometext(jSONObject.get("hometext").toString());
        this.setTime(jSONObject.get("time").toString());
        this.setComment(Integer.parseInt(jSONObject.get("comment").toString()));
    }

    public JSONObject getJSONObject() {
        return jSONObject;
    }

    public Map<String, Object> toMap() {
        return jSONObject;
    }

    public String toJSONString() {
        return jSONObject.toJSONString();
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHometext() {
        return hometext;
    }

    public void setHometext(String hometext) {
        this.hometext = hometext;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
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

        RankArticle that = (RankArticle) o;

        if (sid != that.sid) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (sid ^ (sid >>> 32));
    }
}

/*
<dl class="item item_1">
    <dt>
        <i class="number">1</i>
        <a href="/articles/246048.htm" target="_blank">微软发布开发者预览版IE 11 for Windows 7</a>
    </dt>
    <dd>
        <div class="pic">
            <img src="http://static.cnbetacdn.com/newsimg/2013/0725/01374766432.jpg_w600.jpg_180x132.jpg" />
        </div>
        <div class="newsinfo cf">
            <p>微软刚刚为Windows 7与Windows Server 2008 R2系统发布了IE11开发者预览版。当然对于...                                <a href="/articles/246048.htm" target="_blank">阅读全文&gt;&gt;</a></p>
            <div class="tools">
                <div class="share">
                    <ul>
                        <li class="comment" title="评论">30</li>
                        <li class="s-t"></li>
                        <li class="sina"><a title="分享到新浪微博" href="javascript:void(0)" onclick="javascript:jump('weibosina',this)" target="_blank"></a></li>
                        <li class="qq"><a title="分享到QQ空间" href="javascript:void(0)" onclick="javascript:jump('qq',this)" target="_blank"></a></li>
                        <li class="blg"><a title="分享到搜狐微博" href="javascript:void(0)" onclick="javascript:jump('weibosohu',this)" target="_blank"></a></li>
                        <li class="rrw"><a title="分享到人人网" href="javascript:void(0)" onclick="javascript:jump('renren',this)" target="_blank"></a></li>
                        <li class="db"><a title="分享到豆瓣" href="javascript:void(0)" onclick="javascript:jump('douban',this)" target="_blank"></a></li>
                        <li class="itb"><a title="分享到百度贴吧" href="javascript:void(0)" onclick="javascript:jump('itb',this)" target="_blank"></a></li>
                    </ul>
                </div>
                <div class="time">illumi 发表于 2013-07-26 01:17:46</div>
            </div>
        </div>
    </dd>
</dl>
*/
