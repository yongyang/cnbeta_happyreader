package org.jandroid.cnbeta;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ImageViewerActivity extends BaseActivity {
//TODO: 右上角显示关闭按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);
        String imgSrc = getIntent().getExtras().getString("src");
        WebView webView = (WebView)findViewById(R.id.imageviewer);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
//        webView.getSettings().setUseWideViewPort(true);
        webView.loadDataWithBaseURL("", "<img src='" + imgSrc + "'>", "text/html", "UTF-8", "");
    }

    public static void main(String[] args) {

    }
}
