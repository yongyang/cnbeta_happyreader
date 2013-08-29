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
public class RateArticlePoster extends AbstractLoader<JSONObject> {
/*
    op:rate
    score:3
    YII_CSRF_TOKEN:1555e16583e8fd4f30b360f78f9d79cf0b5288f4
    sid:250474
*/
// {"status":"success","result":{"average":"-0.4","count":"180"}}    


    private static String URL_TEMPLATE = "http://www.cnbeta.com/comment";

    
    private Content content;
    
    private int score;

    public RateArticlePoster(Content content, int score) {
        this.content = content;
        this.score = score;
    }

    public Content getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    @Override
    public JSONObject fromHttp(File baseDir) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        //should add this "X-Requested-With" header, so remote return result
        //httpget.addHeader("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Requested-With", "XMLHttpRequest");
        //this header is optional, better to add
        //httpget.addHeader("Referer", "http://www.cnbeta.com/articles/250243.htm");
        headers.put("Referer", "http://www.cnbeta.com/articles/" + content.getSid() + ".htm");


        Map<String, String> datas = new HashMap<String, String>();
        datas.put("op", "rate");
        datas.put("sid", "" + content.getSid());
        datas.put("score", "" + getScore());
        //需要这个 token 来执行 support/against
        datas.put("YII_CSRF_TOKEN", CnBetaHttpClient.getInstance().getCookie("YII_CSRF_TOKEN"));
        
        
        String response = CnBetaHttpClient.getInstance().httpPost(URL_TEMPLATE, headers, datas);
        
        //if failed
        if(response.indexOf("error") > 0){
            throw new Exception("Failed to rate article: " + content.getSid() + ", " + response );
        }

        return (JSONObject) JSONValue.parse(response);
    }

    @Override
    public String getFileName() {
        throw new UnsupportedOperationException("getFileName");
    }

    @Override
    public JSONObject fromDisk(File baseDir) throws Exception {
        throw new UnsupportedOperationException("fromDisk");
    }
}
