package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.client.RequestContext;
import org.jandroid.common.UnicodeUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class PublishCommentPoster extends AbstractLoader<JSONObject> {
/*
    op:publish
    content:期待啊，nokia,雄起
    seccode:pevv
    YII_CSRF_TOKEN:1555e16583e8fd4f30b360f78f9d79cf0b5288f4
    sid:250461
*/
// 回复：
/*
    op:publish
    subject:Re:[图]诺基亚6英寸跨界手机Lumia 1520首张谍照曝光
    content:大点好啊
    name:
    seccode:vjqe
    YII_CSRF_TOKEN:1555e16583e8fd4f30b360f78f9d79cf0b5288f4
    sid:250461
    pid:7792468
*/
// {"status":"success","result":"comment done"}

    //TODO: 把 YII_CSRF_TOKEN 保存到 Content 对象中，以免Cookie丢失，无法发布评论
    private static String URL_TEMPLATE = "http://www.cnbeta.com/comment";
        
    //只支持匿名
//    private String name;
    
    private long sid;
    
    private String commentContent;
    
    private long pid = 0;
    
    private String seccode;
    private String token;
    
    //匿名新发布
    public PublishCommentPoster(long sid, String commentContent, String seccode, String token) {
        this.sid = sid;
        this.commentContent = commentContent;
        this.seccode = seccode;
        this.token = token;
    }
    
    //匿名回复
    public PublishCommentPoster(long sid, String commentContent, long pid, String seccode, String token) {
        this.sid = sid;
        this.pid = pid;
        this.commentContent = commentContent;
        this.seccode = seccode;
        this.token = token;
    }


    public long getSid() {
        return sid;
    }

    public String getToken() {
        return token;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public long getPid() {
        return pid;
    }

    public String getSeccode() {
        return seccode;
    }

    @Override
    public JSONObject httpLoad(File baseDir, RequestContext requestContext) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        //should add this "X-Requested-With" header, so remote return result
        //httpget.addHeader("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Requested-With", "XMLHttpRequest");
        //this header is optional, better to add
        //httpget.addHeader("Referer", "http://www.cnbeta.com/articles/250243.htm");
        headers.put("Referer", "http://www.cnbeta.com/articles/" + getSid() + ".htm");


        Map<String, String> datas = new HashMap<String, String>();
        datas.put("op", "publish");
        datas.put("name", "");
        datas.put("sid", "" + getSid());
        if(getPid() != 0) {
            datas.put("pid", "" + getPid());
        }
        datas.put("content", getCommentContent());
        datas.put("seccode", getSeccode());
        //需要这个 token 来执行 support/against
        // 将 token 保存在 Content中，更安全
        datas.put("YII_CSRF_TOKEN", getToken());
//        datas.put("YII_CSRF_TOKEN", CnBetaHttpClient.getInstance().getCookie("YII_CSRF_TOKEN"));
        
        String response = CnBetaHttpClient.getInstance().httpPost(URL_TEMPLATE, headers, datas, requestContext);
        response = UnicodeUtils.unicode2Chinese(response);
        return (JSONObject) JSONValue.parse(response);
    }

    @Override
    public String getFileName() {
        return "";
    }

    @Override
    public JSONObject fromDisk(File baseDir) throws Exception {
        throw new UnsupportedOperationException("fromDisk");
    }
}
