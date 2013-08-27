package org.jandroid.cnbeta.loader;

import android.util.Base64;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Content;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsLoader extends AbstractLoader<Content> {
    //TODO: 返回 Content，而不是Comment

    private static String URL_TEMPLATE = "http://www.cnbeta.com/cmt?jsoncallback=okcb{0}&op=info&page=1&sid={1}&sn={2}";
    
    // http://www.cnbeta.com/cmt?jsoncallback=okcb91797948&op=info&page=1&sid=247973&sn=88747
    // okcb91797948({"status":"success","result":"Y25iZXRheyJjbW50ZGljdCI6W10sImhvdGxpc3QiOltdLCJjbW50bGlzdCI6W10sImNvbW1lbnRfbnVtIjoiMjEiLCJqb2luX251bSI6MCwidG9rZW4iOiIyYzM3MzBmY2I3OTE4N2IxNDU2NmQwMzFiOTc2MGQ1YzIxMGRlMWVhIiwidmlld19udW0iOjQzOTAsInBhZ2UiOjEsInNpZCI6MjQ3OTczLCJ1IjpbXX0="})
    // base64 decode 之后
    // cnbeta{"cmntdict":[],"hotlist":[],"cmntlist":[],"join_num":0,"comment_num":0,"token":"1555e16583e8fd4f30b360f78f9d79cf0b5288f4","view_num":169,"page":1,"sid":249281,"u":[]}
    // cnbeta{"cmntdict":[],"hotlist":[],"cmntlist":[{"tid":"7764550","pid":"0","sid":"249281","parent":"","thread":""}],"cmntstore":{"7764550":{"tid":"7764550","pid":"0","sid":"249281","date":"2013-08-20 15:14:01","name":"\u533f\u540d\u4eba\u58eb","host_name":"\u6d59\u6c5f\u7701\u7ecd\u5174\u5e02","comment":"\u8def\u8fc7...","score":"0","reason":1,"userid":"0","icon":""}},"comment_num":"2","join_num":"1","token":"38a5032136b4e3097c28670a7cdd0c7fad2d1c62","view_num":370,"page":1,"sid":249281,"u":[]}


    private Content origContent;

    public ArticleCommentsLoader(Content origContent) {
        this.origContent = origContent;
    }

    public Content getContent() {
        return origContent;
    }

    @Override
    public Content fromHttp(File baseDir) throws Exception {
        String url = MessageFormat.format(URL_TEMPLATE, "" + System.currentTimeMillis(), ""+getContent().getSid(), getContent().getSn());
        String response = CnBetaHttpClient.getInstance().httpGet(url);
        
        //TODO: if failed
        if(response.indexOf("error") > 0){
            //TODO: 解码 result 得到 错误信息
            throw new Exception("error to read comments of article " + getContent().getSid());
        }
        String responseJSONString = response.substring(response.indexOf('(') + 1, response.lastIndexOf(')'));
        JSONObject responseJSON = (JSONObject) JSONValue.parse(responseJSONString);
        Object resultBase64String = responseJSON.get("result");
        String resultJSONString = new String(Base64.decode(resultBase64String.toString(), Base64.DEFAULT), "utf-8");
        resultJSONString = resultJSONString.substring(resultJSONString.indexOf('{'), resultJSONString.lastIndexOf('}')+1);

        writeDisk(baseDir, resultJSONString);

        JSONObject resultJSON = (JSONObject) JSONValue.parse(resultJSONString);
        
        parseResultJSON(resultJSON);
        //返回 updated Content
        return getContent();
    }

    private void parseResultJSON(JSONObject resultJSON){
        //阅读和评论次数
        getContent().setViewNum(Integer.parseInt(resultJSON.get("view_num").toString()));
        getContent().setCommentNum(Integer.parseInt(resultJSON.get("comment_num").toString()));
        //TODO: comments
        
    }
    
    @Override
    public Content fromDisk(File baseDir) throws Exception {
        String resultJSONString = readDisk(baseDir);
        JSONObject resultJSON = (JSONObject) JSONValue.parse(resultJSONString);
        parseResultJSON(resultJSON);
        return getContent();
    }

    @Override
    public String getFileName() {
        return "comment_" + getContent().getSid();
    }
}

/*


{
"cmntdict":{"7784832":[{"tid":"7784752","pid":"0","sid":"250209"}]},
"hotlist":[{"tid":"7784715","pid":"0","sid":"250209","parent":"","thread":""}],
"cmntlist":[
{"tid":"7784836","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784832","pid":"7784752","sid":"250209","parent":"7784752","thread":"7784752"},
{"tid":"7784831","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784801","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784794","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784790","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784757","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784752","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784715","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784698","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784690","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784680","pid":"0","sid":"250209","parent":"","thread":""},
{"tid":"7784679","pid":"0","sid":"250209","parent":"","thread":""}],
"cmntstore":{
"7784836":{"tid":"7784836","pid":"0","sid":"250209","date":"2013-08-27 15:25:13","name":"匿名人士","host_name":"重庆市","comment":"我比较好奇Other。","score":"0","reason":"0","userid":"0","icon":""},
"7784832":{"tid":"7784832","pid":"7784752","sid":"250209","date":"2013-08-27 15:24:09","name":"匿名人士","host_name":"江苏省常州市","comment":"因为别家都没出几台RT，只有微软傻乎乎的推。。。","score":"0","reason":"0","userid":"0","icon":""},
"7784831":{"tid":"7784831","pid":"0","sid":"250209","date":"2013-08-27 15:24:07","name":"匿名人士","host_name":"福建省厦门市","comment":"只要肯赔钱卖，rt肯定能上去，2000以下的价格才有希望","score":"0","reason":"0","userid":"0","icon":""},
"7784801":{"tid":"7784801","pid":"0","sid":"250209","date":"2013-08-27 15:12:48","name":"匿名人士","host_name":"河北省廊坊市","comment":"这个太讽刺了","score":"0","reason":"0","userid":"0","icon":""},
"7784794":{"tid":"7784794","pid":"0","sid":"250209","date":"2013-08-27 15:10:59","name":"匿名人士","host_name":"北京市","comment":"睁眼说瞎话，明明是other销量最大~","score":"3","reason":"0","userid":"0","icon":""},
"7784790":{"tid":"7784790","pid":"0","sid":"250209","date":"2013-08-27 15:09:38","name":"匿名人士","host_name":"上海市","comment":"矮子里头挑大个er","score":3,"reason":"0","userid":"0","icon":""},
"7784757":{"tid":"7784757","pid":"0","sid":"250209","date":"2013-08-27 14:56:52","name":"匿名人士","host_name":"上海市","comment":"好牛13，年底消灭ipad毫无悬念","score":"2","reason":"0","userid":"0","icon":""},
"7784752":{"tid":"7784752","pid":"0","sid":"250209","date":"2013-08-27 14:54:42","name":"匿名人士","host_name":"浙江省温州市","comment":"Surface RT亏了好几亿，竟然是卖得最好的，太讽刺了","score":"1","reason":"0","userid":"0","icon":""},
"7784715":{"tid":"7784715","pid":"0","sid":"250209","date":"2013-08-27 14:44:26","name":"匿名人士","host_name":"上海市","comment":"Surface RT销量最佳，原來其他設備連這個廢品都不如……","score":"5","reason":"0","userid":"0","icon":""},
"7784698":{"tid":"7784698","pid":"0","sid":"250209","date":"2013-08-27 14:40:42","name":"匿名人士","host_name":"上海市","comment":"高級黑","score":"4","reason":"0","userid":"0","icon":""},
"7784690":{"tid":"7784690","pid":"0","sid":"250209","date":"2013-08-27 14:37:57","name":"匿名人士","host_name":"澳门","comment":"牛13的联想呢....","score":"2","reason":"0","userid":"0","icon":""},
"7784680":{"tid":"7784680","pid":"0","sid":"250209","date":"2013-08-27 14:35:08","name":"匿名人士","host_name":"陕西省西安市","comment":"主要是因为RT降价太猛了","score":"2","reason":"0","userid":"0","icon":""},
"7784679":{"tid":"7784679","pid":"0","sid":"250209","date":"2013-08-27 14:34:59","name":"匿名人士","host_name":"北京市","comment":"呵呵~那之前一直说的联想Yoga呢？","score":"2","reason":"0","userid":"0","icon":""}
},
"comment_num":"13",
"join_num":"13",
"token":"1555e16583e8fd4f30b360f78f9d79cf0b5288f4",
"view_num":448,
"page":"1",
"sid":"250209",
"u":[]}


{"cmntdict":[],"hotlist":[{"tid":"7784591","pid":"0","sid":"250207","parent":"","thread":""}],
"cmntlist":[
{"tid":"7784874","pid":"0","sid":"250207","parent":"","thread":""},
{"tid":"7784828","pid":"0","sid":"250207","parent":"","thread":""},
{"tid":"7784810","pid":"0","sid":"250207","parent":"","thread":""},
{"tid":"7784591","pid":"0","sid":"250207","parent":"","thread":""}],
"cmntstore":
{
"7784874":{"tid":"7784874","pid":"0","sid":"250207","date":"2013-08-27 15:35:04","name":"匿名人士","host_name":"上海市嘉定区","comment":"找我来当吧，打败苹果不在话下","score":"0","reason":"0","userid":"0","icon":""},
"7784828":{"tid":"7784828","pid":"0","sid":"250207","date":"2013-08-27 15:23:22","name":"更与何人说","host_name":"河南省郑州市登封市","comment":"其实微软深层的问题不是产品的问题，而是理念和细节出了问题。因为事实上微软的很多产品都是很超前的具有里程碑式的产品的：现在大火的Kinect且不说，当你们认为“ipad就是平板电脑代名词”的时候，别忘了是微软在业界第一个提出平板电脑的概念并将之量产的。我记得当年微软和Acer有合作，推出的一款手写原笔迹平板，主打商务市场。虽然后来没有后来了，但是仍然不失为平板领域的开先河者。当先驱变成先烈，我们仍然应该对这家公司为驱动人类计算应用的进步而付出的努力深怀敬意。","score":"0","reason":"0","userid":"4017","icon":"http:\/\/qzapp.qlogo.cn\/qzapp\/100370481\/AAA7B5FB3FF76B2D323856D92285B59F\/100"},
"7784810":{"tid":"7784810","pid":"0","sid":"250207","date":"2013-08-27 15:16:45","name":"更与何人说","host_name":"河南省郑州市登封市","comment":"微软最大的问题首先是牌技不行，拿一手炸弹打得臭烂。","score":"3","reason":"0","userid":"4017","icon":"http:\/\/qzapp.qlogo.cn\/qzapp\/100370481\/AAA7B5FB3FF76B2D323856D92285B59F\/100"},
"7784591":{"tid":"7784591","pid":"0","sid":"250207","date":"2013-08-27 14:09:37","name":"匿名人士","host_name":"西藏","comment":"微软需要一堆c13喷子","score":8,"reason":"0","userid":"0","icon":""}
},"comment_num":"4","join_num":"4","token":"1555e16583e8fd4f30b360f78f9d79cf0b5288f4","view_num":320,"page":"1","sid":"250207","u":[]}

*/