package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.MRankArticle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class MRankListLoader extends AbstractLoader<List<MRankArticle>>{
    //分析该页面
    public static final String URL_FORMAT = "http://m.cnbeta.com/{0}.htm";
//    http://m.cnbeta.com/hot.htm
//    http://m.cnbeta.com/argue.htm

    public static enum Type {
        RECOMMEND("commend"),
        HOT("hot"),
        ARGUE("argue");

        private String type;

        private Type(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }
    }

    private Type type;

    public MRankListLoader(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public List<MRankArticle> fromDisk(File baseDir) throws Exception {
        String s = readDisk(baseDir);
        JSONArray mRankJSONArray = (JSONArray)JSONValue.parse(s);
        List<MRankArticle>  mRankArticles = new ArrayList<MRankArticle>(mRankJSONArray.size());
        for(int i =0; i<mRankJSONArray.size(); i++){
            JSONObject jSONObject = (JSONObject)mRankJSONArray.get(i);
            mRankArticles.add(new MRankArticle(jSONObject));
        }
        return mRankArticles;
    }

    @Override
    public List<MRankArticle> httpLoad(File baseDir) throws Exception {
        String url = getURL();
        String responseHTML = CnBetaHttpClient.getInstance().httpGet(url);
        List<MRankArticle>  mRankArticles = parsePage(responseHTML);

        JSONArray mRankJSONArray = new JSONArray();
        for(MRankArticle mRankArticle : mRankArticles){
            mRankJSONArray.add(mRankArticle.getJSONObject());
        }
        writeDisk(baseDir, mRankJSONArray.toJSONString());
        return mRankArticles;
    }

    private String getURL(){
        return MessageFormat.format(URL_FORMAT, "" + getType().getTypeString());
    }

    private List<MRankArticle> parsePage(String responseHTML){
        List<MRankArticle> rankArticles = new ArrayList<MRankArticle>();

        Document document = Jsoup.parse(responseHTML, "UTF-8");
        // select all div elements with class=list
        Elements elements = document.select("div.list");

        for(Element element : elements){
            rankArticles.add(parseRankArticleElement(element));
        }
        return rankArticles;
    }

    private MRankArticle parseRankArticleElement(Element rankElement){

        Element hrefElement = rankElement.getElementsByTag("a").first();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("title", hrefElement.text());
        String href =hrefElement.attr("href");
        long sid = Long.parseLong(href.substring(href.indexOf("id=") + 3, href.length()));
        jSONObject.put("sid", sid);

        return new MRankArticle(jSONObject);
    }

    @Override
    public String getFileName() {
        return "ranks_" + getType().getTypeString();
    }
}

/*
<div class="list"><a href="/view.htm?id=251084">[快讯]微软收购诺基亚设备和服务部门 艾洛普将回到微软</a></div>
<div class="list"><a href="/view.htm?id=250988">[图文直播][已完结]魅族MX3发布会 定价2499元</a></div>
<div class="list"><a href="/view.htm?id=250339">企鹅终于参战 腾讯微云推出10T免费云存储</a></div>
<div class="list"><a href="/view.htm?id=250474">消息称谷歌Android产品管理副总裁Hugo Barra辞职加盟小米公司</a></div>
<div class="list"><a href="/view.htm?id=250094">三大运营商逐年降价引后遗症 再降空间已不大</a></div>
<div class="list"><a href="/view.htm?id=250349">走访2000人：终于找到诺基亚销量不好的原因了</a></div>
<div class="list"><a href="/view.htm?id=250732">百度拼了 1元钱可购网盘2T永久空间</a></div>
<div class="list"><a href="/view.htm?id=250666">高徳反击百度：还是快去多卖点假药吧</a></div>
<div class="list"><a href="/view.htm?id=250982">[组图]骚黄来了：小米3谍照再曝光</a></div>
<div class="list"><a href="/view.htm?id=250253">[多图]简体中文64位企业版Windows 8.1 RTM已泄露</a></div>
*/
