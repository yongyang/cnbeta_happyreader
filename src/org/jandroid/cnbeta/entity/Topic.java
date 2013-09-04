package org.jandroid.cnbeta.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Topic implements Serializable {

    private long id;
    private String name;
    private String logo;

    public Topic(Map<String, Object> jSONObject) {
        parse(jSONObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        this.setId(Long.parseLong(jSONObject.get("id").toString()));
        this.setName(jSONObject.get("name").toString());
        this.setLogo(jSONObject.get("logo").toString());
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "RealtimeArticle{" +
                "sid=" + id +
                ", title='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic that = (Topic) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
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