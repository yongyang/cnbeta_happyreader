package org.jandroid.cnbeta.loader;

import android.util.Base64;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.common.UnicodeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleCommentsLoader extends AbstractLoader<List<Comment>> {
    //NOTE!!!! 过时的 cmt URL
//    private static String URL_TEMPLATE = "http://www.cnbeta.com/cmt?jsoncallback=okcb{0}&op=info&page=1&sid={1}&sn={2}&op={3}";


    // 当前的 cmt URL
    //op 需要编码
    //编码方式如下： 1,{SID},{SN}, Base64编码，然后加上8位随机字符数字，参考 generateOp
//    private static String URL_TEMPLATE = "http://www.cnbeta.com/cmt?jsoncallback=okcb{0}&op={1}";
    private static String URL_TEMPLATE = "http://www.cnbeta.com/cmt";

    private static String b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    // http://www.cnbeta.com/cmt?jsoncallback=okcb91797948&op=info&page=1&sid=247973&sn=88747
    // okcb91797948({"status":"success","result":"Y25iZXRheyJjbW50ZGljdCI6W10sImhvdGxpc3QiOltdLCJjbW50bGlzdCI6W10sImNvbW1lbnRfbnVtIjoiMjEiLCJqb2luX251bSI6MCwidG9rZW4iOiIyYzM3MzBmY2I3OTE4N2IxNDU2NmQwMzFiOTc2MGQ1YzIxMGRlMWVhIiwidmlld19udW0iOjQzOTAsInBhZ2UiOjEsInNpZCI6MjQ3OTczLCJ1IjpbXX0="})
    // base64 decode 之后
    // cnbeta{"cmntdict":[],"hotlist":[],"cmntlist":[],"join_num":0,"comment_num":0,"token":"1555e16583e8fd4f30b360f78f9d79cf0b5288f4","view_num":169,"page":1,"sid":249281,"u":[]}
    // cnbeta{"cmntdict":[],"hotlist":[],"cmntlist":[{"tid":"7764550","pid":"0","sid":"249281","parent":"","thread":""}],"cmntstore":{"7764550":{"tid":"7764550","pid":"0","sid":"249281","date":"2013-08-20 15:14:01","name":"\u533f\u540d\u4eba\u58eb","host_name":"\u6d59\u6c5f\u7701\u7ecd\u5174\u5e02","comment":"\u8def\u8fc7...","score":"0","reason":1,"userid":"0","icon":""}},"comment_num":"2","join_num":"1","token":"38a5032136b4e3097c28670a7cdd0c7fad2d1c62","view_num":370,"page":1,"sid":249281,"u":[]}


    private Content content;

    List<Comment> comments = new ArrayList<Comment>();


    public ArticleCommentsLoader(Content content) {
        this.content = content;
    }

    @Override
    public List<Comment> fromHttp(File baseDir) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        //should add this "X-Requested-With" header, so remote return result
        //httpget.addHeader("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Requested-With", "XMLHttpRequest");
        //this header is optional, better to add
        //httpget.addHeader("Referer", "http://www.cnbeta.com/articles/250243.htm");
        headers.put("Referer", "http://www.cnbeta.com/articles/" + content.getSid() + ".htm");

        // see article.min.js#initData
//        String url = MessageFormat.format(URL_TEMPLATE, "" + Math.round((System.currentTimeMillis() / 15e3)), generateOP());

        Map<String, String> datas = new HashMap<String, String>();
        datas.put("op", generateOP());

        String response = CnBetaHttpClient.getInstance().httpPost(URL_TEMPLATE, headers, datas);
        
        //if failed
        if(response.indexOf("error") > 0){
            throw new Exception("Failed to read comments of article: " + content.getSid() + ", " + response );
        }
//        String responseJSONString = response.substring(response.indexOf('(') + 1, response.lastIndexOf(')'));
        JSONObject responseJSON = (JSONObject) JSONValue.parse(response);
        Object resultBase64String = responseJSON.get("result");
        String resultJSONString = new String(Base64.decode(resultBase64String.toString(), Base64.DEFAULT), "UTF-8");
        resultJSONString = resultJSONString.substring(resultJSONString.indexOf('{'), resultJSONString.lastIndexOf('}')+1);

        writeDisk(baseDir, resultJSONString);

        JSONObject resultJSON = (JSONObject) JSONValue.parse(resultJSONString);
        parseResultJSON(resultJSON);
        //返回 updated Content
        return comments;
    }

    private void parseResultJSON(JSONObject resultJSON) throws Exception {
        //阅读和评论次数
        content.setViewNum(Integer.parseInt(resultJSON.get("view_num").toString()));
        content.setCommentNum(Integer.parseInt(resultJSON.get("comment_num").toString()));
        
        
        JSONObject commentStoreJSONObject = (JSONObject)resultJSON.get("cmntstore");
       
        for(Object scommentObject : (JSONArray)resultJSON.get("cmntlist")){
            JSONObject scommentJSONObject = (JSONObject)scommentObject;
            String tid = scommentJSONObject.get("tid").toString();
            JSONObject commentJSONObject = (JSONObject)commentStoreJSONObject.get(tid);
            // unicode to Chinese
            commentJSONObject.put("name", UnicodeUtils.unicode2Chinese(commentJSONObject.get("name").toString()));
            commentJSONObject.put("host_name", UnicodeUtils.unicode2Chinese(commentJSONObject.get("host_name").toString()));
            commentJSONObject.put("comment", UnicodeUtils.unicode2Chinese(commentJSONObject.get("comment").toString()));
            commentJSONObject.put("token", resultJSON.get("token").toString());
            comments.add(new Comment(commentJSONObject));
        }
        
    }
    
    @Override
    public List<Comment> fromDisk(File baseDir) throws Exception {
        String resultJSONString = readDisk(baseDir);
        JSONObject resultJSON = (JSONObject) JSONValue.parse(resultJSONString);
        parseResultJSON(resultJSON);
        return comments;
    }

    @Override
    public String getFileName() {
        return "comment_" + content.getSid();
    }

    private String generateOP() throws Exception {
        
        String encoded = Base64.encodeToString(("1," + content.getSid() + "," + content.getSn()).getBytes("UTF-8"), Base64.NO_WRAP);
        for(int i=0; i<8; i++){
            encoded += b64.charAt((int)(Math.random() * b64.length()));
        }
        // 两次 url encode，主要为转换 "="
        encoded = URLEncoder.encode(encoded, "UTF-8");
        return encoded;
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

/*
http://static.cnbetacdn.com/assets/js/utils/jquery.cbcode.js

(function ($) {
    var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",
            a256 = '',
            r64 = [256],
            r256 = [256],
            i = 0;
    var UTF8 = {
        encode: function (strUni) {
            var strUtf = strUni.replace(/[\u0080-\u07ff]/g,
                    function (c) {
                        var cc = c.charCodeAt(0);
                        return String.fromCharCode(0xc0 | cc >> 6, 0x80 | cc & 0x3f)
                    }).replace(/[\u0800-\uffff]/g,
                    function (c) {
                        var cc = c.charCodeAt(0);
                        return String.fromCharCode(0xe0 | cc >> 12, 0x80 | cc >> 6 & 0x3F, 0x80 | cc & 0x3f)
                    });
            return strUtf
        },
        decode: function (strUtf) {
            var strUni = strUtf.replace(/[\u00e0-\u00ef][\u0080-\u00bf][\u0080-\u00bf]/g,
                    function (c) {
                        var cc = ((c.charCodeAt(0) & 0x0f) << 12) | ((c.charCodeAt(1) & 0x3f) << 6) | (c.charCodeAt(2) & 0x3f);
                        return String.fromCharCode(cc)
                    }).replace(/[\u00c0-\u00df][\u0080-\u00bf]/g,
                    function (c) {
                        var cc = (c.charCodeAt(0) & 0x1f) << 6 | c.charCodeAt(1) & 0x3f;
                        return String.fromCharCode(cc)
                    });
            return strUni
        }
    };
    while (i < 256) {
        var c = String.fromCharCode(i);
        a256 += c;
        r256[i] = i;
        r64[i] = b64.indexOf(c);
        ++i
    }
    ;
    function code(s, discard, alpha, beta, w1, w2) {
        s = String(s);
        var buffer = 0,
                i = 0,
                length = s.length,
                result = '',
                bitsInBuffer = 0;
        while (i < length) {
            var c = s.charCodeAt(i);
            c = c < 256 ? alpha[c] : -1;
            buffer = (buffer << w1) + c;
            bitsInBuffer += w1;
            while (bitsInBuffer >= w2) {
                bitsInBuffer -= w2;
                var tmp = buffer >> bitsInBuffer;
                result += beta.charAt(tmp);
                buffer ^= tmp << bitsInBuffer
            }
            ++i
        }
        ;
        if (!discard && bitsInBuffer > 0) result += beta.charAt(buffer << (w2 - bitsInBuffer));
        return result
    };
    var Plugin = $.cbcode = function (dir, input, encode) {
        return input ? Plugin[dir](input, encode) : dir ? null : this
    };
    Plugin.en64 = Plugin.encode = function (plain, utf8encode, sublen) {
        sublen = sublen === false ? 0 : sublen;
        plain = Plugin.raw === false || Plugin.utf8encode || utf8encode ? UTF8.encode(plain) : plain;
        plain = code(plain, false, r256, b64, 8, 6);
        plain += '===='.slice((plain.length % 4) || 4);
        if (sublen) {
            for (i = 0; i < sublen; i++) {
                plain += b64.charAt(Math.floor(Math.random() * b64.length));
            }
        }
        return plain;
    };
    Plugin.de64 = Plugin.decode = function (coded, utf8decode, sublen) {
        sublen = sublen === false ? 0 : sublen;
        coded = coded.substr(sublen) + '';
        coded = String(coded).split('=');
        var i = coded.length;
        do {
            --i;
            coded[i] = code(coded[i], true, r64, a256, 6, 8)
        }
        while (i > 0);
        coded = coded.join('');
        return Plugin.raw === false || Plugin.utf8decode || utf8decode ? UTF8.decode(coded) : coded
    }
}(jQuery));
$.cbcode.utf8encode = true;

*/
