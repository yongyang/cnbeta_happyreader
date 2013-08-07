package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.RankArticle;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Top10Loader extends AbstractLoader<Map<String, List<RankArticle>>>{
    //分析该页面
    public static final String URL_FORMAT = "http://www.cnbeta.com/top10.htm?_={0}";

    public static final String HITS24 = "hits24";
    public static final String HITS_WEEK = "hits_week";
    public static final String HITS_MONTH = "hits_month";

    public static final String COMMENTS24 = "comments24";
    public static final String COMMENTS_WEEK = "comments_week";
    public static final String COMMENTS_MONTH = "comments_month";

    public static final String RECOMMEND = "recommend";

    public final static Pattern patter = Pattern.compile("/></a>");

    @Override
    public Map<String, List<RankArticle>> fromDisk(File baseDir) throws Exception {
        return null;
    }

    @Override
    public Map<String, List<RankArticle>> fromHttp() throws Exception {
        String url = getURL();
        String responseHTML = CnBetaHttpClient.getInstance().httpGet(url);
        return parsePage(responseHTML);
    }

    @Override
    public void toDisk(File baseDir, Map<String, List<RankArticle>> stringListMap) throws Exception {

    }

    private String getURL(){
        return MessageFormat.format(URL_FORMAT, "" + System.currentTimeMillis());
    }

    private Map<String, List<RankArticle>> parsePage(String responseHTML){
        Map<String, List<RankArticle>> articlesMap = new HashMap<String, List<RankArticle>>();
        //<img src="http://static.cnbetacdn.com/newsimg/2013/0803/01375505447.jpg_180x132.jpg" /></a>
        //删除掉多余的</a>
        Document document = Jsoup.parse(responseHTML, "utf-8");
        // select all div elements with class=mt10
        Elements elements = document.select("div.mt10");

        Element hits24Element = elements.get(0);
        articlesMap.put(HITS24, parseRankTypeElement(hits24Element));
        Element hitsWeekElement = elements.get(1);
        articlesMap.put(HITS_WEEK, parseRankTypeElement(hitsWeekElement));
        Element hitsMonthElement = elements.get(2);
        articlesMap.put(HITS_MONTH, parseRankTypeElement(hitsMonthElement));
        Element comments24Element = elements.get(3);
        articlesMap.put(COMMENTS24, parseRankTypeElement(comments24Element));
        Element commentsWeekElement = elements.get(4);
        articlesMap.put(COMMENTS_WEEK, parseRankTypeElement(commentsWeekElement));
        Element commentsMonthElement = elements.get(5);
        articlesMap.put(COMMENTS_MONTH, parseRankTypeElement(commentsMonthElement));
        Element recommendElement = elements.get(6);
        articlesMap.put(RECOMMEND, parseRankTypeElement(recommendElement));

        return articlesMap;
    }

    private List<RankArticle> parseRankTypeElement(Element rankElement){

        List<RankArticle> rankArticles = new ArrayList<RankArticle>();

        for(Element rankItem : rankElement.select("dl")){
            String number = rankItem.getElementsByClass("number").first().text();
            Element linkElement = rankItem.getElementsByTag("a").first();
            String title = linkElement.text();
            String url = linkElement.attr("href");
            int sidFrom = url.length() - "246048.htm".length();
            String sid = url.substring(sidFrom, sidFrom+6);
            String logo =  rankItem.getElementsByTag("img").first().attr("src");
            String hometext = rankItem.getElementsByTag("p").first().ownText();
            String time = rankItem.getElementsByClass("time").first().text();
            time = time.substring(time.length() - "2013-07-26 01:17:46".length(), time.length());

            JSONObject rankArticleJSONObject = new JSONObject();
            rankArticleJSONObject.put("number", Integer.parseInt(number));
            rankArticleJSONObject.put("sid", Integer.parseInt(sid));
            rankArticleJSONObject.put("title", title);
            rankArticleJSONObject.put("url", url);
            rankArticleJSONObject.put("logo", logo);
            rankArticleJSONObject.put("hometext", hometext);
            rankArticleJSONObject.put("time", time);

            rankArticles.add(new RankArticle(rankArticleJSONObject));
        }

        return rankArticles;
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
