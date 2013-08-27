package org.jandroid.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 返回各种 Intent, 方便调用者去 startActivity startService
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class IntentUtils {
    
    public static Intent newIntent(Activity thisActivity, Class<?> targetActivityClass) {
        return newIntent(thisActivity, targetActivityClass, null);
    }

    public static Intent newIntent(Activity thisActivity, Class<?> targetActivityClass, Bundle bundle) {
   		Intent intent=new Intent();
   		intent.setClass(thisActivity,targetActivityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
   		return intent;
   	}

/*
    public static void startActivity(Activity activity, Class<?> cls, Bundle bundle, int enterAnim, int exitAnim) {
        newIntent(activity, cls, bundle);
   		activity.overridePendingTransition(enterAnim, exitAnim);
   	}
*/

    public static Intent newIntent(Activity thisActivity, String pAction) {
   		return startActivity(thisActivity, pAction, null);
   	}

   	public static Intent startActivity(Activity thisActivity, String pAction, Bundle pBundle) {
   		Intent intent = new Intent(pAction);
   		if (pBundle != null) {
   			intent.putExtras(pBundle);
   		}
   		return intent;
   	}

    public static Intent recommendToYourFriend(Activity activity, String url, String shareTitle) {
   		Intent intent = new Intent(Intent.ACTION_SEND);
   		intent.setType("text/plain");
   		intent.putExtra(Intent.EXTRA_TEXT, shareTitle + "   " + url);
   		Intent chooserIntent = Intent.createChooser(intent, "分享");
   		return chooserIntent;
   	}

}
