package org.jandroid.cnbeta;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import org.jandroid.util.LogUtil;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class BaseActivity extends Activity {

    private static final String TAG = "BaseActivity";

   	protected AlertDialog mAlertDialog;
   	protected AsyncTask mRunningTask;

   	/******************************** 【Activity LifeCycle For Debug】 *******************************************/
   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
   		LogUtil.d(TAG, this.getClass().getSimpleName()
                + " onCreate() invoked!!");
   		super.onCreate(savedInstanceState);
   		requestWindowFeature(Window.FEATURE_NO_TITLE);
   	}

   	@Override
   	protected void onStart() {
   		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStart() invoked!!");
   		super.onStart();
   	}

   	@Override
   	protected void onRestart() {
   		LogUtil.d(TAG, this.getClass().getSimpleName()
   				+ " onRestart() invoked!!");
   		super.onRestart();
   	}

   	@Override
   	protected void onResume() {
   		LogUtil.d(TAG, this.getClass().getSimpleName()
                + " onResume() invoked!!");
   		super.onResume();
   	}

   	@Override
   	protected void onPause() {
   		LogUtil.d(TAG, this.getClass().getSimpleName() + " onPause() invoked!!");
   		super.onPause();
   	}

   	@Override
   	protected void onStop() {
   		LogUtil.d(TAG, this.getClass().getSimpleName() + " onStop() invoked!!");
   		super.onStop();
   	}

   	@Override
   	public void onDestroy() {
   		LogUtil.d(TAG, this.getClass().getSimpleName()
   				+ " onDestroy() invoked!!");
   		super.onDestroy();

   		if (mRunningTask != null && mRunningTask.isCancelled() == false) {
   			mRunningTask.cancel(false);
   			mRunningTask = null;
   		}
   		if (mAlertDialog != null) {
   			mAlertDialog.dismiss();
   			mAlertDialog = null;
   		}
   	}

  	public void finishAnim() 	{
   		super.finish();
   		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_right);
   	}

   	public void finish()	{
   		super.finish();
   	}

}
