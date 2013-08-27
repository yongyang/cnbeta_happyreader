package org.jandroid.cnbeta;

import android.R;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.Logger;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class DownloaderActivity extends BaseActivity {

    protected Logger logger = Logger.newLogger(this.getClass());

   	protected AlertDialog mAlertDialog;
   	protected AsyncTask mRunningTask;

   	/******************************** 【Activity LifeCycle For Debug】 *******************************************/
   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
        logger.d("onCreate() invoked!!");
   		super.onCreate(savedInstanceState);
   		requestWindowFeature(Window.FEATURE_NO_TITLE);
   	}

   	@Override
   	protected void onStart() {
   		logger.d(" onStart() invoked!!");
   		super.onStart();
   	}

   	@Override
   	protected void onRestart() {
   		logger.d(" onRestart() invoked!!");
   		super.onRestart();
   	}

   	@Override
   	protected void onResume() {
   		logger.d(" onResume() invoked!!");
   		super.onResume();
   	}

   	@Override
   	protected void onPause() {
   		logger.d(" onPause() invoked!!");
   		super.onPause();
   	}

   	@Override
   	protected void onStop() {
   		logger.d(" onStop() invoked!!");
   		super.onStop();
   	}

   	@Override
   	public void onDestroy() {
   		logger.d(" onDestroy() invoked!!");
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
   		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
   	}

   	public void finish()	{
   		super.finish();
   	}

}
