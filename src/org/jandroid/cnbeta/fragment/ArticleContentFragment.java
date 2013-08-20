package org.jandroid.cnbeta.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RatingBar;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.async.ArticleContentAsyncTask;
import org.jandroid.cnbeta.async.AsyncResult;
import org.jandroid.cnbeta.loader.ArticleListLoader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentFragment extends Fragment {

    //TODO: sn 从 article.html 页面中取, sid和sn必须要匹配
    //TODO: 可能还需要拿到 TOKEN: 'ae14639495b0ae0c848e2adaefc0f31db276167a',
    // http://www.cnbeta.com/cmt?jsoncallback=okcb91797948&op=info&page=1&sid=247973&sn=88747
    // okcb91797948({"status":"success","result":"Y25iZXRheyJjbW50ZGljdCI6W10sImhvdGxpc3QiOltdLCJjbW50bGlzdCI6W10sImNvbW1lbnRfbnVtIjoiMjEiLCJqb2luX251bSI6MCwidG9rZW4iOiIyYzM3MzBmY2I3OTE4N2IxNDU2NmQwMzFiOTc2MGQ1YzIxMGRlMWVhIiwidmlld19udW0iOjQzOTAsInBhZ2UiOjEsInNpZCI6MjQ3OTczLCJ1IjpbXX0="})
   // base64 decode 之后
    // cnbeta{"cmntdict":[],"hotlist":[],"cmntlist":[],"join_num":0,"comment_num":0,"token":"1555e16583e8fd4f30b360f78f9d79cf0b5288f4","view_num":169,"page":1,"sid":249281,"u":[]}
    // cnbeta{"cmntdict":[],"hotlist":[],"cmntlist":[{"tid":"7764550","pid":"0","sid":"249281","parent":"","thread":""}],"cmntstore":{"7764550":{"tid":"7764550","pid":"0","sid":"249281","date":"2013-08-20 15:14:01","name":"\u533f\u540d\u4eba\u58eb","host_name":"\u6d59\u6c5f\u7701\u7ecd\u5174\u5e02","comment":"\u8def\u8fc7...","score":"0","reason":1,"userid":"0","icon":""}},"comment_num":"2","join_num":"1","token":"38a5032136b4e3097c28670a7cdd0c7fad2d1c62","view_num":370,"page":1,"sid":249281,"u":[]}
    
/*
http://static.cnbetacdn.com/assets/js/utils/article.js?v=20130808

    if(res.comment_num != 'undefined'){
        $("#comment_num").html(res.comment_num);
        $("#view_num").html(res.view_num);
        $(".post_count").html('共有<em>'+res.comment_num+'</em>条评论，显示<em>'+res.join_num+'</em>条').fadeIn();
    }
*/

    //TODO: 支持 视频？？？

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)	{
        View root = inflater.inflate(R.layout.content_article, container);
        TextView titleTextView = (TextView)root.findViewById(R.id.tv_articleTitle);
        WebView contentWebView = (WebView)root.findViewById(R.id.wv_articleContent);
        RatingBar ratingBar = (RatingBar)root.findViewById(R.id.rating);
		return root;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_content_fragment_menu, menu);
        menu.add("MENU").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    
}
