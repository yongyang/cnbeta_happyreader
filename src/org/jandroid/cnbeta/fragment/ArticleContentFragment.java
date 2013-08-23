package org.jandroid.cnbeta.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.ContentActivity;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.util.Logger;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentFragment extends Fragment {

    static Logger log = Logger.newLogger(ArticleContentFragment.class);

    private TextView titleTextView;
    private TextView timeTextView;
    private TextView whereTextView;
    private TextView viewNumTextView;
    private TextView commentNumTextView;
    private RatingBar ratingBar;
    private WebView contentWebView;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_article, null);
        titleTextView = (TextView) root.findViewById(R.id.tv_articleTitle);
        titleTextView.setText(((ContentActivity) getActivity()).getArticleTitle());
        titleTextView.setSelected(true); // select to enable marque
        timeTextView = (TextView) root.findViewById(R.id.tv_date);
        viewNumTextView = (TextView) root.findViewById(R.id.tv_viewNum);
        commentNumTextView = (TextView) root.findViewById(R.id.tv_commentNum);
        whereTextView = (TextView) root.findViewById(R.id.tv_where);

        ratingBar = (RatingBar) root.findViewById(R.id.rating);
        contentWebView = (WebView) root.findViewById(R.id.wv_articleContent);
        // work weird
//        contentWebView.setBackgroundColor(R.color.cnBeta_bg_introduction);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        //设置嫩参数
        contentWebView.getSettings().setDefaultFontSize(14);
        contentWebView.getSettings().setDefaultFixedFontSize(14);
        contentWebView.getSettings().setAllowFileAccess(true);
        contentWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        contentWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // no scroll
        contentWebView.getSettings().setBuiltInZoomControls(true);
        contentWebView.getSettings().setAppCacheEnabled(true);
        contentWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        contentWebView.getSettings().setLoadsImagesAutomatically(false); //don't load images auto
//        contentWebView.getSettings().setBlockNetworkImage(true);
        contentWebView.getSettings().setDefaultTextEncodingName("UTF-8");

        // resize big image to fit screen width
        contentWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        contentWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void openImage(String img) {
                Intent intent = new Intent();
                intent.putExtra("image", img);
                //			intent.setClass(context, ShowWebImageActivity.class);
                //			context.startActivity(intent);
                Toast.makeText(getActivity(), "点击了图片: " + img, Toast.LENGTH_SHORT).show();
                //TODO: 新开一个 Transparent Activity, 使用 WebView 打开大图
            }
        }, "JS");

        contentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addImageClickListener();
                //TODO: load images here, after Page Loaded
                List<String> images =((ContentActivity)getActivity()).getContent().getImages();
                for(String imgSrc : images){
                    ((ContentActivity)getActivity()).loadImages();
                }
                
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

//                return super.shouldOverrideUrlLoading(view, url);
                if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif")) {
                    //TODO: 打开  topic Activity
                }
                // 禁止所有的 url 访问
                return true;
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                log.e("ERRROR: " + errorCode + ", " + description + ", " + failingUrl);
            }
        });

        // 处理消息 和 Alert
        contentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                log.d(cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Toast.makeText(getActivity(), "JSAlert: " + message, Toast.LENGTH_SHORT).show();
                return true;
            }

        });

        //TODO: 在WebView load 的之前, 重写topic img url, 并注入JS，使得img load完之后，通过JS更新内容

        //TODO: use a WebView to enlarge image, http://mobile.tutsplus.com/tutorials/android/image-display-and-interaction-with-android-webviews/
        //picView.getSettings().setBuiltInZoomControls(true);
        //picView.getSettings().setUseWideViewPort(true);
        return root;
    }
    private void addImageClickListener() {
        // 给所有img添加onclick函数，点击时打开大图
        contentWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
//                "for(var i=0;i<objs.length;i++)  " +
                // 跳过 topic 图片
                "for(var i=1;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "window.JS.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_content_fragment_menu, menu);
//        menu.add("MENU").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void updateContent(Content content) {
        titleTextView.setText(content.getTitle());
        titleTextView.setSelected(true);
        timeTextView.setText(content.getTime());
        viewNumTextView.setText("" + content.getViewNum());
        commentNumTextView.setText("" + content.getCommentNum());
        whereTextView.setText(content.getWhere());
        //TODO: 在WebView load 的之前, 重写topic img url, 并设置 img Id，使得img load完之后，通过JS更新内容
        //TODO: 使用 JSoup Element 完成重写 img url？
        contentWebView.loadDataWithBaseURL("", content.getContent(), "text/html", "UTF-8", "");
    }
    

    public void updateImage(String id, String imgSrc) {
        // 在android代码中调用javaScript方法
        contentWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].src='" + imgSrc + "';" +
                "}" +
                "})()");
        //TODO: WebView从APK中加载Assets目录中的内容，是否需要在 asset 中存放一张默认图片
        contentWebView.loadUrl("file:///android_asset/personaldata.html");
    }
}
