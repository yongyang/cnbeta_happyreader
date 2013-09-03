package org.jandroid.cnbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.cnbeta.entity.HistoryArticle;
import org.jandroid.cnbeta.loader.HistoryArticleListLoader;
import org.jandroid.common.DateFormatUtils;
import org.jandroid.common.IntentUtils;
import org.jandroid.common.ToastUtils;
import org.json.simple.JSONObject;

import java.util.Date;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Utils {

    public static void openContentActivity(final Activity theActivity, final long sid, final String title) {
        Bundle bundle = new Bundle();
        bundle.putLong("sid", sid);
        bundle.putString("title", title);
        Intent intent = IntentUtils.newIntent(theActivity, ContentActivity.class, bundle);
        theActivity.startActivity(intent);

        // write history
        new Thread(){
            @Override
            public void run() {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("sid", sid);
                jSONObject.put("title", title);
                jSONObject.put("date", DateFormatUtils.getDefault().format(new Date()));
                HistoryArticle historyArticle = new HistoryArticle(jSONObject);
                try {
                    new HistoryArticleListLoader().writeHistory(((CnBetaApplicationContext)theActivity.getApplicationContext()).getBaseDir(), historyArticle);
                }
                catch (final Exception e) {
                    theActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            ToastUtils.showShortToast(theActivity.getApplicationContext(), e.toString());
                        }
                    });
                }
            }
        }.start();
    }

    public static void openPublishCommentActivity(Activity theActivity, Content content) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", content);
        Intent intent = IntentUtils.newIntent(theActivity, PublishCommentActivity.class, bundle);
        theActivity.startActivity(intent);
    }

    public static void openMainActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, MainActivity.class);
        theActivity.startActivity(intent);
    }

    public static void openTopicActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, TopicActivity.class);
        theActivity.startActivity(intent);
    }

    public static void openTypesActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, TypesActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void openMRankActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, MRankActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void openHistoryActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, HistoryActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
