package org.jandroid.cnbeta;

import android.os.Bundle;
import org.jandroid.common.BaseActivity;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class PublishCommentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_publish);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
