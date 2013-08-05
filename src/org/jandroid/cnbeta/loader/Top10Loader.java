package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Top10Loader extends AbstractLoader<Map<String, List<Article>>>{
    //分析该页面
    public static final String URL_FORMAT = "http://www.cnbeta.com/top10.htm&_={0}";

    public static final String HITS24 = "hits24";
    public static final String HITS_WEEK = "hits_week";
    public static final String HITS_MONTH = "hits_month";

    public static final String COMMENTS24 = "comments24";
    public static final String COMMENTS_WEEK = "comments_week";
    public static final String COMMENTS_MONTH = "comments_month";

    //推荐
    public static final String XPATH_EXPRESSION_RECOMMEND = "//div[contains(@class,'mt10'), not(@id)]";
//    public static final String XPATH_EXPRESSION_RECOMMEND = "//div[contains(@class,'mt10'), last()]";

    public final static Pattern patter = Pattern.compile("/></a>");

    @Override
    public Map<String, List<Article>> fromDisk(File baseDir) throws Exception {
        return null;
    }

    @Override
    public Map<String, List<Article>> fromHttp() throws Exception {
        String url = getURL();
        String responseHTML = CnBetaHttpClient.getInstance().httpGet(url);
        return null;
    }

    @Override
    public void toDisk(File baseDir, Map<String, List<Article>> stringListMap) throws Exception {

    }

    private String getURL(){
        return MessageFormat.format(URL_FORMAT, "" + System.currentTimeMillis());
    }

    private Map<String, List<Article>> parsePage(String responseHTML){
        //<img src="http://static.cnbetacdn.com/newsimg/2013/0803/01375505447.jpg_180x132.jpg" /></a>
        //删除掉多余的</a>
        String fixedHTML = patter.matcher(responseHTML).replaceAll("/>");
        Document document = Jsoup.parse(fixedHTML, "utf-8");
        // select all div elements with class=mt10
        Elements elements = document.select("div.mt10");
        Element hits24Element = elements.get(0);
        Element hitsWeekElement = elements.get(1);
        Element hitsMonthElement = elements.get(2);
        Element comments24Element = elements.get(3);
        Element commentsWeekElement = elements.get(4);
        Element commentsMonthElement = elements.get(5);
        Element recommendElement = elements.get(6);
        return null;
    }

    private List<Article> parseElement(Element rankElement){
        return null;
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