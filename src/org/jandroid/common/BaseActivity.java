package org.jandroid.common;

import android.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class BaseActivity extends Activity {

    protected Handler handler = new Handler();
    
    protected Logger logger = Logger.getLogger(this.getClass());
    
    protected List<AsyncTask> runningTasks = new ArrayList<AsyncTask>();

   	/******************************** 【Activity LifeCycle For Debug】 *******************************************/
   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
        logger.d("onCreate() invoked!!");
   		super.onCreate(savedInstanceState);
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

    public synchronized void executeAsyncTaskMultiThreading(AsyncTask asyncTask){
        for(Iterator<AsyncTask> it = runningTasks.iterator(); it.hasNext();){
            AsyncTask runningTask = it.next();
            if(runningTask.isCancelled() || runningTask.getStatus() == AsyncTask.Status.FINISHED) {
                it.remove();
            }
        }
        runningTasks.add(asyncTask);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

   	@Override
   	public void onDestroy() {
   		logger.d(" onDestroy() invoked!!");
   		super.onDestroy();

        for(Iterator<AsyncTask> it = runningTasks.iterator(); it.hasNext();){
            AsyncTask runningTask = it.next();
            if(!runningTask.isCancelled() || runningTask.getStatus() != AsyncTask.Status.FINISHED) {
                runningTask.cancel(true);
            }
            it.remove();
        }
   	}

    public void finishWithSlideAnimation() 	{
    		super.finish();
    		overridePendingTransition(android.R.anim.slide_in_left, R.anim.slide_out_right);
    }

  	public void finish(int enterAnimation, int exitAnimation) 	{
   		super.finish();
   		overridePendingTransition(enterAnimation, exitAnimation);
   	}

   	public void finish()	{
   		super.finish();
   	}

}
