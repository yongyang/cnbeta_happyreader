package org.jandroid.cnbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.jandroid.util.IntentUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Utils {

    public static void openContentActivity(Activity theActivity, long sid, String title) {
        Bundle bundle = new Bundle();
        bundle.putLong("sid", sid);
        bundle.putString("title", title);
        Intent intent = IntentUtils.newIntent(theActivity, ContentActivity.class, bundle);
        theActivity.startActivity(intent);
    }

    public static void openImageViewerActivity(Activity theActivity, String imgSrc) {
        Bundle bundle = new Bundle();
        bundle.putString("src", imgSrc);
        Intent intent = IntentUtils.newIntent(theActivity, ImageViewerActivity.class, bundle);
        theActivity.startActivity(intent);
    }

}
