package org.jandroid.cnbeta;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ImageViewerActivity extends BaseActivity {

    private ViewGroup topContainer;
    private WebView imageWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String imgSrc = getIntent().getExtras().getString("src");
        setContentView(R.layout.image_viewer);
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
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setDisplayZoomControls(true);
//        webView.getSettings().setUseWideViewPort(true);
        imageWebView.loadDataWithBaseURL("", "<img src='" + imgSrc + "'>", "text/html", "UTF-8", "");
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
