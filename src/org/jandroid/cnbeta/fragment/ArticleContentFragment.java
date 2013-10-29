package org.jandroid.cnbeta.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.CnBetaPreferences;
import org.jandroid.cnbeta.ContentActivity;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.Utils;
import org.jandroid.cnbeta.async.ArticleContentAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.ImageBytesAsyncTask;
import org.jandroid.cnbeta.async.RateArticleAsyncTask;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.FontUtils;
import org.jandroid.common.JavaScriptObject;
import org.jandroid.common.PixelUtils;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ArticleContentFragment extends BaseFragment implements HasAsync<Content> {

    private Content content;

    private TextView titleTextView;
    private TextView timeTextView;
    private TextView whereTextView;
    private TextView viewNumTextView;
    private TextView commentNumTextView;
    private WebView contentWebView;
    private LinearLayout progressBarLayout;

    private RatingBar rateRatingBar;
    private RatingBar resultRatingBar;
    private ProgressBar ratingProgressBar;

    private ViewGroup root;

    private boolean loaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loaded = false;
        root = (ViewGroup) inflater.inflate(R.layout.content_article, null);
        titleTextView = (TextView) root.findViewById(R.id.tv_articleTitle);
        titleTextView.setText(((ContentActivity) getActivity()).getArticleTitle());
        titleTextView.setSelected(true); // select to enable marque
        timeTextView = (TextView) root.findViewById(R.id.tv_date);
        viewNumTextView = (TextView) root.findViewById(R.id.tv_viewNum);
        commentNumTextView = (TextView) root.findViewById(R.id.tv_commentNum);
        whereTextView = (TextView) root.findViewById(R.id.tv_where);

        rateRatingBar = (RatingBar) root.findViewById(R.id.rate_ratingBar);
        resultRatingBar = (RatingBar) root.findViewById(R.id.result_ratingBar);
        ratingProgressBar = (ProgressBar) root.findViewById(R.id.rating_progressBar);
        setupRatingBar();

        progressBarLayout = (LinearLayout) root.findViewById(R.id.progressBarLayout);
        contentWebView = (WebView) root.findViewById(R.id.wv_articleContent);
        setupWebView();
        return root;
    }

    private void setupRatingBar() {
        rateRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ArticleContentFragment.this.rateRatingBar.setIsIndicator(true);
                final int score = (int) (2 * ArticleContentFragment.this.rateRatingBar.getRating() - 5);
                ToastUtils.showShortToast(getActivity(), "您的评分: " + score);
                executeAsyncTaskMultiThreading(new RateArticleAsyncTask() {
                    @Override
                    protected long getSid() {
                        return ((ContentActivity) getActivity()).getArticleSid();
                    }

                    @Override
                    protected int getScore() {
                        return score;
                    }

                    @Override
                    public HasAsync<JSONObject> getAsyncContext() {
                        return new HasAsync<JSONObject>() {
                            public CnBetaApplicationContext getCnBetaApplicationContext() {
                                return (CnBetaApplicationContext) (getActivity().getApplicationContext());
                            }

                            public void onProgressShow() {
                                rateRatingBar.setVisibility(View.GONE);
                                ratingProgressBar.setVisibility(View.VISIBLE);
                                resultRatingBar.setVisibility(View.GONE);
                            }

                            public void onProgressDismiss() {
                                rateRatingBar.setVisibility(View.GONE);
                                ratingProgressBar.setVisibility(View.GONE);
                                resultRatingBar.setVisibility(View.VISIBLE);
                            }

                            public void onSuccess(AsyncResult<JSONObject> jsonObjectAsyncResult) {
                                //{"status":"success","result":{"average":"0.6","count":"10"}}
                                JSONObject resultJSON = jsonObjectAsyncResult.getResult();
                                if (resultJSON.get("status").toString().equals("success")) {
                                    String average = ((JSONObject) resultJSON.get("result")).get("average").toString();
                                    String count = ((JSONObject) resultJSON.get("result")).get("count").toString();
                                    ToastUtils.showLongToast(getActivity(), "当前平均分: " + average + " (共 " + count + " 次打分)");
                                    ArticleContentFragment.this.resultRatingBar.setRating((5.0f + Float.parseFloat(average)) / 2);
                                }
                                else {
                                    String message = ((JSONObject) resultJSON.get("result")).get("message").toString();
                                    ToastUtils.showShortToast(getActivity(), message);
                                }
                            }

                            public void onFailure(AsyncResult<JSONObject> jsonObjectAsyncResult) {

                            }
                        };
                    }
                });
            }
        });

    }

    private void setupWebView() {
        // work weird
//        contentWebView.setBackgroundColor(R.color.cnBeta_bg_introduction);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        //设置嫩参数
        contentWebView.getSettings().setDefaultFontSize((int) PixelUtils.pixelsToSp(getActivity(), getResources().getDimension(R.dimen.webview_default_text_size)));
        contentWebView.getSettings().setDefaultFixedFontSize((int) PixelUtils.pixelsToSp(getActivity(), getResources().getDimension(R.dimen.webview_default_text_size)));
        contentWebView.getSettings().setAllowFileAccess(true);
        // no these two method in 4.0
//        contentWebView.getSettings().setAllowFileAccessFromFileURLs(true);
//        contentWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //支持视频，需安装 Flash Player
        contentWebView.getSettings().setFixedFontFamily("sans");
        contentWebView.getSettings().setFantasyFontFamily("sans");
        contentWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        contentWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // no scroll
        contentWebView.getSettings().setBuiltInZoomControls(true);
        contentWebView.getSettings().setDisplayZoomControls(false); // but won't display the zoom buttons
        contentWebView.getSettings().setLoadWithOverviewMode(true);
        contentWebView.getSettings().setAppCacheEnabled(false);
        contentWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        contentWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        contentWebView.getSettings().setLoadsImagesAutomatically(true);
        contentWebView.getSettings().setBlockNetworkImage(true);
        contentWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        contentWebView.getSettings().setLightTouchEnabled(true);
        contentWebView.getSettings().setSavePassword(true);
        contentWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        contentWebView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36");
        contentWebView.requestFocus();
        contentWebView.requestLayout();

        // resize big image to fit screen width
        contentWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        contentWebView.addJavascriptInterface(new JavaScriptObject() {
            @JavascriptInterface
            public void openImage(String imgSrc) {
                //新开一个 Transparent Activity, 使用 WebView 打开大图
                Utils.openImageViewerActivity(getActivity(), imgSrc);
                //TODO: try to save image first, in case cache cleared, WONT_FIX
            }

            @JavascriptInterface
            public void openTopic(String topicId, String topicName) {
//                ToastUtils.showShortToast(getActivity(), "openTopic id=" + topicId +", name=" + topicName);
                Utils.openTopicActivity(getActivity(), Long.parseLong(topicId), topicName);
            }

        }, "JS");

        contentWebView.setWebViewClient(new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
/*
                if (!url.contains("image")) {
                    ToastUtils.showShortToast(getActivity(), url);
                }
*/
                //NOTE!!!! 过滤掉这些 url，来自优酷视频，可以去掉广告
                if (
                        url.contains("atm.youku.com")
                                || url.contains("stat.youku.com")
//                                || url.contains("stat.ykimg.com")
                                || url.contains("log.ykimg.com")
                                || url.contains("scorecardresearch.com")
                                || url.contains("irs01.com")
                        ) {

/*
                    if (!url.contains("image")) {
                        ToastUtils.showShortToast(getActivity(), "Cancel, " + url);
                    }
*/
                    return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));
                }

                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loaded = true;
                contentWebView.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.GONE);

                //Delay loading images comments to give the contentWebView high priority to paint

                //load images here, after Page Loaded
                handler.postDelayed(new Runnable() {
                    public void run() {
                        loadImages();
                    }
                }, 100);

                //Stat to load comments and view_num, comment_num etc
                //!!!NOTE: this is the best point to start to load comments, after content page loaded
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ((ContentActivity) getActivity()).reloadComments();
                    }
                }, 500);

                // 延迟显示，以免早于显示 WebView paint完成之前显示
                handler.postDelayed(new Runnable() {
                    public void run() {
                        rateRatingBar.setVisibility(View.VISIBLE);
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
                logger.e("ERROR: " + errorCode + ", " + description + ", " + failingUrl);
//                ToastUtils.showShortToast(getActivity(), "ERROR: " + errorCode + ", " + description + ", " + failingUrl);
            }
        });

        // 处理消息 和 Alert
        contentWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                logger.d("onConsoleMessage" + cm.message() + " -- line " + cm.lineNumber() + " of " + cm.sourceId());
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                logger.d("JSAlert: " + message);
                return true;
            }

        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        reloadContent();
    }

    public Content getContent() {
        return content;
    }

    public void updateCommentNumbers() {
        // Content viewNum has been updated in ArticleCommentsLoader
        handler.post(new Runnable() {
            public void run() {
                viewNumTextView.setText("" + content.getViewNum());
                commentNumTextView.setText("" + content.getCommentNum());
            }
        });
    }


    public void updateImage(final String id, final byte[] imageData) {
        // 在android代码中调用javaScript方法
        handler.post(new Runnable() {
            public void run() {
                final String image64 = Base64.encodeToString(imageData, Base64.NO_WRAP);
                //        imageData = "file://" + ((CnBetaApplication)getActivity().getApplicationContext()).getBaseDir().getAbsolutePath()+"/" + imageData;
                if (contentWebView != null) {
                    contentWebView.loadUrl("javascript:(function(){" +
                            "var img = document.getElementById('" + id + "');"
                            + "img.src='data:image/*;base64," + image64 + "';" +
                            "})()");
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (contentWebView != null) {
            //NOTE!!! This will pause all WebView, and then the ImageViewerActivity can NOT load image
//            contentWebView.pauseTimers();
            contentWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int fontSizeOffset = ((CnBetaApplicationContext) getActivity().getApplicationContext()).getCnBetaPreferences().getFontSizeOffset();
        if (contentWebView != null) {
            FontUtils.updateTextSize(getActivity(), contentWebView, R.dimen.webview_default_text_size, fontSizeOffset);

            // Try resumeTimers anyway, flash plugin may case pauseTimers
            contentWebView.resumeTimers();
            contentWebView.onResume();
        }

        // update font size
        FontUtils.updateTextSize(getActivity(), titleTextView, R.dimen.listitem_title_text_size, fontSizeOffset);
        FontUtils.updateTextSize(getActivity(), timeTextView, R.dimen.listitem_status_text_size, fontSizeOffset);
        FontUtils.updateTextSize(getActivity(), viewNumTextView, R.dimen.listitem_status_text_size, fontSizeOffset);
        FontUtils.updateTextSize(getActivity(), commentNumTextView, R.dimen.listitem_status_text_size, fontSizeOffset);
        FontUtils.updateTextSize(getActivity(), whereTextView, R.dimen.listitem_status_text_size, fontSizeOffset);

        CnBetaPreferences pref = ((CnBetaApplicationContext) getActivity().getApplicationContext()).getCnBetaPreferences();
        FontUtils.changeFont(getView(), pref.getCustomFontTypeface());
        if (loaded) { // reload
            contentWebView.loadDataWithBaseURL(null, getStyledHTMLContent(content), "text/html", "UTF-8", "about:blank");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contentWebView != null) {
            // Stopping a webview and all of the background processes (flash,
            // javascript, etc) is a very big mess.
            // The following steps are to counter most of the issues seen on
            // internals still going on after the webview is destroyed.
            contentWebView.pauseTimers();
            contentWebView.stopLoading();
            contentWebView.clearCache(true);
            contentWebView.clearHistory();
            contentWebView.clearView();
            contentWebView.freeMemory();
            contentWebView.setWebChromeClient(null);
            contentWebView.setWebViewClient(null);
            //try release Flash Player plugin
            contentWebView.getSettings().setPluginState(WebSettings.PluginState.OFF);
            contentWebView.loadData("", "text/html", "utf-8");
            //try release Flash Player plugin
            contentWebView.getSettings().setPluginState(WebSettings.PluginState.OFF);
            contentWebView.reload();
            contentWebView.stopLoading();
            contentWebView.clearCache(true);
            contentWebView.clearHistory();
            contentWebView.clearView();
            contentWebView.freeMemory();
            contentWebView.removeAllViews();
            if (root != null) {
                root.removeAllViews();
            }
            contentWebView.destroy();
            contentWebView = null;
            logger.d("Content WebView destroyed.");
        }
    }

    public void reloadContent() {
        executeAsyncTaskMultiThreading(new ArticleContentAsyncTask() {

            @Override
            protected long getSid() {
                return ((ContentActivity) getActivity()).getArticleSid();
            }

            @Override
            public HasAsync<Content> getAsyncContext() {
                return ArticleContentFragment.this;
            }
        }
        );
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return (CnBetaApplicationContext) getActivity().getApplicationContext();
    }

    public void onProgressShow() {
        getActivity().setProgressBarIndeterminate(true);
        getActivity().setProgressBarVisibility(true);

        //由 WebView.onPageFishi 负责切换显示
        contentWebView.setVisibility(View.GONE);
        rateRatingBar.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    public void onProgressDismiss() {
        getActivity().setProgressBarVisibility(false);
    }

    public void onSuccess(AsyncResult<Content> contentAsyncResult) {
        //update content in ContentActivity
        this.updateContent(contentAsyncResult.getResult());
    }

    private void updateContent(final Content _content) {
        handler.post(new Runnable() {
            public void run() {
                content = _content;
                titleTextView.setText(content.getTitle());
                // enable marquee
                titleTextView.setSelected(true);
                timeTextView.setText(content.getTime());
                whereTextView.setText(content.getWhere());
                contentWebView.loadDataWithBaseURL(null, getStyledHTMLContent(content), "text/html", "UTF-8", "about:blank");
            }
        });
    }

    private String getStyledHTMLContent(Content content) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><style type=\"text/css\">");
        // 设置 custom Font
        String customFont = ((CnBetaApplicationContext) getActivity().getApplicationContext()).getCnBetaPreferences().getCustomFont();
        if (customFont != null && !customFont.isEmpty() && !customFont.equals("default")) {
            sb.append("@font-face{ font-family: customFont; src:url('" + customFont + "');" + "} body {font-family: 'customFont';}");
        }

        sb.append("</style></head>");
        sb.append("<body>");
        sb.append(content.getContent());
        sb.append("</body></html>");
        return sb.toString();
    }


    public void newPostedComment(Comment comment) {
        content.setJoinNum(content.getJoinNum() + 1);
    }

    public void onFailure(AsyncResult<Content> contentAsyncResult) {
        progressBarLayout.setVisibility(View.GONE);
    }


    private void loadImages() {
        for (String image : content.getImages()) {
            loadImage(image);
        }
    }

    private void loadImage(final String imgSrc) {
        executeAsyncTaskMultiThreading(new ImageBytesAsyncTask() {
            @Override
            protected String getImageUrl() {
                return imgSrc;
            }

            @Override
            public HasAsync<byte[]> getAsyncContext() {
                return new HasAsync<byte[]>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplicationContext) getActivity().getApplicationContext();
                    }

                    public void onProgressShow() {

                    }

                    public void onProgressDismiss() {

                    }

                    public void onSuccess(AsyncResult<byte[]> asyncResult) {
                        String id = Base64.encodeToString(imgSrc.getBytes(), Base64.NO_WRAP);
                        //update image in WebView by javascript
                        updateImage(id, asyncResult.getResult());
                    }

                    public void onFailure(AsyncResult<byte[]> asyncResult) {

                    }
                };
            }
        }
        );
    }
}
