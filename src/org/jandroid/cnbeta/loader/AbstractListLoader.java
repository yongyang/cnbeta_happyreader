package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用more.htm来获取文章列表的基类
 *
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractListLoader<T> extends AbstractLoader<List<T>> {

    private static final String digits = "0123456789";

    //http://www.cnbeta.com/more.htm?jsoncallback=jQuery18007005875239260606_1373612093876&type=realtime&sid=244461&_=1373612267852
    //jsoncallback算法
    public static final String URL_TEMPLATE = "http://www.cnbeta.com/more.htm?jsoncallback={0}&type={1}&page={2}&_={3}";

    private Type type;
    private int page;

    public static enum Type {
        ALL("all"),
        REALTIME("realtime"),
        DIG("dig"),
        SOFT("soft"),
        INDUSTRY("industry"),
        INTERACT("interact"),
        EDITOR_COMMEND("editorcommend"),
        HOT_COMMENT("jhcomment");
        private String type;

        private Type(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }
    }

    public AbstractListLoader(Type type, int page) {
        this.type = type;
        this.page = page;
    }

    public Type getType() {
        return type;
    }

    public int getPage() {
        return page;
    }

    protected String getURL(){
        return MessageFormat.format(URL_TEMPLATE, randomJQueryCallback(), getType().getTypeString(), ""+getPage(), ""+(System.currentTimeMillis() + 1));
    }

    @Override
    public List<T> fromHttp(File baseDir) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        //TODO: clear cache if page=1, reload
        String url = getURL();
        //user json-simple to parse returned json string
        String response = CnBetaHttpClient.getInstance().httpGet(url, headers);

        JSONArray articleListJSONArray = getJSONArray(response);
        List<T> articles = parseJSONArray(articleListJSONArray);
        //parse成功才存盘
        writeDisk(baseDir, articleListJSONArray.toJSONString());
        return articles;
    }

    protected JSONArray getJSONArray(String response){
        String responseJSONString = response.substring(response.indexOf('(') + 1, response.lastIndexOf(')'));
        JSONObject responseJSON = (JSONObject)JSONValue.parse(responseJSONString);
        JSONArray articleListJSONArray;

        // 返回的JSON结构偶尔会不一样
        Object result = responseJSON.get("result");
        if(result instanceof JSONArray){
            articleListJSONArray = (JSONArray)result;
        }
        else {
            articleListJSONArray = (JSONArray)((JSONObject)responseJSON.get("result")).get("list");
        }
        return articleListJSONArray;
    }

    protected List<T> parseJSONArray(JSONArray articleListJSONArray){
        List<T> articleList = new ArrayList<T>(articleListJSONArray.size());
        for(int i=0; i<articleListJSONArray.size(); i++){
            JSONObject jsonObject = (JSONObject)articleListJSONArray.get(i);
            T article = newEntity(jsonObject);
            articleList.add(article);
        }
        return articleList;
    }

    protected abstract T newEntity(JSONObject jSONObject);

    @Override
    public List<T> fromDisk(File baseDir) throws Exception {
        //read json file from SD Card
        String articleListJSONString = readDisk(baseDir);
        JSONArray articleListJSONArray = (JSONArray)JSONValue.parse(articleListJSONString);
        return parseJSONArray(articleListJSONArray);
    }

    @Override
    public String getFileName() {
        return "" + getType().getTypeString() + "_" + getPage();
    }

    // generate random callback seed as JQuery
    protected  String randomJQueryCallback() {
        StringBuilder sb = new StringBuilder("jQuery1800");

        for(int i=0; i< "8753548712314047".length(); i++){
            sb.append(digits.charAt((int)(Math.random() * digits.length())));
        }

        sb.append("_");
        sb.append(Math.round((System.currentTimeMillis() / 15e3)));
        return sb.toString();
    }
}
/*
http://www.cnbeta.com/more.htm?jsoncallback=jQuery18007005875239260606_1373612093876&type=realtime&sid=244461&_=1373612267852

http://www.cnbeta.com/more.htm?jsoncallback=jQuery180028785929968214596_1373611841741&type=all&page=1&_=1373611882757
*/

/*
jQuery18008753548712314047_1374591752967(
{"status":"success","result":
{"list":[
{"sid":"245736","title_show":"金色iPhone5S与廉价iPhone、iPhone5对比照曝光","hometext_show_short":"感谢T客在线的投递前几天微博用户@C科技为我们带来了两张金色iPhone5S的谍照，今天他又放出了一组iPhone5S谍照，其中包括廉价iPhone、iPhone5、金色iPhone5S后...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374586704.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245736.htm","counter":"1959","comments":"5","score":"-20","time":"2013-07-23 21:38:27"},{"sid":"245735","title_show":"苹果新专利：保持通话时让等待用户欣赏照片和音乐","hometext_show_short":"美国专利和商标局本周二通过了一项苹果申请的名为“用户通信设备的保持通话可视菜单”，专利中提到的系统可以让用户在通话中更方便的分享数据。专利中提到的技术为了替代传统“保持通话时发送信息”功能。","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374586633.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245735.htm","counter":"536","comments":"7","score":"0","time":"2013-07-23 21:37:22"},{"sid":"245734","title_show":"三星周五发布第二季度财报 预计将无太大惊喜","hometext_show_short":"全球最大的智能手机制造商三星电子将于周五发布该公司受市场密切关注的第二季度财报。虽然三星电子上一季度的运营利润有望创出历史新高，但是这并不足以安抚旗舰智能手机Galaxy S4出货增速放缓引发的市场顾虑。","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374586321.jpg_180x132.jpg","url_show":"/articles/245734.htm","counter":"260","comments":"3","score":"0","time":"2013-07-23 21:32:01"},{"sid":"245733","title_show":"传Moto X定价将与Nexus 4相同 299美元起","hometext_show_short":"国外媒体称，来自中国的泄露信息，摩托罗拉Moto X新机的售价将和Nexus
4持平，16GB版无合约裸机的价格是299美元（约合人民币1836元），32GB版售价349美元...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374585827.jpg_180x132.jpg","url_show":"/articles/245733.htm","counter":"4040","comments":"28","score":"64","time":"2013-07-23 21:23:49"},{"sid":"245732","title_show":"PS4已通过美国联邦委员会审核 文档显示CPU主频2.75Ghz","hometext_show_short":"索尼PS4已经通过了美国联邦委员会的审核批准。这款即将在今年年底上市的新款游戏机的申请文档中显示，此设备的制造国家包含了中国和日本，这应当也表明索尼期望尽可能减少PS4上市时供货不足的问题出现几率。","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374584646.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245732.htm","counter":"1223","comments":"8","score":"0","time":"2013-07-23 21:04:12"},{"sid":"245730","title_show":"全球最薄又能如何？华为P6视频评测","hometext_show_short":"感谢爱极客的投递有这样一款手机，只有6.18mm全球第一薄，还在架构上领先iPhone 5一年！现如今敢如此叫板苹果的厂商可不多见，哪怕三星诺基亚，也不...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374583478.png_w600.png_180x132.png","url_show":"/articles/245730.htm","counter":"6418","comments":"29","score":"-24","time":"2013-07-23 20:45:08"},{"sid":"245729","title_show":"Quora 精选: iPhone 内置铃声的故事","hometext_show_short":"电话机发明人贝尔选择的电话铃声是线圈控制铁的槌敲打铃体，和80后回忆里的上课铃声一样。时代快进到iPhone兴起， 「马林巴琴（marimba）」当道的年代...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374583277.jpg_180x132.jpg","url_show":"/articles/245729.htm","counter":"2008","comments":"7","score":"5","time":"2013-07-23 20:41:17"},{"sid":"245728","title_show":"Mozilla Firefox 23.0 Beta 8 发布","hometext_show_short":"Firefox 23.0的进展相当迅速，Beta 8已经来到Beta测试目录中，新版开始采用Gecko 23内核，预计正式发布时间2013年8月，Moziila在新版本23中加入了混合内容锁定...","logo":"http://static.cnbetacdn.com/topics/firefox.gif","url_show":"/articles/245728.htm","counter":"1137","comments":"7","score":"15","time":"2013-07-23 20:39:37"},{"sid":"245727","title_show":"Moto X拍照应用UI多图泄露 重在手势操作","hometext_show_short":"虽然不知道各位同学是否已对Moto
X的泄露信息感到厌烦，好在这款手机终于很快就要发布了。而现在，我们还是继续来谈谈泄露信息。国外媒体AndroidPo...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/33_1374581839.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245727.htm","counter":"2702","comments":"8","score":"14","time":"2013-07-23 20:22:19"},{"sid":"245726","title_show":"ATM吞钱银行不急 改口说吐钱银行急了","hometext_show_short":"7月18日晚，安庆市民何先生在该市人民路一家银行的ATM机取款，不料机器只发出数钱声，却不吐钱，最后银行卡也被吞了。何先生拨打银行客服电话，对...","logo":"http://static.cnbetacdn.com/topics/alert.png","url_show":"/articles/245726.htm","counter":"8135","comments":"40","score":"64","time":"2013-07-23 20:21:43"},{"sid":"245725","title_show":"统计：今年一季度全球平均网速首超4Mbps 移动流量翻番","hometext_show_short":"来自美国服务商Akamai的最新发布的季度统计《2013年第一季度互联网现状报告（State of the Internet report of
Q1 2013）》，全球互联网平均连接速度首度超过了...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374580367.png_180x132.png","url_show":"/articles/245725.htm","counter":"918","comments":"7","score":"-20","time":"2013-07-23 19:52:51"},{"sid":"245724","title_show":"明年ARM处理器主频将达到3Ghz","hometext_show_short":"目前市面上绝大部分平板和智能手机都采用了ARM芯片，而这种处理器也随着台积电（TSMC）、格罗方德（GlobalFoundries）等的工艺进步
以及人们对性能的追...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374578405.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245724.htm","counter":"3929","comments":"23","score":"-11","time":"2013-07-23 19:20:05"},{"sid":"245723","title_show":"刷榜公司盗号下载APP “刷榜”灰色产业链难监管","hometext_show_short":"据中国之声《新闻晚高峰》报道，最近，多位网友通过微博反映，他们在查看App Store的已购项目时发现，除了自己真正下载的应用外，还充斥着大量从未...","logo":"http://static.cnbetacdn.com/topics/view.gif","url_show":"/articles/245723.htm","counter":"1127","comments":"6","score":"-5","time":"2013-07-23 19:03:25"},{"sid":"245722","title_show":"苹果新专利：双传感器成像让iPhone拍照效果更好","hometext_show_short":"本周二美国专利和商标局通过了一项苹果申请的名为“双图像传感器的图像处理系统和方法”，该专利描述了一种可以从两个或更多图像传感器上隔行扫...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374577037.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245722.htm","counter":"2102","comments":"8","score":"1","time":"2013-07-23 18:57:22"},{"sid":"245721","title_show":"苹果庆祝播客服务订阅量破十亿","hometext_show_short":"﻿在微博、移动聊天等产品兴起的同时，一些传统的互联网产品，却越来越寂寞，其中包括音视频订阅服务播客（Podcast），不过几乎陷入独家运营的苹果...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374576803.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245721.htm","counter":"609","comments":"5","score":"5","time":"2013-07-23 18:53:27"},{"sid":"245720","title_show":"谷歌欲850万美元和解3年前一起集体诉讼","hometext_show_short":"据周五提交的一份法庭文件显示，针对2010年一起指控谷歌与第三方共享互联网搜索用户的查询和个人信息的集体诉讼案，谷歌已经同意和解。根据和解协...","logo":"http://static.cnbetacdn.com/topics/2011-8-17 22-31-10.gif","url_show":"/articles/245720.htm","counter":"642","comments":"13","score":"0","time":"2013-07-23 18:51:19"},{"sid":"245719","title_show":"技术人员解释致命的山寨iPhone充电器","hometext_show_short":"美国一名硬件工程师肯·谢里夫(Ken Shirriff)本周在个人博客中撰文，对假冒伪劣充电器是否会引起手机用户触电进行了探讨。他认为，在不符合绝缘规范的情况下，触电确实有可能发生。以下为文章主要内容：","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374576560.png_180x132.png","url_show":"/articles/245719.htm","counter":"4236","comments":"15","score":"4","time":"2013-07-23 18:49:34"},{"sid":"245718","title_show":"国产大飞机发动机9月开测 明年有望首飞","hometext_show_short":"C919客机是近年来我国在民用航空飞行器自主开发制造领域的重量级产品。据最新消息显示，GE航空集团旗下全球最大的航空发动机制造商CFM国际公司透露...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374576481.jpg_180x132.jpg","url_show":"/articles/245718.htm","counter":"4825","comments":"34","score":"26","time":"2013-07-23 18:48:01"},{"sid":"245717","title_show":"苹果将发布第三财季业绩 各方分析师预测重要数据","hometext_show_short":"苹果将于当地时间周二美股收盘后发布其第三财季业绩报告。就此，美国投资银行PiperJaffray分析师基尼•蒙斯特(Gene Munster)提前发布了一份对各方分析师...","logo":"http://static.cnbetacdn.com/topics/apple.png","url_show":"/articles/245717.htm","counter":"633","comments":"4","score":"0","time":"2013-07-23 18:40:41"},{"sid":"245716","title_show":"[视频+多图]诺基亚最大屏手机Lumia 625上手试玩","hometext_show_short":"感谢倚天科技视频网的投递Lumia 625采用4.7英寸480*800分辨率屏幕，支持超敏感触控，并搭载1GHz双核处理器、512MB RAM，支持扩展卡。软件方面，内置“智能...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374575854.jpg_180x132.jpg","url_show":"/articles/245716.htm","counter":"4150","comments":"24","score":"-5","time":"2013-07-23 18:37:38"},{"sid":"245715","title_show":"诺基亚4.7英寸Lumia 625正式发布 官方宣传视频","hometext_show_short":"感谢WP8论坛的投递诺基亚在刚刚结束的英国发布会上正式发布一款4.7英寸的大屏幕新机：Lumia 625。诺基亚Lumia625配置：4.7英寸WVGA屏幕（800*480，201ppi、支持...","logo":"http://static.cnbetacdn.com/topics/nokiapure.gif","url_show":"/articles/245715.htm","counter":"2650","comments":"12","score":"-10","time":"2013-07-23 18:36:42"},{"sid":"245714","title_show":"[图]沿着Yoshida Trail路线 街景团队带你领略富士山优美风景","hometext_show_short":"受到《007：大破天幕杀机》的启发近日Google日本Street View团队克服了重重困难，成功将采集设备Trekker背包带上了海拔3776米高的富士山上，并首次实现了3...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374569863.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245714.htm","counter":"1572","comments":"5","score":"14","time":"2013-07-23 16:57:49"},{"sid":"245713","title_show":"日官员称福岛核电站放射性废水或已泄露入海","hometext_show_short":"距离造成日本东北部海啸的9.0级地震已经过去了2年多的时间，日本东京电力公司(TEPCO)宣布，受辐射污染的地下水似乎已从福岛第一核电站泄露到了太平...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/91_1374568591.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245713.htm","counter":"2713","comments":"15","score":"-9","time":"2013-07-23 17:07:00"},{"sid":"245712","title_show":"小米公司回应：与QQ旗下产品将有重大合作","hometext_show_short":"7月23日消息，今日有媒体发布消息称腾讯将通过DST集团向小米公司投资20亿美金，记者就此事向小米公司市场部求证，对方表示并不知情，称市场部并
没...","logo":"http://static.cnbetacdn.com/topics/11-12-12 09-54-59.gif","url_show":"/articles/245712.htm","counter":"12767","comments":"38","score":"-44","time":"2013-07-23 16:52:51"},{"sid":"245711","title_show":"Ubuntu手机系统会成为第四大手机系统吗","hometext_show_short":"今天凌晨，Ubuntu开发商Canonical发布了代号为Edge的智能手机，与其说是发布，倒不如说是这个项目的成立，因为该项目正式启动需要募集至少3200万美元(30...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374569480.jpg_180x132.jpg","url_show":"/articles/245711.htm","counter":"6505","comments":"29","score":"47","time":"2013-07-23 16:51:20"},{"sid":"245710","title_show":"[视频]诺基亚负责人亲自演示Lumia 625","hometext_show_short":"在经过早上来自爆料大神@evleaks的曝光之后在今天下午四���的发布会上诺基亚正式推出这款4.7英寸的Lumia
625，该机屏幕采用WVGA（480*800）分辨率，像素密...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/32_1374568344.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245710.htm","counter":"2549","comments":"11","score":"-7","time":"2013-07-23 16:37:55"},{"sid":"245708","title_show":"诺基亚宣布推出Lumia 625 将成最便宜的4G WP8手机","hometext_show_short":"据外媒报道，诺基亚刚刚确认了Lumia 620继任者--Lumia 625的存在。相较于最近发布的Lumia 1020, 620的配置显得低了许多--4.7英寸WVGA(480x800)显示屏、1.2GHz骁龙S4处...","logo":"http://static.cnbetacdn.com/topics/nokiapure.gif","url_show":"/articles/245708.htm","counter":"8508","comments":"50","score":"-35","time":"2013-07-23 16:23:49"},{"sid":"245706","title_show":"软银孙正义预言称电脑将在2018年赶上人脑","hometext_show_short":"日媒23日报道，日本软银公司（softbank）社长孙正义在23日召开的法人研讨会上预言称：“电脑将在2018年赶上人脑。”他的这番话也惊动了在场的所有会议参加者。","logo":"http://static.cnbetacdn.com/topics/latest.gif","url_show":"/articles/245706.htm","counter":"2495","comments":"39","score":"-30","time":"2013-07-23 16:02:18"},{"sid":"245705","title_show":"[图]售￥2443 定位年轻人的HTC Desire 500登场","hometext_show_short":"HTC与台湾大哥大 2013 年7月23日发布了中端智能手机Desire 500，产品预计 8 月 1 日开卖；该手机建议售价为 11,900 元（约￥2443），搭配台湾大哥大资费方案月...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374566281.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245705.htm","counter":"7152","comments":"49","score":"-204","time":"2013-07-23 16:08:58"},{"sid":"245704","title_show":"阿里发布TV操作系统 联手华数推出机顶盒产品","hometext_show_short":"今天阿里巴巴集团正式发布阿里智能TV操作系统，并联手华数传媒推出搭载该操作系统的第一代盒子产品。这款产品将于今年9月左右正式上市销售。这也...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374566008.jpg_180x132.jpg","url_show":"/articles/245704.htm","counter":"4949","comments":"11","score":"-35","time":"2013-07-23 15:53:28"},{"sid":"245703","title_show":"[图+视频]PingTime：球桌变舞池 打乒乓也能很电音","hometext_show_short":"电玩游戏的科技似乎把脑筋动到现实生活中了！一台会表演声光秀的乒乓球桌很稀罕吧？约莫 40 年前显示屏幕科技正起步时，Pong 便发明简单的 2D 乒乓球...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/30_1374564541.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245703.htm","counter":"1079","comments":"7","score":"0","time":"2013-07-23 15:50:51"},{"sid":"245702","title_show":"[科普]信息学奥林匹克竞赛是什么","hometext_show_short":"感谢WJMZBMR的投递在《IOI2013中国队喜获四金,历史性包揽金牌前三》这篇文章中，很多网友对信息学奥林匹克竞赛不甚了解，在这里我就给大家详细介绍一下这门竞赛。","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374565211.png_180x132.png","url_show":"/articles/245702.htm","counter":"4216","comments":"39","score":"22","time":"2013-07-23 15:40:13"},{"sid":"245701","title_show":"[视频]PIN码破解机器人将在Defcon上亮相","hometext_show_short":"破解智能手机的四位数PIN码并不难，只要尝试1万种组合就行了，但要用手指一个个按显然不是一种可行的方法，因此黑客创造出了手指形状的机器人，简...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/58_1374564261.jpg_180x132.jpg","url_show":"/articles/245701.htm","counter":"3333","comments":"21","score":"-16","time":"2013-07-23 15:25:38"},{"sid":"245700","title_show":"[图]注重软件发展 三星确定10月27日召开首届私人开发者大会","hometext_show_short":"在短短几年的发展中来自韩国的三星凭借着Android平台迅速成长成为可以和苹果比肩的手机制造商，其出产的Galaxy S系列不断缔造着智能手机的销售奇迹。...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374564257.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245700.htm","counter":"836","comments":"5","score":"0","time":"2013-07-23 15:24:22"},{"sid":"245699","title_show":"[视频]迪士尼演示2D照片生成高清3D模型算法","hometext_show_short":"迪士尼研究院开发出一种算法，可以将2D照片生成高清3D模型。和此前的3D全景技术类似，这项技术也有十多年的历史。但迪士尼的新算法赋予了3D模型更...","logo":"http://static.cnbetacdn.com/topics/3dimg.gif","url_show":"/articles/245699.htm","counter":"2106","comments":"7","score":"31","time":"2013-07-23 15:20:11"},{"sid":"245698","title_show":"传索尼将生产24MP、32MP与36MP三种规格的35MM全画幅元件","hometext_show_short":"索尼去年一口气发布了三款不同设计却全都搭载 35mm 全画幅元件的相机与摄影机产品，而今年也被证实除了 RX1 一体式相机、 Alpha 单眼相机(SLR)与 VG 可换...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/40_1374563552.jpg_180x132.jpg","url_show":"/articles/245698.htm","counter":"2743","comments":"14","score":"38","time":"2013-07-23 15:37:56"},{"sid":"245697","title_show":"新款红米手机获得入网许可，即将上市","hometext_show_short":"感谢MOOC联盟的投递最新消息，小米手机不支持移动3G的历史即将结束，TD-SCDMA版(新款红米手机)已经获得入网许可证，即将上市。以下是来自工信部电信设备进网管理网站的截图：","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374563212.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245697.htm","counter":"6803","comments":"21","score":"-20","time":"2013-07-23 15:06:53"},{"sid":"245696","title_show":"沃尔沃欲2020年将推免碰撞汽车 小猫小狗都能躲","hometext_show_short":"北京时间7月23日消息，据国外媒体报道，每天，全世界发生大量的车祸导致人员伤亡。如今，瑞典沃尔沃公司正在开发各种免碰撞技术，其计划到2020年推...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374563149.jpg_180x132.jpg","url_show":"/articles/245696.htm","counter":"3594","comments":"29","score":"60","time":"2013-07-23 15:05:49"},{"sid":"245695","title_show":"美媒盘点苹果公司新动向 手势操控电视成焦点","hometext_show_short":"据美国《赫芬顿邮报》报道，万众期待的苹果公司可能正在研制一款具有跳过广告、手势操控功能的电视，该产品可以为用户提供更为个性化服务。此外...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374561940.jpg_180x132.jpg","url_show":"/articles/245695.htm","counter":"775","comments":"4","score":"-2","time":"2013-07-23 14:45:40"},{"sid":"245694","title_show":"[图]LG官方网站开始为LG G2倒数 确认8月7日发布","hometext_show_short":"LG移动官方网站已经全面开始为新旗舰手机LG
G2进行重新设计，现在登陆http://www.lgmobile.com/网站将自动跳转到http://www.g2.lgmobile.com
/上。该页面目前只有...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374561876.png_180x132.png","url_show":"/articles/245694.htm","counter":"2304","comments":"9","score":"0","time":"2013-07-23 14:44:37"},{"sid":"245693","title_show":"[观点]Lumia 1020不可能复兴诺基亚","hometext_show_short":"7 月 11 号，诺基亚推出一款 4100 万像素的超薄手机，配备 1/1.5 英寸感光器、6 块蔡司认证镜片、氙气闪光灯、第二代 OIS
光学防抖技术。可谓秒杀市面上...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374561695.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245693.htm","counter":"5844","comments":"65","score":"-422","time":"2013-07-23 14:41:37"},{"sid":"245692","title_show":"[图+视频]无人即暗！智能街灯环保又省电","hometext_show_short":"欧洲的道路很多，但人和车相对较少，部份道路虽然行人和车辆稀疏，但依然要维持街灯照明，以确保道路安全。荷兰设计师Christian Shuh设计了 1 款新的...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/16_1374559702.jpg_180x132.jpg","url_show":"/articles/245692.htm","counter":"2639","comments":"13","score":"35","time":"2013-07-23 14:39:35"},{"sid":"245690","title_show":"苹果down个几天不算啥 微软WP开发者中心每周都会……","hometext_show_short":"这几天苹果的开发者中心宕机，让很多开发者感到非常焦虑。但要和WP开发者中心比起来，苹果就是小巫见大巫了。无论从宕机周期、持续时间、修复速...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374560825.png_w600.png_180x132.png","url_show":"/articles/245690.htm","counter":"6309","comments":"21","score":"0","time":"2013-07-23 14:27:21"},{"sid":"245689","title_show":"[视频]模拟人的素描创作方式 迪斯尼实验室打造素描机器人","hometext_show_short":"随着科学技术的发展计算设备在更多领域不断代替人工，但是这些设备也存在的一定的局限性，并不能在所有方面替代人工，只是说能够尽量模拟人的思...","logo":"http://static.cnbetacdn.com/topics/science.gif","url_show":"/articles/245689.htm","counter":"877","comments":"3","score":"-5","time":"2013-07-23 14:27:04"},{"sid":"245688","title_show":"犹他州电信商曾被要求提供监控互联网活动的权限","hometext_show_short":"据外媒报道，近日，美国犹他州一家小型ISP --XMission的CEO Pete Ashdown透露，他曾在2010年收到来自《外国情报监控法案》法庭的一项指令，要求其为联邦政府提供监控某一名用户互联网活动的权限。","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/53_1374558396.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245688.htm","counter":"741","comments":"0","score":"-15","time":"2013-07-23 13:49:45"},{"sid":"245687","title_show":"三星新款Exynos 5 Octa处理器性能提升20%","hometext_show_short":"继续此前的报道，三星周一公布了新款Exynos 5 Octa八核处理器的详情，除了沿用上代的big.LITTLE架构(4个Cortex A15核心+4个Cortex A7核心)之外，该处理器还搭配...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/96_1374556772.png_180x132.png","url_show":"/articles/245687.htm","counter":"4298","comments":"23","score":"19","time":"2013-07-23 13:40:43"},{"sid":"245686","title_show":"外媒：诺基亚定位失败 错失在中国发展良机","hometext_show_short":"据科技博客网站TechinAsia7月22日报道，诺基亚上周公布的业绩报告显示，第二季度诺基亚在中国仅售出了410万部手机。这只达到了诺基亚2006年中期在中国...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374556908.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245686.htm","counter":"8206","comments":"52","score":"10","time":"2013-07-23 13:21:49"},{"sid":"245685","title_show":"起底李毅吧：吧主敛财年入百万 吧内网络暴力盛行","hometext_show_short":"百度李毅吧，也叫帝吧，有百度卢浮宫之称。李毅吧被认为是百度人气最高，最具内涵的贴吧。“屌丝”“矮矬穷”“高帅富”等词发源于此，引得无数...","logo":"http://static.cnbetacdn.com/topics/view.gif","url_show":"/articles/245685.htm","counter":"17356","comments":"37","score":"93","time":"2013-07-23 13:21:46"},{"sid":"245684","title_show":"山东高考志愿填报系统故障 考生刷屏刷到手软","hometext_show_short":"“要是报不上,可是影响孩子一辈子的事儿!”因牵涉20万考生,22日的高考志愿填报系统故障瞬间成为“大事件”。网络瘫痪的几个小时中,本报新闻热线此...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374556458.jpg_180x132.jpg","url_show":"/articles/245684.htm","counter":"5790","comments":"43","score":"-26","time":"2013-07-23 13:14:19"},{"sid":"245683","title_show":"谷歌总裁被曝妻妾成群:新情妇系越南裔钢��家","hometext_show_short":"施密特,他也是风流成性、情妇众多。21日,英国《星期日邮报》曝光,现年58岁的施密特与多名女子有染,包括电视台主播、金发女公关和越南裔钢琴家等;为...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374556219.jpg_180x132.jpg","url_show":"/articles/245683.htm","counter":"11371","comments":"34","score":"10","time":"2013-07-23 13:10:19"},{"sid":"245682","title_show":"传腾讯将通过DST战略投资小米 总金额超20亿美元","hometext_show_short":"据该知情人士爆料称，小米公司最新一轮融资金额将超越91无线成为科技界最大规模融资，总金额超20亿美元。据一位投行人士向DoNews爆料称，小米公司将...","logo":"http://static.cnbetacdn.com/topics/11-12-12 09-54-59.gif","url_show":"/articles/245682.htm","counter":"8999","comments":"32","score":"-69","time":"2013-07-23 13:09:33"},{"sid":"245681","title_show":"日本移动通讯应用Line宣布用户数突破2亿","hometext_show_short":"日本移动通讯应用Line今日在官网宣布，产品全球用户数于7月21日突破2亿。在六个月的时间内，Line新增了1亿用户。自2011年6月发布以来，Line已经从一个满...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374556002.jpg_180x132.jpg","url_show":"/articles/245681.htm","counter":"3487","comments":"11","score":"20","time":"2013-07-23 13:06:42"},{"sid":"245680","title_show":"[图]Nokia香港正在测试中？Lumia 1020现身香港","hometext_show_short":"採用 Oversampling 影像处理和第二代 Nokia OIS 光学防震的 4100 万像素 Lumia 1020 会在今年第四季登陆香港，而 Nokia 香港似乎亦开始为此机进行内部测试。有读...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374555456.png_180x132.png","url_show":"/articles/245680.htm","counter":"3730","comments":"11","score":"21","time":"2013-07-23 12:57:39"},{"sid":"245679","title_show":"什么才是「黑客」（Hacker）？","hometext_show_short":"我在华盛顿特区工作的时候，常看到「黑客Hacker」一词，但基本上只剩下了一个「黑Hack」字，不止一次在新闻和政府发言稿里，看到对「黑客」一词充满...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/12_1374555288.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245679.htm","counter":"5391","comments":"11","score":"20","time":"2013-07-23 12:56:18"},{"sid":"245678","title_show":"摩托罗拉Moto X相机界面大量截图曝光 支持手势操作","hometext_show_short":"Android Police今天放出大量摩托罗拉Moto X的相机应用界面截图，Moto X采用了非常简约的界面设计，和此前摩托罗拉定制的界面或者Android 4.2、4.3原生相机界面...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374554764.jpg_w600.jpg_180x132.jpg","url_show":"/articles/245678.htm","counter":"8824","comments":"23","score":"48","time":"2013-07-23 12:46:27"},{"sid":"245677","title_show":"深圳卖15年手机的老板自诉：补贴成山寨机割喉一刀","hometext_show_short":"一个在深圳做山寨手机销售15年的企业老板，在上周的深夜拨通了我的电话，焦虑、失望、不安的声音在手机那头响起。

这是我的一位相识多年的老...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/01374554322.jpg_180x132.jpg","url_show":"/articles/245677.htm","counter":"14018","comments":"20","score":"21","time":"2013-07-23 12:38:42"},{"sid":"245676","title_show":"[视频]Digg创始人护狗心切 怒丢野生浣熊","hometext_show_short":"近日，Digg创始人凯文·罗斯将一只浣熊从楼梯上丢下来的视频以及衍生的gif图在网站上引发了热议。据悉，这个视频是由罗斯家门前的安全监控摄像头拍...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/25_1374549981.gif_180x132.gif","url_show":"/articles/245676.htm","counter":"12481","comments":"40","score":"91","time":"2013-07-23 13:31:20"},{"sid":"245675","title_show":"[图]中移动运营 搭载骁龙600处理器的Galaxy Note II跑分曝光","hometext_show_short":"当大部分的消费者都关注即将推出的Galaxy Note III之后来自安兔兔的跑分数据库曝光了型号为“GT-N7108D”的Galaxy
Note II衍生版本，和现在Galaxy S4一样装备了...","logo":"http://static.cnbetacdn.com/topics/2009-3-23 16-22-09.gif","url_show":"/articles/245675.htm","counter":"4485","comments":"13","score":"-30","time":"2013-07-23 11:28:02"},{"sid":"245674","title_show":"三星推出新款Exynos 5 Octa八核SoC 八月量产","hometext_show_short":"三星电子今日为Exynos家族带来了新的产品，提供增强的图形性能和四个1.8GHz ARM Cortex-A15 + 四个1.3GHz Cortex A7核心。据报道，新芯片的图形性能比上一代Exyn...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/43_1374548647.jpg_180x132.jpg","url_show":"/articles/245674.htm","counter":"3713","comments":"15","score":"0","time":"2013-07-23 11:05:39"},{"sid":"245673","title_show":"SeeYourFolks.com：一个令人忧伤的网站","hometext_show_short":"由于现在越来越多的年轻一代选择在外打拼，于是回家探亲成为了一个社会性话题，而且它已经是一个全球性的话题。近日，4位英国人为此专门推出了一...","logo":"http://static.cnbetacdn.com/newsimg/2013/0723/16_1374548423.png_w600.png_180x132.png","url_show":"/articles/245673.htm","counter":"6622","comments":"13","score":"10","time":"2013-07-23 12:54:34"}],"pager":"","auto":1}})
*/