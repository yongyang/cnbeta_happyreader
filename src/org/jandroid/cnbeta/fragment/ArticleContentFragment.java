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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import org.jandroid.cnbeta.ContentActivity;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.Logger;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentFragment extends BaseFragment {

    static Logger log = Logger.getLogger(ArticleContentFragment.class);

    private TextView titleTextView;
    private TextView timeTextView;
    private TextView whereTextView;
    private TextView viewNumTextView;
    private TextView commentNumTextView;
    private RatingBar ratingBar;
    private WebView contentWebView;

    private LinearLayout loadingLayout;

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
        loadingLayout = (LinearLayout) root.findViewById(R.id.loadingLayout);
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
                loadingLayout.setVisibility(View.GONE);
                contentWebView.setVisibility(View.VISIBLE);
                //load images here, after Page Loaded
                ((ContentActivity)getActivity()).loadImages();
                //load comments and view_num, comment_num etc
                ((ContentActivity)getActivity()).loadComments();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ratingBar.setVisibility(View.VISIBLE);
                    }
                }, 1000);

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
