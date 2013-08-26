package org.jandroid.cnbeta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ImageViewerActivity extends BaseActivity {
//TODO: 右上角显示关闭按钮
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);
        String imgSrc = getIntent().getExtras().getString("src");
        Button closeButton = (Button)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView)findViewById(R.id.imageviewer);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
//        webView.getSettings().setUseWideViewPort(true);
        webView.loadDataWithBaseURL("", "<img src='" + imgSrc + "'>", "text/html", "UTF-8", "");
    }



    @Override
    public void onDestroy() {
        if(webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    public static void main(String[] args) {

    }
}
