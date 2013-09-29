package org.jandroid.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

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

    public static Intent homeIntent(Activity activity) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return mHomeIntent;
    }

    public Intent installAPK(String file){
        return openFile(new File(file));
    }

    //在手机上打开各种类型的文件
    public static Intent openFile(File file)  {
    	Intent intent = new Intent();
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	intent.setAction(android.content.Intent.ACTION_VIEW);
    	// 来取得MimeType
    	String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.toString()));
    	//设定intent的file与MimeType
    	intent.setDataAndType(Uri.fromFile(file), type);
        return intent;
    }

}
