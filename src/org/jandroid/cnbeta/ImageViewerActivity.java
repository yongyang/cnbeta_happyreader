package org.jandroid.cnbeta;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import org.jandroid.cnbeta.loader.ImageBytesLoader;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.JavaScriptObject;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ImageViewerActivity extends BaseActivity {

    private ViewGroup rootContainer;
    private WebView imageWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);

        byte[] _imageData = {};
        String imageSrc = getIntent().getExtras().getString("src");
        try {
            _imageData = new ImageBytesLoader(imageSrc).diskLoad(((CnBetaApplication) getApplicationContext()).getBaseDir());
        }
        catch (Exception e) {
            logger.e(e.toString());
        }
        final byte[] imageData = _imageData;
        rootContainer = (ViewGroup) findViewById(R.id.imageviewer_container);

        //右上角的关闭按钮
        Button closeButton = (Button)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        imageWebView = (WebView)findViewById(R.id.imageviewer);
        imageWebView.getSettings().setBuiltInZoomControls(true);
        imageWebView.getSettings().setDisplayZoomControls(false); // but won't display the zoom buttons
        imageWebView.getSettings().setJavaScriptEnabled(true);
        imageWebView.addJavascriptInterface(new JavaScriptObject() {
            @JavascriptInterface
            public void close() {
                finish();
            }

        }, "JS");

        imageWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                updateImage("imgId", imageData);
            }
        });
        imageWebView.setWebChromeClient(new WebChromeClient());
        imageWebView.loadDataWithBaseURL("", "<center><img id='imgId' onclick='javascript:window.JS.close()' src='file:///android_asset/default_img.png'></center>", "text/html", "UTF-8", "");
    }

    public void updateImage(final String id, final byte[] imageData) {
        // 在android代码中调用javaScript方法
        handler.postDelayed(new Runnable() {
            public void run() {
                final String image64 = Base64.encodeToString(imageData, Base64.NO_WRAP);
                //        imageData = "file://" + ((CnBetaApplication)getActivity().getApplicationContext()).getBaseDir().getAbsolutePath()+"/" + imageData;
                imageWebView.loadUrl("javascript:(function(){" +
                        "var img = document.getElementById('" + id + "');"
                        + "img.src='data:image/*;base64," + image64 + "';" +
                        "})()");
            }
        }, 200);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (imageWebView != null) {
            imageWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (imageWebView != null) {
            imageWebView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(imageWebView != null) {
            imageWebView.stopLoading();
            imageWebView.clearCache(true);
            imageWebView.clearHistory();
            imageWebView.clearView();
            imageWebView.freeMemory();
            imageWebView.destroy();
            imageWebView = null;
        }
        rootContainer.removeAllViews();
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        // nothing
    }
}
