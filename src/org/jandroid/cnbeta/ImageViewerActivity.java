package org.jandroid.cnbeta;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import org.jandroid.cnbeta.loader.ImageBytesLoader;
import org.jandroid.common.JavaScriptObject;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ImageViewerActivity extends CnBetaThemeActivity {

    private WebView imageWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);
        this.setFinishOnTouchOutside(true);

        byte[] _imageData = {};
        String imageSrc = getIntent().getExtras().getString("src");
        try {
            _imageData = new ImageBytesLoader(imageSrc).diskLoad(((CnBetaApplication) getApplicationContext()).getCacheDir());
        }
        catch (Exception e) {
            logger.e(e.toString());
        }
        final byte[] imageData = _imageData;

        //右上角的关闭按钮
        ImageButton closeButton = (ImageButton)findViewById(R.id.closeButton);
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
        handler.post(new Runnable() {
            public void run() {
                final String image64 = Base64.encodeToString(imageData, Base64.NO_WRAP);
                //        imageData = "file://" + ((CnBetaApplication)getActivity().getApplicationContext()).getBaseDir().getAbsolutePath()+"/" + imageData;

                String javascript = "(function(){" +
                                        "var img = document.getElementById('" + id + "');"
                                        + "img.src='data:image/*;base64," + image64 + "';" +
                                        "})()";
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 19之前用loadUrl调用 javascript
                    imageWebView.evaluateJavascript(javascript, null);
                }
                else {
                    imageWebView.loadUrl("javascript:" + javascript);
                }
            }
        });
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        // nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true; // no optionsMenu
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public int getDarkThemeId() {
        return R.style.Theme_Dialog_cnBeta_Dark;
    }

    @Override
    public int getLightThemeId() {
        return R.style.Theme_Dialog_cnBeta_Light;
    }

}
