package org.jandroid.cnbeta.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.common.Logger;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentFragment extends Fragment {

    static Logger log = Logger.getLogger(ArticleContentFragment.class);

    private TextView titleTextView;
    private TextView timeTextView;
    private TextView whereTextView;
    private TextView viewNumTextView;
    private TextView commentNumTextView;
    private RatingBar ratingBar;
    private WebView contentWebView;

    private Handler handler = new Handler();


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
        contentWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        contentWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        contentWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        contentWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // no scroll
        contentWebView.getSettings().setBuiltInZoomControls(true);
        contentWebView.getSettings().setAppCacheEnabled(true);
        contentWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        contentWebView.getSettings().setLoadsImagesAutomatically(true);
        contentWebView.getSettings().setBlockNetworkImage(true);
        contentWebView.getSettings().setDefaultTextEncodingName("UTF-8");

        // resize big image to fit screen width
        contentWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        contentWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void openImage(String imgSrc) {
                //新开一个 Transparent Activity, 使用 WebView 打开大图
                Utils.openImageViewerActivity(getActivity(), imgSrc);
            }

            @JavascriptInterface
            public void openTopic(String topicId, String topicName) {
                Utils.openTopicActivity(getActivity(), Long.parseLong(topicId), topicName);
/*
                Intent intent = new Intent();
                intent.putExtra("topicId", topicId);
                intent.putExtra("topicName", topicName);
                Toast.makeText(getActivity(), "点击了Topic: " + topicId + ", " + topicName, Toast.LENGTH_SHORT).show();
*/
            }

        }, "JS");

        contentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //load images here, after Page Loaded
                ((ContentActivity)getActivity()).loadImages();
                //load comments and view_num, comment_num etc
                ((ContentActivity)getActivity()).loadComments();

/*
                List<String> images =((ContentActivity)getActivity()).getContent().getImages();
                for(String imgSrc : images){
                    ((ContentActivity)getActivity()).loadImages();
                }
*/

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.content_menu, menu);
//        menu.add("MENU").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }
*/

    public void updateArticleContent(final Content content) {
        //TODO: 偶尔抛出 NPE
        titleTextView.setText(content.getTitle());
        // enable marquee
        titleTextView.setSelected(true);
        timeTextView.setText(content.getTime());
        whereTextView.setText(content.getWhere());
        contentWebView.loadDataWithBaseURL("", content.getContent(), "text/html", "UTF-8", "");
    }

    public void updateCommentNumbers(Content content) {
        viewNumTextView.setText("" + content.getViewNum());
        commentNumTextView.setText("" + content.getCommentNum());
    }
    

    public void updateImage(final String id, final byte[] imageData) {
        // 在android代码中调用javaScript方法
        final String image64 = Base64.encodeToString(imageData, Base64.NO_WRAP);
//        imageData = "file://" + ((CnBetaApplication)getActivity().getApplicationContext()).getBaseDir().getAbsolutePath()+"/" + imageData;
        contentWebView.loadUrl("javascript:(function(){" +
                "var img = document.getElementById('" + id + "');"
                + "img.src='data:image/*;base64,"+ image64 + "';" +
                "})()");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(contentWebView != null) {
            contentWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(contentWebView != null) {
            contentWebView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if(contentWebView != null) {
            contentWebView.destroy();
            contentWebView = null;
        }
        super.onDestroy();
    }
}
