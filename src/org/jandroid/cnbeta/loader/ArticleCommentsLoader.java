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

    private String URL_TEMPLATE = "http://www.cnbeta.com/cmt?jsoncallback=okcb{0}&op=info&page=1&sid={1}&sn={2}";
    
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
        Object result = responseJSON.get("result");
        String resultJSONString = new String(Base64.decode(result.toString(), Base64.DEFAULT), "utf-8");
        resultJSONString = resultJSONString.substring(resultJSONString.indexOf('{'), resultJSONString.lastIndexOf('}')+1);
        JSONObject resultJSON = (JSONObject) JSONValue.parse(resultJSONString);
        
        parseResultJSON(resultJSON);
        //返回 updated Content
        return getContent();
    }

    private void parseResultJSON(JSONObject resultJSON){
        //阅读和评论次数
        getContent().setViewNum(Integer.parseInt(resultJSON.get("view_num").toString()));
        getContent().setViewNum(Integer.parseInt(resultJSON.get("comment_num").toString()));
        //TODO: comments
        
    }
    
    @Override
    public Content fromDisk(File baseDir) throws Exception {
        return null;
    }

    @Override
    public void toDisk(File baseDir, Content comment) throws Exception {

    }

    @Override
    public File getCacheFile(File baseDir) {
        return new File(baseDir, "comment_" + getContent().getSid());
    }
}
