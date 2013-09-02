package org.jandroid.cnbeta.entity;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class HotComment implements Serializable {
/*
    jQuery180033392061898484826_1377945655437({"status":"success","result":[
        {"id":"5275","tid":"7792355","start_time":"1377759982","end_time":"1377844460","cb1":"250523","addby":"","comment_show":{"tid":"7792355","pid":"0","sid":"250523","date":"2013-08-29 15:06:22","name":"","email":null,"url":null,"host_name":"218.58.75.118","subject":"Re:[\u8bd1\u6587\u5168\u6587]Hugo Barra\u5728Google+\u4e0a\u53d1\u8868\u7684\u8f9e\u522b\u4fe1","comment":"\u4e00\u573a\u594b\u4e0d\u987e\u8eab\u7684\u7231\u60c5\u548c\u4e00\u6b21\u8bf4\u8d70\u5c31\u8d70\u7684\u65c5\u884c","score":"196","reason":"0","last_moderation_ip":"0","allow":"0","report":"0","arguetype":"0","userid":"0","host_name_show":"\u5c71\u4e1c\u7701\u9752\u5c9b\u5e02","title_show":"[\u8bd1\u6587\u5168\u6587]Hugo Barra\u5728Google+\u4e0a\u53d1\u8868\u7684\u8f9e\u522b\u4fe1","article_url":"\/articles\/250523.htm"}},{"id":"5274","tid":"7792414","start_time":"1377760755","end_time":"1377844305","cb1":"250530","addby":"","comment_show":{"tid":"7792414","pid":"7792301","sid":"250530","date":"2013-08-29 15:19:15","name":"","email":null,"url":null,"host_name":"222.222.216.211","subject":"Re:\u5973\u4e3b\u89d2\u66dd\u5149\uff1a\u5c31\u662f\u5979\u8ba9\u8c37\u6b4c\u526f\u603b\u88c1\u6295\u5954\u5c0f\u7c73","comment":"\u4f30\u8ba1\u4e5f\u88ab\u6253\u4e86\u4e00\u5df4\u638c\uff0c\u4e4b\u540e\u6295\u5954\u5c0f\u7c73\u3002\u3002\u3002\n\u5386\u53f2\u603b\u662f\u60ca\u4eba\u7684\u76f8\u4f3c\uff01\uff01","score":"633","reason":"5","last_moderation_ip":"0","allow":"0","report":"0","arguetype":"0","userid":"0","host_name_show":"\u6cb3\u5317\u7701\u77f3\u5bb6\u5e84\u5e02","title_show":"\u5973\u4e3b\u89d2\u66dd\u5149\uff1a\u5c31\u662f\u5979\u8ba9\u8c37\u6b4c\u526f\u603b\u88c1\u6295\u5954\u5c0f\u7c73","article_url":"\/articles\/250530.htm"}},{"id":"5273","tid":"7792301","start_time":"1377759315","end_time":"1377817274","cb1":"250530","addby":"","comment_show":{"tid":"7792301","pid":"0","sid":"250530","date":"2013-08-29 14:55:15","name":"","email":null,"url":null,"host_name":"218.75.16.158","subject":"Re:\u5973\u4e3b\u89d2\u66dd\u5149\uff1a\u5c31\u662f\u5979\u8ba9\u8c37\u6b4c\u526f\u603b\u88c1\u6295\u5954\u5c0f\u7c73","comment":"\u5e74\u5ea6\u5927\u620f\uff0c\u4e0e\u5927\u54e5\u62a2\u5973\u4eba","score":"514","reason":"2","last_moderation_ip":"0","allow":"0","report":"0","arguetype":"0","userid":"0","host_name_show":"\u6d59\u6c5f\u7701\u6e29\u5dde\u5e02\u7535\u4fe1","title_show":"\u5973\u4e3b\u89d2\u66dd\u5149\uff1a\u5c31\u662f\u5979\u8ba9\u8c37\u6b4c\u526f\u603b\u88c1\u6295\u5954\u5c0f\u7c73","article_url":"\/articles\/250530.htm"}},
        {"id":"5272","tid":"7790009","start_time":"1377712585","end_time":"1377814472","cb1":"250421","addby":"","comment_show":{"tid":"7790009","pid":"0","sid":"250421","date":"2013-08-29 01:56:25","name":"","email":null,"url":null,"host_name":"61.171.214.240","subject":"Re:\u4efb\u5929\u58023DS\u9609\u5272\u72482DS\u53d1\u5e03 \u53cc\u5c4f\u76f4\u677fXL\u8bbe\u8ba1","comment":"\u90a3\u5e74\uff0c\u4e2d\u534e\u6b66\u58eb\u4f1a\u6210\u7acb\u3002\u4ece\u5357\u65b9\u6765\u4e86\u4e00\u4e2a\u4eba\uff0c\u8bdd\u4e0d\u591a\u8bf4\uff0c\u624b\u4e2d\u62ff\u7740\u4e00\u4e2a\u4efb\u5929\u58022DS\uff0c\u8ba9\u6211\u5927\u5e08\u5144\u674e\u5b58\u4e49\u63b0\u5f00\u3002","score":"169","reason":"0","last_moderation_ip":"0","allow":"0","report":"0","arguetype":"0","userid":"0","host_name_show":"\u4e0a\u6d77\u5e02\u6d66\u4e1c\u65b0\u533a\u7535\u4fe1ADSL","title_show":"\u4efb\u5929\u58023DS\u9609\u5272\u72482DS\u53d1\u5e03 \u53cc\u5c4f\u76f4\u677fXL\u8bbe\u8ba1","article_url":"\/articles\/250421.htm"}},
        {"id":"5271","tid":"7789406","start_time":"1377696275","end_time":"1377754319","cb1":"250405","addby":"","comment_show":{"tid":"7789406","pid":"0","sid":"250405","date":"2013-08-28 21:24:35","name":"Midres","email":null,"url":null,"host_name":"111.172.145.210","subject":"Re:\u534e\u4e3a\u8363\u80003\u53d1\u5e03 \u552e1888\u5143 IP57\u9632\u62a4","comment":"\u7956\u4f20K3V2\u9886\u5148\u53cb\u5546\u4e09\u5341\u5e74","score":"420","reason":"7","last_moderation_ip":"0","allow":"0","report":"0","arguetype":"0","userid":"2538","host_name_show":"\u6e56\u5317\u7701\u6b66\u6c49\u5e02","title_show":"\u534e\u4e3a\u8363\u80003\u53d1\u5e03 \u552e1888\u5143 IP57\u9632\u62a4","article_url":"\/articles\/250405.htm"}},{"id":"5270","tid":"7789485","start_time":"1377697393","end_time":"1377754295","cb1":"250400","addby":"","comment_show":{"tid":"7789485","pid":"7789399","sid":"250400","date":"2013-08-28 21:43:13","name":"","email":null,"url":null,"host_name":"125.85.38.60","subject":"Re:\u9ad8\u5fb7\u5ba3\u5e03\u9ad8\u5fb7\u5bfc\u822a\u5373\u65e5\u8d77\u6c38\u4e45\u514d\u8d39","comment":"\u6240\u4ee5\u8bf4\u53ea\u6709\u5145\u5206\u7ade\u4e89\uff0c\u6d88\u8d39\u8005\u624d\u662f\u4e0a\u5e1d\uff0c\u6570\u5b57\uff0c\u4f01\u9e45\uff0c\u8d25\u6bd2\uff0c\u963f\u4e3d\u6076\u5fc3\u5f52\u6076\u5fc3\uff0c\u4ed6\u4eec\u8fd8\u662f\u5974\u624d\uff0c\u4e24\u6876\u6cb9\u548c\u901a\u4fe1\u884c\u4e1a\u90a3\u4e9b\u5bb6\u4f19\u662f\u7237\u7237\u3002","score":"464","reason":"9","last_moderation_ip":"0","allow":"0","report":"1","arguetype":"0","userid":"0","host_name_show":"\u91cd\u5e86\u5e02\u6c5f\u5317\u533a","title_show":"\u9ad8\u5fb7\u5ba3\u5e03\u9ad8\u5fb7\u5bfc\u822a\u5373\u65e5\u8d77\u6c38\u4e45\u514d\u8d39","article_url":"\/articles\/250400.htm"}},{"id":"5269","tid":"7790804","start_time":"1377741113","end_time":"1377754278","cb1":"250482","addby":"","comment_show":{"tid":"7790804","pid":"0","sid":"250482","date":"2013-08-29 09:51:53","name":"","email":null,"url":null,"host_name":"140.224.195.84","subject":"Re:\u8d1d\u5c14\u5b9e\u9a8c\u5ba4\u7814\u53d1\u201c\u771f\u6b63\u610f\u4e49\u4e0a\u7684\u6027\u7231\u673a\u5668\u4eba\u201d","comment":"\u4e70\u5f97\u8d77\u7684\u7528\u4e0d\u7740\uff0c\u7528\u5f97\u7740\u7684\u4e70\u4e0d\u8d77","score":"718","reason":"4","last_moderation_ip":"0","allow":"0","report":"0","arguetype":"0","userid":"0","host_name_show":"\u4e2d\u56fd\u7535\u4fe1\u9aa8\u5e72\u7f51","title_show":"\u8d1d\u5c14\u5b9e\u9a8c\u5ba4\u7814\u53d1\u201c\u771f\u6b63\u610f\u4e49\u4e0a\u7684\u6027\u7231\u673a\u5668\u4eba\u201d","article_url":"\/articles\/250482.htm"}},
        ]})
*/
    private long tid;
    private long sid;
    private String title;
    private String hometextShowShort;
    private String urlShow;


    public HotComment(Map<String, Object> jSONObject) {
        parse(jSONObject);
    }

    private void parse(Map<String, Object> jSONObject) {
        this.setTid(Long.parseLong(jSONObject.get("tid").toString()));
//        this.setSid(Long.parseLong(jSONObject.get("sid").toString()));
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
