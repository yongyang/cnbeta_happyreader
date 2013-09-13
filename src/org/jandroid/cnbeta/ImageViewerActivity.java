package org.jandroid.cnbeta;

import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import org.jandroid.cnbeta.loader.ImageBytesLoader;
import org.jandroid.common.BaseActivity;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ImageViewerActivity extends BaseActivity {

    private ViewGroup topContainer;
    private WebView imageWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);

        byte[] imageData = {};
        String imageSrc = getIntent().getExtras().getString("src");
        try {
            imageData = new ImageBytesLoader(imageSrc).fromDisk(((CnBetaApplication)getApplicationContext()).getBaseDir());
        }
        catch (Exception e) {

        }
        final String image64 = Base64.encodeToString(imageData, Base64.NO_WRAP);
        topContainer = (ViewGroup) findViewById(R.id.imageviewer_container);

        //右上角的关闭按钮
        Button closeButton = (Button)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        imageWebView = (WebView)findViewById(R.id.imageviewer);
        imageWebView.getSettings().setBuiltInZoomControls(true);
        imageWebView.getSettings().setJavaScriptEnabled(true);
        imageWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void close() {
                finish();
            }

        }, "JS");

        imageWebView.loadDataWithBaseURL("", "<img onclick='javascript:window.JS.close()' src='data:image/*;base64," + image64 + "'>", "text/html", "UTF-8", "");
    }


    @Override
    public void onDestroy() {
        topContainer.removeAllViews();
        if(imageWebView != null) {
            imageWebView.destroy();
            imageWebView = null;
        }
        super.onDestroy();
    }

}
