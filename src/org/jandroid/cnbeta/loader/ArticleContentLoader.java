package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.CnBetaApplication;
import org.jandroid.cnbeta.Constants;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.entity.RankArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentLoader extends AbstractLoader<Content> {

    private static String URL_TEMPLATE = Constants.BASE_URL + "/articles/{1}"+".htm";
    
    private int sid;

    public ArticleContentLoader(int sid) {
        this.sid = sid;
    }

    public int getSid() {
        return sid;
    }

    @Override
    public Content fromHttp() throws Exception {
        String url = getURL();
        String responseHTML = CnBetaHttpClient.getInstance().httpGet(url);
        return parsePage(responseHTML);
    }

    @Override
    public Content fromDisk(File baseDir) throws Exception {
        return null;
    }

    @Override
    public void toDisk(File baseDir, Content content) throws Exception {

    }

    private String getURL(){
        return MessageFormat.format(URL_TEMPLATE, "" + getSid());
    }

    private Content parsePage(String responseHTML){
        
        Document document = Jsoup.parse(responseHTML, "utf-8");
        return null;
    }

}


/*

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" >
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="http://static.cnbetacdn.com/assets/js/jquery.js"></script>
<title>索尼Z Ultra评测：可能永远不会买的最佳手机_SONY 索尼_cnBeta.COM</title>
<meta http-equiv="content-language" content="zh-cn" />
<meta http-equiv="imagetoolbar" content="false" />
<meta name="keywords" content="SONY 索尼,索尼Z Ultra评测：可能永远不会买的最佳手机,cnBeta" />
<meta name="description" content="8月20日消息，在5英寸智能手机已经相当普遍的今天，不知道有多少人能够回忆起在2010年看到同为5英寸的戴尔Streak 5时那种惊讶的感觉。而面对6.44英寸的索尼Xperia Z Ultra，这种感觉似乎又回来了。这款设备是不是和Streak 5一样生不逢时？6.44英寸的超大屏幕会不会对握持造成负担？除了巨屏之外它又有哪些闪光点？" />
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
    TOKEN: 'ae14639495b0ae0c848e2adaefc0f31db276167a',
    URL: {
        QZONE:"http://open.qzone.qq.com/like?url=http%3A%2F%2Fuser.qzone.qq.com%2F706290416&width=120&height=22&type=button_num",
        SINA:"http://widget.weibo.com/relationship/followbutton.php?language=zh_cn&width=136&height=22&uid=2769378403&style=2&btn=red&dpc=1",
        INDEX:{ALLNEWS: "/more.htm"},
        ARTICLE:{},
        TOPIC:{},
        ARGUE:{},
        LOGIN:"/user/front.htm",
        LOGOUT:"/user/home/logout.htm"
    }
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
<div class="dig_btn"><a title="顶一个" class="dig_link" onclick="return false;" href="javascript:;">我顶</a></div>
<section class="main_content">
    <section class="main_content_left">
        <article class="cb_box article">
            <div class="body">
                <header>
                    <h2 id="news_title">索尼Z Ultra评测：可能永远不会买的最佳手机</h2>
                    <a alt="197" id="sign"></a>
                    <div class="title_bar">
                        <span class="date">2013-08-20 14:45:47</span>
                        <span class="counter"><span id="view_num"></span> 次阅读</span>
                        <span class="where">
                            稿源：<a href="http://digi.it.sohu.com/">搜狐数码</a></span>
                        <span class="discuss"><a href="#comment"><font style="font-size:12px;color:#c00;" id="comment_num"></font></a> 条评论</span>
                    </div>
                </header>
                <div class="content">
                    <div class="introduction">
                                                <div style="float:right;margin-top:12px;"><a href="/topics/197.htm" target="_blank"><img title="SONY 索尼" src="http://static.cnbetacdn.com/topics/2010-4-9 9-32-51.gif" /></a></div>
                                                <p><strong>8月20日消息，在5英寸智能手机已经相当普遍的今天，不知道有多少人能够回忆起在2010年看到同为5英寸的戴尔Streak 5时那种惊讶的感觉。而面对6.44英寸的索尼Xperia Z Ultra，这种感觉似乎又回来了。</strong>这款设备是不是和Streak 5一样生不逢时？6.44英寸的超大屏幕会不会对握持造成负担？除了巨屏之外它又有哪些闪光点？</p>                    </div>
                    <div class="content"><p>瘾科技最新发布的评测当中包含了这些问题的答案：</p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/36_1376980958.jpg_w600.jpg" title="2.jpg"/></p><p><strong>设计</strong></p><p><a data-type="2" data-keyword="索尼" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">索尼</a>Xperia Z Ultra是我们所评测过的最大的一款智能手机。在屏幕尺寸上，也只有6.3英寸的<a data-type="2" data-keyword="三星" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">三星</a><a data-type="2" data-keyword="Galaxy" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">Galaxy</a> Mega与之相近。但从总体尺寸和重量上看，索尼的这款设备无疑是真正的冠军。Z Ultra的机身规格为179.4 x 92.2 x 6.5毫米，比起Mega要长出11.8毫米，宽4.2毫米，机身厚度要少1.5毫米。Z Ultra同时也是我们把玩过的最重的手机之一，其机身重量达到了212克，比起Mega要高出13克。考虑到索尼更精良的用料和设备本身更大的尺寸，这并不让我们感到惊讶。有意思的是，Z Ultra甚至比Kindle Paperwhite还要长，两款设定的重量也几乎相同。这款手机的宽度和一本护照相当，首席设计师胜沼润也向我们证实这是故意为之，为的是使设备的尺寸近似于某样<a data-type="2" data-keyword="旅行" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">旅行</a>者会一直带在自己上衣口袋里的东西。</p><p>说完了这些抽象的数字，这款手机在实际的使用中又感觉如何呢？和你预料到的一样，两个字：笨拙。Z Ultra的尺寸介于舒适的智能手机和小<a data-type="2" data-keyword="平板" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">平板</a>之间，我们觉得即便是再大的手掌也无法驾驭得了它。虽然屏幕的长宽比为16：9，但上下边框都非常厚。机身方正平整的边角又进一步加深了这份不自然的感觉。Ultra纤薄的机身（坦白讲有点太薄了）能够帮助对机身的抓握，但手指仍然需要尽力伸展，单手使用更是需要比以往更多的练习。双手使用自然是没什么问题，但在使用的过程中，我们更想要去以横屏而非竖屏的模式来使用这部设备——换句话说，Z Ultra感觉上依然更像是一部小平板（比如Nexus 7）而非一部智能手机。虽然理论上能被放进裤子口袋，但可能不会太舒服（太长、太宽）。</p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/97_1376980959.jpg_w600.jpg" title="3.jpg"/></p><p>Z Ultra的正反两面都采用了钢化玻璃面板，外型时尚美观。但与此同时，这些防碎防刮伤的表面也成了指纹收集器，同时让机身变得很滑，难以握持。不过好在索尼的设计师们保留了机身的金属边缘，如果你总是拿不稳手机（特别是这一部），那么可以说是个救命的设计。</p><p><strong>显示屏</strong></p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/5_1376980960.jpg_w600.jpg" title="4.jpg"/></p><p>在索尼今年早些时候推出的Xperia Z身上，显示效果是外界所猛烈吐槽的一个地方。虽然像素密度颇高，但Xperia Z的显示屏亮度低、可视角度有限、户外可见度一般——可以说是市面上最差劲的1080p面板之一。但是，Z Ultra的6.44英寸Triluminos显示屏获得了急需的提升，同时可能也代表了索尼未来更小尺寸旗舰所具备的水平。</p><p>Triluminos最早是应用于索尼电视产品的一种显示技术，但由于成本过高，在2009年时已经被放弃。而这项技术在索尼手机身上的延续让人既意外又期待。简单来讲，Triluminos是一种RGB LED技术，采用的是QD Display的量子点——一种能够发射出特定波长光线的纳米粒子，一个点的大小都在2-10纳米之间。采用该技术的显示屏并不会使用白色的背光来穿过RGB滤波器以制造出需要的色彩，而是使用蓝色LCD来刺激量子点发出纯绿和纯红的色彩。也就是说，这项技术应该能够创造出异常纯粹的色彩。</p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/68_1376980961.jpg_w600.jpg" title="5.jpg"/></p><p>Ultra的屏幕分辨率为1920x1080，因此像素密度为344ppi。虽然没有Xperia Z（441ppi）多，但丝毫没有影响到它的显示效果。Ultra的LCD显示屏在亮度上要比Xperia Z高出非常多，同时色彩也更为自然，也不会出现AMOLED面板的过饱和情况。与此同时，这块显示屏的可视角度绝佳，在阳光直射下的可视程度也是当下数一数二的。</p><p>喜欢手写<a data-type="2" data-keyword="笔" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">笔</a>的用户可要注意的，Z Ultra并不附带任何形式的电容笔（同时也不支持三星的S Pen）。但是，它的屏幕上被加入了名为ASF的超硬涂层，能够支持或者说是经受钢笔/铅笔的直接输入。没错，这也是一项面向需要经常出差的商务人士所推出的功能。在我们使用一根圆珠笔进行测试后发现，这项功能工作情况正常，但触控传感器显然无法探测到压力，因此也就不会出现任何的笔锋效果。与此同时，在书写时有可能会误触到机身侧面的几个按键，这会对用户的操作造成影响。</p><p><strong><a data-type="2" data-keyword="相机" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">相机</a></strong></p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/27_1376980962.jpg_w600.jpg" title="6.jpg"/></p><p>相信大家都不是特别喜欢用平板来拍照，大多数的厂商也没有在这方面花太大功夫。由于Z Ultra是最接近平板/手机的混合设备，它的拍照能力会向哪一边靠拢呢？Z Ultra采用了一枚老式的800万像素Exmor R感光元件，且没有<a data-type="2" data-keyword="闪光灯" data-rd="1" data-style="1" data-tmpl="628x270" target="_blank">闪光灯</a>。所以事实显而易见：索尼并不想让Ultra成为一部拍照旗舰，但也不希望把这项功能做的太差。</p><p>在户外表现上，Z Ultra的色彩饱和度相对不错，但细节不够丰富，对焦也显得有点虚，图中高光区域和阴影的混合也不太好，不过HDR一般能够解决最后这个问题。</p><p>在弱光环境的处理上，这款手机做的也不太好，但选用不同的拍摄模式会带来不同的效果。普通模式下的照片会很暗；如果换成夜景模式，虽然进光量会增加，但图像会变得模糊；而在Superior自动模式下，虽然模糊不见了，但噪点会增多。</p><p><strong>性能和续航</strong></p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/75_1376980962.jpg_w600.jpg" title="11.jpg"/></p><p>Z Ultra不仅是我们见过的最大的一部手机，同时从理论上讲也是性能最强的，因为它采用了2.15GHz的高通MSM8974四核处理器，也就是骁龙800。此外，手机的<a data-type="2" data-keyword="内存" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">内存</a>为2GB。</p><p>毫无疑问，骁龙800能够带来令人印象深刻的性能效果：手机能够流畅地处理所有的操作，即便它们都在同时运行。在看过跑分之后，我们发现索尼的定制UI有点拖累到系统性能，因为在<a data-type="2" data-keyword="CPU" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">CPU</a>测试当中，Z Ultra所获得的分数不及同样采用骁龙800处理器的高通开发设备。虽是如此，Ultra还是超过了我们在JavaScript性能上的预期，其SunSpider浏览器分数也是智能手机当中最低的（越低越好）。说到浏览器性能，设备的Vellamo成绩和高通开发设备相当。</p><p>由于是首款采用骁龙800的设备，Z Ultra自然也首当其冲享受到了Adreno 330 GPU所带来的益处。据高通声称，这款GPU对比上代型号（Adreno 320）在图形性能上提升了50%。</p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/45_1376981075.jpg" title="QQ图片20130820143945.jpg"/></p><p>续航方面，Z Ultra采用了一块容量为3050mAH的<a data-type="2" data-keyword="电池" data-rd="1" data-style="1" data-tmpl="628x270" target="_blank">电池</a>，虽然容量颇大，但实际的表现却没有达到与电池容量相符的水平。但请不要理解错了，Z Ultra有能力应对一天的繁忙使用，在我们的测试当中，这款设备在50%屏幕亮度下一般能够坚持14-15小时。但是如果把亮度调高，或是工作负荷升高，其续航就只有10小时左右了。虽说这样的续航水平已经足够好了，但对于一块拥有大容量电池的手机来说并不是个出色的成绩，也比不上Galaxy Note II或者是<a data-type="2" data-keyword="摩托罗拉" data-rd="1" data-style="1" data-tmpl="290x380" target="_blank">摩托罗拉</a>DROID Maxx系列。</p><p>在我们的标准续航测试当中——55%屏幕亮度下持续浏览网页，直到手机电量达到15%——Z Ultra坚持了4小时8分钟。对比之下，Galaxy Note II的成绩是5：28，Xperia Z为3：31，Galaxy S4的成绩是3：35。</p><p><strong>总结</strong></p><p style="text-align:center"><img src="http://static.cnbetacdn.com/newsimg/2013/0820/82_1376980963.jpg_w600.jpg" title="dsc06690.jpg"/></p><p>和Galaxy Mega一样，你是否决定购买Xperia Z Ultra主要还是取决于它的尺寸。这款手机并没有提供任何独特的地方让你决定去忍受它过大的机身，高达675美元（约合人民币4130）的价格也会吓跑不少持观望态度的消费者。</p><p>但如果你非常偏爱于巨屏手机，Z Ultra绝对是6英寸+类别当中的最佳选择。它拥有时髦靓丽的外形设计，显示效果出色的屏幕，还有最顶尖的性能。简而言之，你用来决定是否购买这款手机的时间应该不会太长，要么喜欢要么讨厌，但不会一直保持中立。（Eskimo）</p></div>
                    <div class="clear"></div>
                    <div id="googleAd_afc">
                    <!-- google custom begin-->
                    <script charset="utf-8" type="text/javascript">
                        function google_ad_request_done(google_ads) {
                            var s = "";
                            var i;
                            if (google_ads.length == 0) return;
                            s += '<div class="gbox830"><div class="gtitle"><a href=\"' + google_info.feedback_url  + '\" target="_blank">Google提供的广告</a></div>'
                            if (google_ads.length == 1) {
                                s += '<div class="tbox1">' +
                                '<a href="' + google_ads[0].url + '" target="_blank">' + google_ads[0].line1 + '</a><br />' +
                                google_ads[0].line2 + "&nbsp;" + google_ads[0].line3 + '<br />' +
                                '<a href="' + google_ads[0].url + '" target="_blank" class="gaAddress">' + google_ads[0].visible_url + '</a>' +
                                '</div>';

                                }
                            else if (google_ads.length > 1) {

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
                </div>
                <div class="clearfix"></div>
                <section style="text-align:center;padding:0px 10px;"">
                    <!--left1 start--><div class="cbv">
<p><script type="text/javascript" >BAIDU_CLB_SLOT_ID = "611927";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script></p>
<p>
<!-- 广告位：CBV4-640x60 文章页文末 #1 -->
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "700021";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script></p>
<p><script type="text/javascript">/*cnBeta文末580*90* / var cpro_id = 'u792078';</script>
<script src="http://cpro.baidu.com/cpro/ui/c.js" type="text/javascript"></script></p>
</div><!--left1 end-->
</section>
<div class="clearfix" style="margin:0 10px;">
<div class="fl"><span class="author">[责任编辑：teikaei ]</span></div>
<div class="fr">
<!-- Baidu Button BEGIN -->
<div id="bdshare" class="bdshare_b" style="line-height: 12px;">
<img src="http://bdimg.share.baidu.com/static/images/type-button-3.jpg?cdnversion=20120831" />
<a class="shareCount"></a>
</div>
<!-- Baidu Button END -->
</div>
</div>
<div class="rating_box clearfix">
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
<div class="hd"><h3>顺序阅读</h3></div>
</div>
<div class="bd">
<div class="bd_r fr"><a target = "_blank" href = "http://www.wy.cn/">唯一网络 wy.cn 专业服务器租用托管<br>
机柜大带宽 高防服务器</a></div>
<div class="bd_l">
<div>上一篇：<a href="/articles/249279.htm">[图]comScore：2013年7月美搜索引擎排名 谷歌微增 微软持平</a>                            </div>
<div>下一篇：<a href="/articles/249281.htm">五款Windows Phone新设备的代号曝光</a>                            </div>
</div>
</div>
</div>
</div>
</article>
<section style="text-align:center;">
<!--left2 start--><script type="text/javascript">
/*662x88 Metro广告测试* /
        var cpro_id = "u1321009";
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/c.js" type="text/javascript"></script><!--left2 end-->
</section>
<section>
<nav class="blue_bar">
<h4>网友评论</h4>
<div class="post_count" style="display:none"></div>
</nav>
<div class="content_body">
<a name="comment"></a>
<div class="post_commt">
<div class="commt_l">
<input type="hidden" name="tid" value="">
<input type="hidden" name="sid" value="249280">
<input type="hidden" name="nowsubject" value="Re:索尼Z Ultra评测：可能永远不会买的最佳手机" />
<div class="hd">
<span id="top_reply_logout" class="login" >
<img src="http://static.cnbetacdn.com/assets/images/user/face.png">
<a id="top_login_link" href="/user/front.htm" data-fancybox-type="iframe" class="logoA"><strong>登录</strong><div class="nav_new"></div></a>
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
<script type="text/javascript" >BAIDU_CLB_SLOT_ID = "693418";</script>
<script type="text/javascript" src="http://cbjs.baidu.com/js/o.js"></script><!--right1 end-->
</section>
<script language="javascript" src="/home/rank/show.htm?t=20130820"></script>
<section style="text-align:center;" id="fixed_body">
<!--right2 start--><center>
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
</script><!--right2 end-->
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
        &nbsp;<a href="http://www.linuxeden.com/"   >Linux伊甸园</a>
        &nbsp;<a href="http://www.huxiu.com/"   >虎嗅</a>
        &nbsp;<a href="http://www.yseeker.com/"   >品味雅虎</a>
        &nbsp;<a href="http://www.ownlinux.org/"   >Ownlinux</a>
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
<i rel="nofollow" role="button" id="back_top" tabindex="-1">返回顶部</i>
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
GV.DETAIL = {SID:"249280",POST_URL:"/comment",POST_VIEW_URL:"/cmt",SN:"0e6b3"};
CB.use('article','fancybox',function(){
        $.cbRate(".rating_box");
$.cbDig(".dig_btn");
$("#top_login_link").fancybox({
        maxWidth	: 580,
        maxHeight	: 380,
        fitToView	: false,
        width		: '70%',
        height		: '70%',
        autoSize	: true,
        closeClick	: false,
        openEffect	: 'none',
        closeEffect	: 'none'
        });
$.cbPublish(".post_commt");
$.cmtOnload(".commt_list");

fixed_top = $("#fixed_area").offset().top;
$(window).scroll(function(){
        var scrolla=$(window).scrollTop();
var dis=parseInt(fixed_top)-parseInt( scrolla);
if(dis<=0)
        {
        $("#fixed_body").removeClass().addClass("fixed_right");
//l=false;
}
        if(dis>0)
        {
        $("#fixed_body").removeClass("fixed_right");
//l=true;
}

        var o = $('#J_commt_more');
if(o!=null ){
        var bottom = o.offset().top + o.outerHeight(),
        scrollTop = document.documentElement.scrollTop || document.body.scrollTop || 0,
        windowHeight = document.documentElement.clientHeight || document.body.clientHeight || 0;
if (scrollTop >= bottom - windowHeight && GV.COMMENTS.POSTED == 0 && GV.COMMENTS.MOREPAGE > 0 && GV.COMMENTS.CLICKED == 0) {
        o.click();
}
        }
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
</script>
<!--<script type="text/javascript">(function(d,t){var url="/user/front";var g=d.createElement(t);g.async=1;g.src=url;d.body.insertBefore(g,d.body.firstChild);}(document,"script"));</script>-->
<script type="text/javascript">
/*<![CDATA[* /
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
/*]]>* /
</script>
</body>
</html>

*/