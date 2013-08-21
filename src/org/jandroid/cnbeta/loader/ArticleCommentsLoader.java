package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;

import java.io.File;

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


    private Content content;

    public ArticleCommentsLoader(Content content) {
        this.content = content;
    }

    @Override
    public Content fromHttp() throws Exception {
        //TODO: load 完成之后，update Content并返回
        return null;
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
