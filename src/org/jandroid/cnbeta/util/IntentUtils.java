package org.jandroid.cnbeta.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class IntentUtils {

    public static void startActivity(Activity activity, Class<?> cls) {
        startActivity(activity, cls, null);
   	}

    public static void startActivity(Activity activity, Class<?> cls, Bundle bundle) {
   		Intent intent=new Intent();
   		intent.setClass(activity,cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
   		activity.startActivity(intent);
   	}

    public static void startActivity(Activity activity, Class<?> cls, Bundle bundle, int enterAnim, int exitAnim) {
        startActivity(activity, cls, bundle);
   		activity.overridePendingTransition(enterAnim, exitAnim);
   	}

    public static void startActivity(Activity activity, String pAction) {
   		startActivity(activity, pAction, null);
   	}

   	public static void startActivity(Activity activity, String pAction, Bundle pBundle) {
   		Intent intent = new Intent(pAction);
   		if (pBundle != null) {
   			intent.putExtras(pBundle);
   		}
   		activity.startActivity(intent);
   	}

    public static void recommendToYourFriend(Activity activity, String url, String shareTitle) {
   		Intent intent = new Intent(Intent.ACTION_SEND);
   		intent.setType("text/plain");
   		intent.putExtra(Intent.EXTRA_TEXT, shareTitle + "   " + url);
   		Intent chooserIntent = Intent.createChooser(intent, "分享");
   		activity.startActivity(chooserIntent);
   	}

}
