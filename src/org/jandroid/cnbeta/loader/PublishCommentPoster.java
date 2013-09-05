package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Content;
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
    
    private static String URL_TEMPLATE = "http://www.cnbeta.com/comment";
        
    //只支持匿名
//    private String name;
    
    private long sid;
    
    private String commentContent;
    
    private long pid = 0;
    
    private String seccode;
    
    
    //匿名新发布
    public PublishCommentPoster(long sid, String commentContent, String seccode) {
        this.sid = sid;
        this.commentContent = commentContent;
        this.seccode = seccode;
    }
    
    //匿名回复
    public PublishCommentPoster(long sid, String commentContent, long pid, String seccode) {
        this.sid = sid;
        this.pid = pid;
        this.commentContent = commentContent;
        this.seccode = seccode;
    }


    public long getSid() {
        return sid;
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
    public JSONObject fromHttp(File baseDir) throws Exception {
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
        datas.put("YII_CSRF_TOKEN", CnBetaHttpClient.getInstance().getCookie("YII_CSRF_TOKEN"));
        
        String response = CnBetaHttpClient.getInstance().httpPost(URL_TEMPLATE, headers, datas);
        
        //if failed
        if(response.indexOf("error") > 0){
            throw new Exception("Failed to publish comment: " + getSid() + ", " + response );
        }

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
