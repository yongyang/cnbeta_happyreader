package org.jandroid.cnbeta.loader;

import android.util.Base64;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.client.RequestContext;
import org.jandroid.cnbeta.entity.Content;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentLoader extends AbstractLoader<Content> {

    private static String URL_TEMPLATE = "http://www.cnbeta.com/articles/{0}"+".htm";
    
    private long sid ;

    public ArticleContentLoader(long sid) {
        this.sid = sid;
    }

    public long getArticleSid() {
        return sid;
    }

    @Override
    public Content httpLoad(File baseDir, RequestContext requestContext) throws Exception {
        String url = getURL();
        String responseHTML = CnBetaHttpClient.getInstance().httpGet(url, requestContext);

        responseHTML = fixResponseHTML(responseHTML);

        Document document = Jsoup.parse(responseHTML, "utf-8");
        Element bodyElement = document.select("div.body").first();

        cleanBodyElement(bodyElement);

        // add token and sn
        String token = getToken(responseHTML);
        String sn = getSN(responseHTML);
        bodyElement.attr("token", token);
        bodyElement.attr("sn", sn);

        writeDisk(baseDir, bodyElement.outerHtml());

        return parseBodyElement(bodyElement);
    }

    private String fixResponseHTML(String origResponseHTML){
        String responseHTML = origResponseHTML.replaceAll(";\"\">", ";\">");
        return responseHTML.replaceAll("<center>", "");
    }

    private void cleanBodyElement(Element bodyElement) {

        for(Element sectionElement : bodyElement.select("div#googleAd_afc")){
            sectionElement.remove();
        }

        //ad section
        for(Element sectionElement : bodyElement.select("section[style]")){
            sectionElement.remove();
        }

        for(Element clearElement : bodyElement.select("div.clear")){
            clearElement.remove();
        }

        for(Element clearElement : bodyElement.select("div.cbv")){
            clearElement.remove();
        }

        for(Element clearfixElement : bodyElement.select("div.clearfix")){
            clearfixElement.remove();
        }
        for(Element ratingboxElement : bodyElement.select("div#rating_box")){
            ratingboxElement.remove();
        }
        for(Element naviElement : bodyElement.select("div.navi")){
            naviElement.remove();
        }

    }


    @Override
    public Content fromDisk(File baseDir) throws Exception {
        String html = readDisk(baseDir);
        Document document = Jsoup.parse(html, "UTF-8");
        //JSoup会自动补上 <html><body>标签，所以需要取得 div.body
        Element bodyElement = document.select("div.body").first();
        return parseBodyElement(bodyElement);
    }

    @Override
    public String getFileName() {
        return "article_" + getArticleSid();
    }

    private String getURL(){
        return MessageFormat.format(URL_TEMPLATE, "" + getArticleSid());
    }

    private Content parseBodyElement(Element bodyElement) throws Exception {
        //TODO: 解析上一篇，下一篇 ???
        Element titleElement = bodyElement.getElementById("news_title");
        Element contentElement = bodyElement.select("section.article_content").first();
        Element dateElement = bodyElement.select("span.date").first();
        Element introductionElement = bodyElement.select("div.introduction").first();
        introductionElement.attr("style", "background-color: #fbfbfb; color: #43434; border: 1px solid #e5e5e5; border-left: 0px; border-right: 0px; padding-left: 10px; padding-right: 10px; margin-bottom: 10px");

        Element whereElement = bodyElement.select("span.where").first();

        JSONObject contentJSONObject = new JSONObject();
//        contentJSONObject.put("introduction", introductionElement.outerHtml());

        contentJSONObject.put("where", whereElement.getElementsByTag("a").isEmpty() ? whereElement.text() : whereElement.getElementsByTag("a").first().text());
        contentJSONObject.put("token", bodyElement.attr("token"));
        contentJSONObject.put("sn", bodyElement.attr("sn"));
        contentJSONObject.put("sid", getArticleSid());
        contentJSONObject.put("title", titleElement.text());
        contentJSONObject.put("time", dateElement.text());
        //NOTE: viewNum, commentNum will be set by ArticleCommentsLoader

        //NOTE: parse images in content before load
        List<String> images = new ArrayList<String>();

        //content img 处理
        for(Element imgElement : contentElement.select("div.content").first().getElementsByTag("img")) {

            // 取出原始的 img src, 交给 ImageLoader去异步加载
            String imgSrc = imgElement.attr("src");
            images.add(imgSrc);
            //设置id， ImageLoader根据 id 来更新图片
            imgElement.attr("id", Base64.encodeToString(imgSrc.getBytes(), Base64.NO_WRAP));
            //设置一个默认图片
            imgElement.attr("src", "file:///android_asset/default_img.png");
            //设置 alt，作为对照记录
            imgElement.attr("alt", imgSrc);
            //设置 onclick 事件， topics 图片除外
            if((imgElement.parent().attr("href") != null && imgElement.parent().attr("href").contains("topics"))) {
                String topicHref = imgElement.parent().attr("href");
                String topicId = topicHref.substring("/topics/".length(),topicHref.indexOf(".htm"));
                String topicName = imgElement.attr("title");
                imgElement.attr("onclick", "javascript:window.JS.openTopic(\'" + topicId + "\',\'" + topicName + "\')");
            }
            else {
                imgElement.attr("onclick", "javascript:window.JS.openImage(this.alt)");
            }
        }
        //必须最后设置 content，以保证img src 已经修改
        contentJSONObject.put("content", contentElement.outerHtml());
        Content content =  new Content(contentJSONObject);
        content.setImages(images);
        return content;
    }

    private String getToken(String responseHTML) {
        String key = "TOKEN: ";
        int start = responseHTML.indexOf(key)+key.length()+1;
        int length = "3bdbf3c8595f67eab8aacbb9780c1d78955ce4ee".length();
        return responseHTML.substring(start, start + length);
    }

    private String getSN(String responseHTML) {
        String key = "SN:";
        int start = responseHTML.indexOf(key) + key.length()+1;
        int length= "88747".length();
        return responseHTML.substring(start, start+length);

    }

}

/*
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="http://static.cnbetacdn.com/assets/js/jquery.js"></script>
<title>与Xbox One不同：PS4在推出时暂不支持“暂停和恢复”功能_SONY 索尼_cnBeta.COM</title>
<meta http-equiv="content-language" content="zh-cn" />
<meta http-equiv="imagetoolbar" content="false" />
<meta name="keywords" content="SONY 索尼,与Xbox One不同：PS4在推出时暂不支持“暂停和恢复”功能,cnBeta" />
<meta name="description" content="微软Xbox One和索尼PlayStation 4之间的战斗仍在继续。不过这一次，我们了解到的是，11月22日推出的Xbox One将会兑现其支持&amp;quot;暂停和恢复&amp;quot;功能的承诺；而在另一方面，PS4在推出时将不支持此功能。索尼证实该功能不会在PS4推出时立即可用：&amp;quot;有些功能不会在推出时可用，比如使系统处于低功率状态、并在需要时及时返回游戏的&amp;#39;暂停/恢复&amp;#39;模式&amp;quot;。" />
<meta name="resource-type"content="document" />
<meta name="copyright" content="(c)2003-2013 cnBeta.COM" />
<meta name="author" content="cnBeta" />
<meta name="robots" content="index, follow" />
<meta name="revisit-after" content="1 days" />
<meta name="rating" content="general" />
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
<link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="/backend.php" />
<link rel="alternate" type="application/atom+xml" title="ATOM 1.0" href="/backend.php?atom" />
<link rel="stylesheet" type="text/css" href="http://static.cnbetacdn.com/assets/css/base.css?v=20130808" />    <link rel="stylesheet" type="text/css" href="http://static.cnbetacdn.com/assets/css/front.css?v=20130808" />	<link rel="stylesheet" type="text/css" href="http://static.cnbetacdn.com/assets/css/jquery.fancybox.min.css?v=20130808" /><script>
var GV = {
    JS_ROOT: 'http://static.cnbetacdn.com/assets/js/',
    JS_VERSION: '20130808',
    TOKEN: 'b2e45642ad2d1acfeb4cb122e12adef6db0c5b3d',
    URL: {
        QZONE:"http://open.qzone.qq.com/like?url=http%3A%2F%2Fuser.qzone.qq.com%2F706290416&width=120&height=22&type=button_num",
        SINA:"http://widget.weibo.com/relationship/followbutton.php?language=zh_cn&width=136&height=22&uid=2769378403&style=2&btn=red&dpc=1",
        INDEX:{ALLNEWS: "/more.htm"},
        ARTICLE:{},
        TOPIC:{},
        ARGUE:{},
        LOGIN:"/user/login",
        LOGOUT:"/user/logout"
    },
    USER:{ID:0,NICK:"",ICON:""}
};
</script>
<!--[if lt IE 9]><script src="http://static.cnbetacdn.com/assets/base/html5.js?t=20130808" charset="utf-8"></script><![endif]-->
<script src="http://static.cnbetacdn.com/assets/js/cb.js?t=20130808" charset="utf-8"></script></head>
<body>
<div class="wrapper">
	<header class="global_head">
		<nav class="tiny_bar">
            <div class="cb_login" id="cb_login"></div>
			<ul class="cb_rss">
                <li><iframe id="sina_follow" src="" allowtransparency="true" scrolling="no" border="0" frameborder="0" align="absmiddle" style="width:115px;height:22px;border:none;overflow:hidden;"></iframe></li>
				<li><iframe id="qzone_follow" src="" allowtransparency="true" scrolling="no" border="0" frameborder="0" align="absmiddle" style="width:115px;height:22px;border:none;overflow:hidden;"></iframe></li>
				<li class="a"><a class="feed" href="/backend.php" target="_blank" title="订阅本站资讯">资讯</a></li>
				<li class="a"><a class="feed" href="/commentrss.php" target="_blank" title="订阅本站精彩评论">评论</a></li>
			</ul>
		</nav>
		<h1 class="cnbeta_logo"><a href="http://www.cnbeta.com">cnBeta.COM_中文业界资讯站</a></h1>
		<nav class="global_navi">
            <div id="cb_search">
                <form action="http://so.cnbeta.com/cse/search" target="_blank">
                <input name="s" type="hidden" value="14146556968177638016">
                <input type="text" name="q" size="30" class="search_key">
                <input type="submit" value=""class="search_btn"></form>
            </div>
			<ul class="main_navi">
                                <li   >
                    <a href="http://www.cnbeta.com/"   >首页</a>
                </li>
                                <li   >
                    <a href="http://www.cnbeta.com/deliver" target="_blank" style="color:#ffcc00;" >投稿</a>
                </li>
                                <li   >
                    <a href="http://www.cnbeta.com/topics/306.htm"   >互动</a>
                </li>
                                <li   >
                    <a href="http://www.cnbeta.com/soft.htm"   >软件</a>
                </li>
                                <li   >
                    <a href="http://www.cnbeta.com/topics.htm"   >主题</a>
                </li>
                                <li   >
                    <a href="http://www.cnbeta.com/top10.htm"   >排行</a>
                </li>
                                <li   >
                    <a href="http://www.cnbeta.com/about/index.htm"   >关于</a>
                </li>
                			</ul>
            			<ul class="sub_navi">
                                <li  >
                    <a href="http://www.cnbeta.com/topics/9.htm"   >苹果</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/52.htm"   >Google</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/305.htm"   >视点观察</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/448.htm"   >科学探索</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/487.htm"   >小米</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/70.htm"   >硬件</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/353.htm"   >B2C</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/371.htm"   >三星</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/453.htm"   >人物</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/243.htm"   >手机</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/147.htm"   >诺基亚</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/422.htm"   >美国</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/45.htm"   >警告</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/39.htm"   >游戏</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/469.htm"   >3D</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/197.htm"   >索尼</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/144.htm"   >Ultrabook</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/326.htm"   >Glass</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/506.htm"   >Windows 8.1</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/439.htm"   >HTC</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/4.htm"   >微软</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/184.htm"   >WP</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/444.htm"   >Android</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/91.htm"   >Yahoo!</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/32.htm"   >Intel</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/138.htm"   >通信技术</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/331.htm"   >华为</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics/464.htm"   >iPad</a>
                </li>
                                <li  >
                    <a href="http://www.cnbeta.com/topics.htm"   >更多...</a>
                </li>
                			</ul>
            <ul class="sub_navi_bottom"></ul>
            <span class="nav_arrow"></span>
            		</nav>
	</header>
	<section style="text-align:center;">
    <!--topbanner start--><!-- 广告位：CBV4-980x90 文章页页头 -->
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "693405";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script><!--topbanner end-->
</section>
<div id="left_art" class="left_art left_art_short">
	<div class="left_art_box dig_box" id="dig_btn">
        <a rel="nofollow" title="顶一个" class="" href="javascript:void(0);"><span class="dig_label">我顶</span><br>(<em>0</em>)</a>
    </div>
	<div class="left_art_box fav_box" id="favorite_btn" style="display:none">
        <a rel="nofollow" title="收藏本文" class="" href="javascript:void(0);"><span class="favorite_label">收藏</span><br>(<em>0</em>)</a>
    </div>
	<div class="left_art_box cmt_box"><a onclick="return scrollToPosition('comment');" href="javascript:void(0);">评论<br>(<font class="comment_num"></font>)</a></div>
</div>
<section class="main_content">
    <section class="main_content_left">
        <article class="cb_box article">
            <div class="body">
                <header>
                    <h2 id="news_title">与Xbox One不同：PS4在推出时暂不支持“暂停和恢复”功能</h2>
                    <a alt="197" id="sign"></a>
                    <div class="title_bar">
                        <span class="date">2013-10-28 09:13:15</span>
                        <span class="counter"><span id="view_num"></span> 次阅读</span>
                        <span class="where">
                            稿源：<a href="http://www.cnbeta.com/">cnBeta.COM</a></span>
                        <span class="discuss"><a onclick="return scrollToPosition('comment');" href="javascript:void(0);"><font style="font-size:12px;color:#c00;" class="comment_num"></font></a> 条评论</span>
                    </div>
                </header>
                <div class="clearfix"></div>
                <section class="article_content">
                    <div class="introduction">
                                                <div style="float:right;margin-left:10px;margin-top:5px;margin-bottom:10px;margin-right:-20px;"><a href="/topics/197.htm" target="_blank"><img title="SONY 索尼" src="http://static.cnbetacdn.com/topics/2010-4-9 9-32-51.gif" /></a></div>
                                                <p>微软Xbox One和索尼PlayStation 4之间的战斗仍在继续。<strong>不过这一次，我们了解到的是，11月22日推出的Xbox One将会兑现其支持&quot;暂停和恢复&quot;功能的承诺；而在另一方面，PS4在推出时将不支持此功能。</strong>索尼证实该功能不会在PS4推出时立即可用：&quot;有些功能不会在推出时可用，比如使系统处于低功率状态、并在需要时及时返回游戏的&#39;暂停/恢复&#39;模式&quot;。<br/></p>                    </div>
                    <div class="content"><p style="text-align: center;"><img src="http://static.cnbetacdn.com/newsimg/2013/1028/96_1382922221.jpg_w600.jpg" title="xboxone_controller_4.jpg"/></p><p>微软公司的Albert Penello证实了<a data-type="2" data-keyword="Xbox" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">Xbox</a> One会在推出时支持&quot;暂停/恢复&quot;模式的信息。不过<a data-type="2" data-keyword="索尼" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">索尼</a>也表示，&quot;我们将在不久的将来带来额外的信息和功能&quot;。这一点是否会对你购买哪款产品产生影响呢？</p><p>[编译自：<a title="" target="_self" href="http://www.winbeta.org/news/unlike-xbox-one-ps4-does-not-support-suspend-and-resume-feature-launch">WinBeta</a>]<br/></p></div>
                    <div class="clear"></div>
                    <div id="googleAd_afc">
                    <!-- google custom begin-->
                    <script charset="utf-8" type="text/javascript">
                        function google_ad_request_done(google_ads) {
                            var s = "";
                            var i;
                            if(google_ads.length == 0) return;
                            s += '<div class="gbox830"><div class="gtitle"><a href=\"' + google_info.feedback_url  + '\" target="_blank">Google提供的广告</a></div>'
                            if(google_ads.length == 1){
                                s += '<div class="tbox1">' +
                                '<a href="' + google_ads[0].url + '" target="_blank">' + google_ads[0].line1 + '</a><br />' +
                                google_ads[0].line2 + "&nbsp;" + google_ads[0].line3 + '<br />' +
                                '<a href="' + google_ads[0].url + '" target="_blank" class="gaAddress">' + google_ads[0].visible_url + '</a>' + '</div>';
                            }else if (google_ads.length > 1){
                                for(i=0; i < google_ads.length; ++i) {
                                    s += '<div class="tbox">' +
                                        '<a href="' + google_ads[i].url + '" target="_blank">' + google_ads[i].line1 + '</a>'+"&nbsp;"+
                                        '<a href="' + google_ads[i].url + '" target="_blank" class="gaAddress">' + google_ads[i].visible_url + '</a><br />' +
                                        google_ads[i].line2 + "&nbsp;" + google_ads[i].line3 + '<br />' +
                                        '</div>';
                                    if (i<google_ads.length) s += "";
                                }
                            }
                            s += '</div>';
                            document.write(s);
                            return;
                        }
                        google_ad_client = "pub-9066977823953139";
                        google_ad_output = "js";
                        google_max_num_ads = "2";
                        google_ad_type = "text";
                        google_gl = "CN";
                        google_language = "zh-CN";
                        google_encoding = "utf8";
                        google_ad_channel= "0839583543";
                        google_feedback = "on";
                        google_adtest = 'off';
                        document.write("<scr"+"ipt type=text/javascript src=http://pagead2.googlesyndication.com/pagead/show_ads.js></scr"+"ipt>");
                    </script>
                    <!-- google custom end-->
                    </div>
                </section>
                <div class="clearfix"></div>
                <section style="text-align:center;padding:0px 10px;"">
                    <!--left1 start--><div class="cbv">
<p><!-- 广告位：CBV4 580x90 文章页文末 #1 -->
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "774209";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script></p>
<p><script type="text/javascript" >BAIDU_CLB_SLOT_ID = "611927";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script></p>
<p>
<!-- 广告位：CBV4-640x60 文章页文末 #1 -->
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "700021";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script></p>
<p><script type="text/javascript">/ * cnBeta文末580*90 * / var cpro_id = 'u792078';</script>
<script src="http://cpro.baidu.com/cpro/ui/c.js" type="text/javascript"></script></p>
</div><!--left1 end-->
                </section>
                <div class="clearfix" style="margin:0 10px;">
                    <div class="fl"><span class="author">[责任编辑：raymon725]</span></div>
                    <div class="fr">
                        <!-- Baidu Button BEGIN -->
                        <div id="bdshare" class="bdshare_b" style="line-height: 12px;">
                        <img src="http://bdimg.share.baidu.com/static/images/type-button-3.jpg?cdnversion=20120831" />
                        <a class="shareCount"></a>
                        </div>
                        <!-- Baidu Button END -->
                    </div>
                </div>
                <div id="rating_box" class="rating_box clearfix">
                    <div class="mod h-bd-box">
                        <div class="hd"><h3>文章打分</h3></div>
                    </div>
                    <div class="bd">
                        <span class="load"></span>
                        <div class="rating_down">
                            <ul>
                                <li title="垃圾中的战斗机,扣5分" data-score="-5"><a class="r-5">-5</a></li>
                                <li title="太垃圾了,扣4分" data-score="-4"><a class="r-4">-4</a></li>
                                <li title="傻冒新闻,扣3分" data-score="-3"><a class="r-3">-3</a></li>
                                <li title="差劲,扣2分" data-score="-2"><a class="r-2">-2</a></li>
                                <li title="不怎么样,扣1分" data-score="-1"><a class="r-1">-1</a></li>
                                <li title="平淡,0分" data-score="0"><a class="r0">0</a></li>
                            </ul>
                        </div>
                        <div class="rating_up">
                            <ul>
                                <li title="凑合吧,1分" data-score="1"><a class="r1">1</a></li>
                                <li title="还可以,2分" data-score="2"><a class="r2">2</a></li>
                                <li title="很不错,3分" data-score="3"><a class="r3">3</a></li>
                                <li title="太棒了,4分" data-score="4"><a class="r4">4</a></li>
                                <li title="太有才了,满分!" data-score="5"><a class="r5">5</a></li>
                            </ul>
                        </div>
                        <div class="rate_num">
                            <strong>-5</strong><strong>-4</strong><strong>-3</strong><strong>-2</strong><strong>-1</strong><strong>0</strong><strong>1</strong><strong>2</strong><strong>3</strong><strong>4</strong><strong>5</strong>
                        </div>
                        <div class="rated gray">当前平均分: <span>打分后显示</span></div>
                    </div>
                </div>
                <div class="navi">
                    <div class="mod h-bd-box">
                        <div class="hd"><h3>大家还在看</h3></div>
                    </div>
                    <div class="bd">
                        <div class="bd_r fr"><div style="width:200px"><a target="_blank" href="http://www.wy.cn/">唯一网络 wy.cn 专业服务器租用托管机柜大带宽 高防服务器</a><br>
<a target="_blank" href="http://click.mediav.com/c?type=2&db=mediav&pub=339_514078_1021574&cus=24185_133475_1131620_10478756_10478756000&url=http://shop.coolpad.cn/9970.html">5.9英寸1080p屏 酷派大观4曝光</a></div></div>
                        <div class="bd_l" id="bfd_box_vav" style="min-height:30px">
                        </div>
                    </div>
                </div>
            </div>
        </article>
        <section style="text-align:center;">
            <!--left2 start--><script type="text/javascript">
/*662x150 Metro* /
var cpro_id = "u1321009";
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/c.js" type="text/javascript"></script><!--left2 end-->
        </section>
        <section>
            <a id="comment" name="comment"></a>
            <nav class="blue_bar">
                <h4>网友评论</h4>
                <div class="post_count" style="display:none"></div>
            </nav>
            <div class="content_body">
                <div class="post_commt">
                    <div class="commt_l">
                        <input type="hidden" name="tid" value="">
                        <input type="hidden" name="sid" value="258098">
                        <input type="hidden" name="nowsubject" value="Re:与Xbox One不同：PS4在推出时暂不支持“暂停和恢复”功能" />
                        <div class="hd">
                            <span id="top_reply_logout" class="login" >
                                <img src="http://static.cnbetacdn.com/assets/images/user/face.png">
                                <a id="top_login_link" href="/user/login/popup" data-fancybox-type="iframe" class="logoA"><strong>登录</strong><div class="nav_new"></div></a>
                            </span>
                            <span id="top_reply_login" class="login" style="display:none">
                                <img width="20" height="20" src=""><span></span><a class="quitLogin" href="javascript:void(0)">[退出]</a>
                            </span>
                        </div>
                        <div class="info">

                            <div class="textarea_wraper"><textarea name="nowcomment" title="请输入评论内容" placeholder="请输入评论内容"></textarea></div>
                            <div class="commt_sub">
                                                                <span class="seccode_box">验证码：<input class="form_input" name="seccode" type="text" title="请输入验证码" align="absmiddle" />
                                <img id="seccode" title="刷新验证码" style="cursor:pointer;vertical-align:middle;" imgshow="0" src="" alt="" /></span>
                                                                <span class="fontsline" id="post_tips"></span>

                                <div style="text-align:right;padding:5px 0px"><button id="post_btn" class="submit disabled" href="javascript:void(0)"></button></div>

                            </div>
                        </div>
                    </div>
                    <div class="commt_r">
                    <!--left3 start--><!-- 广告位：CBV4-250x200 评论框 -->
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "685340";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script><!--left3 end-->
                    </div>
                    <div class="clear"></div>
                </div>
                <div class="commt_list" style="display:none">
                    <nav>
                        <span class="new_cmnt">最新评论</span>
                    </nav>
                    <div id="J_commt_list">
                    </div>
                    <div style="display:none" class="commt_more" id="J_commt_more"><a onclick="return false;" href="javascript:;">显示更多评论</a></div>
                </div>
            </div>
            <div class="white_bottom"></div>
        </section>
    </section>
    <aside class="main_content_right">
        <section class="hotcomments">
            <header class="yellow_bar"><h4>最热评论</h4></header>
            <ul class="yellow_body_v2" id="J_hotcommt_list">
                <li style="border:none !important"><div class="comContent"><button class='tips_loading' style='width:30px;height:16px;'></button>努力加载中...</div></li>
            </ul>
            <div class="yellow_bottom_v2"></div>
        </section>
        <section style="text-align:center;">
        <!--right1 start--><!-- 广告位：CBV4-300x250 文章页右 #1 -->
<div class="cbv">
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "693418";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script>
</div><!--right1 end-->
        </section>
        <script language="javascript" src="/home/rank/show.htm?t=20131028"></script>
        <section style="text-align:center;">
        <!--right2 start--><!-- 广告位：文章页右下 300x250 #1 -->
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "577466";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script><!--right2 end-->
        </section>
                <section style="text-align:center;" id="fixed_body">
        <!--rightfloat start--><center>
<script type="text/javascript"><!--
google_ad_client = "ca-pub-9066977823953139";
/* 文章页右300x250 * /
google_ad_slot = "0948614537";
google_ad_width = 300;
google_ad_height = 250;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script><br><br><script type="text/javascript">
     document.write('<a style="display:none!important" id="tanx-a-mm_10007624_104555_13330265"></a>');
     tanx_s = document.createElement("script");
     tanx_s.type = "text/javascript";
     tanx_s.charset = "gbk";
     tanx_s.id = "tanx-s-mm_10007624_104555_13330265";
     tanx_s.async = true;
     tanx_s.src = "http://p.tanx.com/ex?i=mm_10007624_104555_13330265";
     tanx_h = document.getElementsByTagName("head")[0];
     if(tanx_h)tanx_h.insertBefore(tanx_s,tanx_h.firstChild);
</script><!--rightfloat end-->
        </section>
        <section id="fixed_area"></section>
    </aside>
    <div class="clear"></div>
</section>
<section style="text-align:left;">
    <!--bottombanner start--><script async src="http://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<ins class="adsbygoogle"
     style="display:inline-block;width:970px;height:90px"
     data-ad-client="ca-pub-9066977823953139"
     data-ad-slot="9606315813"></ins>
<script>
(adsbygoogle = window.adsbygoogle || []).push({});
</script><!--bottombanner end-->
</section>
<!--ADD 弹出评论框-->
<div id="pop_reply" class="popup_area" style="display:none;z-index:88;">
    <div class="hd">
        <span id="pop_tip" class="fontsline">还能输入<strong>140</strong>字</span>
        <div class="close" onclick="return $(this).parent().parent().hide()"><a id="pop_reply_close" href="javascript:void(0)">关闭按钮</a></div>
    </div>
    <div class="popup_textarea">
        <div class="popup_textarea_input">
            <input type="hidden" id="pop_tid" name="pop_tid" value="" />
            <textarea id="pop_content" name="popup_content" class="popup_textarea_input" placeholder="说点什么"></textarea>
        </div>
        <div class="popup_textarea_tool clearfix">
                        <a href="javascript:void(0)" class="submit_btn" id="pop_submit">立即回复</a>
            <span class="seccode_box">验证码：<input id="pop_seccode" name="seccode" class="seccode" type="text" title="请输入验证码" align="absmiddle" /><img id="popup_seccode" title="刷新验证码" style="cursor:pointer;vertical-align:middle;" imgshow="0" src="" alt="" /></span>
                    </div>
    </div>
</div>
<!--ADD-->
<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=1827086" ></script>
<script type="text/javascript" id="bdshell_js"></script>

	<footer class="copyright">
        <p>除非特别注明，本站所有文章均不代表本站观点。报道中出现的商标属于其合法持有人。<strong>请遵守理性，宽容，换位思考的原则。</strong></p>
		<p>合作媒体与供稿人：
                            <a href="http://thewind.hk/"   >TheWind</a>
                                            &nbsp;<a href="http://www.apppoo.com/"   >安卓应用谱</a>
                                            &nbsp;<a href="http://weibo.com/ictech"   >C科技</a>
                                            &nbsp;<a href="http://news.mydrivers.com/"   >驱动之家</a>
                                            &nbsp;<a href="http://www.bio360.net/"   >生物360</a>
                                            &nbsp;<a href="http://google.org.cn/"   >谷奥</a>
                                            &nbsp;<a href="http://www.weiphone.com/"   >iPhone威锋网</a>
                                            &nbsp;<a href="http://www.huxiu.com/"   >虎嗅</a>
                                            &nbsp;<a href="http://www.yseeker.com/"   >品味雅虎</a>
                <br />                            &nbsp;<a href="http://www.ownlinux.org/"   >Ownlinux</a>
                                            &nbsp;<a href="http://www.leica.org.cn/"   >中文摄影杂志</a>
                                            &nbsp;<a href="http://www.geekpark.net/"   >极客公园</a>
                                            &nbsp;<a href="http://www.macx.cn/"   >MacX</a>
                                    </p>
        <p>本站由<a href="http://www.verycloud.cn/" target="_blank">VeryCloud</a>云分发提供CDN加速
            <span class="sep">|</span> 部分节点提供: <a href="http://www.whozen.com/" target="_blank">互城网络</a>
            <span class="sep">|</span><a href="http://www.gigelayer.com/" target="_blank">浙岭云擎</a>
            <span class="sep">|</span><a href="http://www.hypo.cn/" target="_blank">海波网络</a>
            <span class="sep">|</span><a href="http://www.wy.com.cn/cn2.aspx" target="_blank">唯一网络CN2</a>
        </p>
        <p>&copy;2003-2013 cnBeta <a href="http://www.miibeian.gov.cn" target="_blank">浙ICP备11027646号</a>&nbsp;
        <a href="/about/index.htm" target="_blank">关于我们</a>&nbsp;
        <a href="/about/cooperation.htm" target="_blank">广告招租</a>&nbsp;
        <a href="mailto:ugmbbc@gmail.com" target="_blank">报告不适当内容</a></p>
	    <span class="powered">Powered by CB-TEAM | generate by ceallan</span>
	</footer>
    <div class="clear"></div>
    <!--stat start--><script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?4216c57ef1855492a9281acd553f8a6e";
  var s = document.getElementsByTagName("script")[0];
  s.parentNode.insertBefore(hm, s);
})();
</script>
<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-435607-2";
urchinTracker();
</script><!--stat end-->
    <!--返回顶部-->
    <div class="goToTop" id="backToTop"><a rel="nofollow" href="javascript:void(0)" class="toTop"></a></div>
</div>
<script charset="utf-8" type="text/javascript">
//<![CDATA[
CB.use('jquery','common',function(){
    $("#qzone_follow").attr("src", GV.URL.QZONE);
    $("#sina_follow").attr("src", GV.URL.SINA);
});
//]]>
</script>
<script charset="utf-8" type="text/javascript">
//<![CDATA[
GV.COMMENTS = {CMNTDICT:{},CMNTLIST:{},HOTLIST:{},PAGE:1,MORENUM:100,SHOWNUM:0,MOREPAGE:1,POSTED:0,CLICKED:0};
GV.DETAIL = {SID:"258098",POST_URL:"/comment",POST_VIEW_URL:"/cmt",SN:"11f1a"};
CB.use('cbcode','lazyload','article','fancybox',function(){
	$.cbPublish(".post_commt");
    if($(".content").length > 0){
        $(".content img").lazyload({effect:"fadeIn"});
    }
    $("#top_login_link").fancybox({
        padding:0,
        content: CB.tmpl(popLoginTp,{}),
        helpers:{
            overlay:{closeClick:false}
        },
        afterShow:function(){if($('.connections').length){$('.connections').find('a').click(windowOpen);}}
	});
});
var bds_config={"snsKey":{'tsina':'696316965','tqq':'a75e1c5904c842b7','t163':'','tsohu':''}}
document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000);
// BFD fetch
window["_BFD"] = window["_BFD"] || {};
_BFD.BFD_INFO = {"page_type" : "detail"};
//]]>
</script>
<!-- BFD -->
<script type="text/javascript">
window["_BFD"] = window["_BFD"] || {};
_BFD.client_id = "Czhongwenyejiezx";
_BFD.script = document.createElement("script");
_BFD.script.type = "text/javascript";
_BFD.script.async = true;
_BFD.script.charset = "utf-8";
_BFD.script.src = (('https:' == document.location.protocol?'https://ssl-static1':'http://static1')+'.baifendian.com/service/zhongwenyejiezx/zhongwenyejiezx.js');
document.getElementsByTagName("head")[0].appendChild(_BFD.script);
(function(win,doc){
    var s = doc.createElement("script"), h = doc.getElementsByTagName("head")[0];
    if (!win.alimamatk_show) {
        s.charset = "utf8";
        s.async = true;
        s.src = "http://a.alimama.cn/tkapi.js";
        h.insertBefore(s, h.firstChild);
    };
    var o = {
        pid: "mm_10007624_104555_13572417",/*推广单元ID，用于区分不同的推广渠道* /
        appkey: "",/*通过TOP平台申请的appkey，设置后引导成交会关联appkey* /
        unid: ""/*自定义统计字段* /
    };
    win.alimamatk_onload = win.alimamatk_onload || [];
    win.alimamatk_onload.push(o);
})(window,document);
</script>
<script type="text/javascript">
/ * <![CDATA[ * /
jQuery(function($) {

$(document).on('click', '#seccode', function(){
	$.ajax({
		url: "\/captcha.htm?refresh=1",
		dataType: 'json',
		cache: false,
		success: function(data) {
			$('#seccode').attr('src', data['url']);
			$('body').data('captcha.hash', [data['hash1'], data['hash2']]);
		}
	});
	return false;
});


$(document).on('click', '#popup_seccode', function(){
	$.ajax({
		url: "\/captcha.htm?refresh=1",
		dataType: 'json',
		cache: false,
		success: function(data) {
			$('#popup_seccode').attr('src', data['url']);
			$('body').data('captcha.hash', [data['hash1'], data['hash2']]);
		}
	});
	return false;
});

});
/ * ]]>* /
</script>
</body>
</html>
*/