package org.jandroid.cnbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.common.IntentUtils;

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

    public static void openPublishCommentActivity(Activity theActivity, Content content) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", content);
        Intent intent = IntentUtils.newIntent(theActivity, PublishCommentActivity.class, bundle);
        theActivity.startActivity(intent);
    }

    public static void openReplyCommentActivity(Activity theActivity, Content content, Comment comment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", content);
        bundle.putSerializable("comment", comment);
        Intent intent = IntentUtils.newIntent(theActivity, ReplyCommentActivity.class, bundle);
        theActivity.startActivity(intent);
    }

    public static void openImageViewerActivity(Activity theActivity, String imgSrc) {
        Bundle bundle = new Bundle();
        bundle.putString("src", imgSrc);
        Intent intent = IntentUtils.newIntent(theActivity, ImageViewerActivity.class, bundle);
        theActivity.startActivity(intent);
    }

}
