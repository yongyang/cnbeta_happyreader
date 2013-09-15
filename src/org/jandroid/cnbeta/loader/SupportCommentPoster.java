package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Comment;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class SupportCommentPoster extends AbstractLoader<JSONObject> {
/*
    op:support
    YII_CSRF_TOKEN:1555e16583e8fd4f30b360f78f9d79cf0b5288f4
    sid:250474
    tid:7792156
*/
// {"status":"success","result":"voted"}
    
    private static String URL_TEMPLATE = "http://www.cnbeta.com/comment";

    public static enum Op {
        SUPPORT("support"),
        AGAINST("against");

        private String type;

        private Op(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return type;
        }
    }
    
    private Comment comment;
    
    private Op op;


    public SupportCommentPoster(Comment comment, Op op) {
        this.comment = comment;
        this.op = op;
    }

    public Comment getComment() {
        return comment;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public JSONObject httpLoad(File baseDir) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        //should add this "X-Requested-With" header, so remote return result
        //httpget.addHeader("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Requested-With", "XMLHttpRequest");
        //this header is optional, better to add
        //httpget.addHeader("Referer", "http://www.cnbeta.com/articles/250243.htm");
        headers.put("Referer", "http://www.cnbeta.com/articles/" + comment.getSid() + ".htm");


        Map<String, String> datas = new HashMap<String, String>();
        datas.put("op", getOp().getType());
        datas.put("sid", "" + getComment().getSid());
        datas.put("tid", "" + getComment().getTid());
        //需要这个 token 来执行 support/against
        datas.put("YII_CSRF_TOKEN", CnBetaHttpClient.getInstance().getCookie("YII_CSRF_TOKEN"));
        
        
        String response = CnBetaHttpClient.getInstance().httpPost(URL_TEMPLATE, headers, datas);
        
        //if failed
        if(response.indexOf("error") > 0){
            throw new Exception("Failed to support comment: " + comment.getTid() + ", " + response );
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
