package org.jandroid.common;

import android.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import org.jandroid.cnbeta.client.CnBetaHttpClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class BaseActivity extends Activity {

    //用来生成随机数，用于 postDelay，以分散线程创建和执行，提速
    private static final Random random = new Random();

    protected Handler handler = new Handler();
    
    protected Logger logger = Logger.getLogger(this.getClass());
    
    protected List<AsyncTask> runningTasks = new ArrayList<AsyncTask>();

    public static final int MULTI_THREADING_SLOW_DOWN_VALVE = (int)(CnBetaHttpClient.MAX_TOTAL_CONNECTIONS/2);
    public static final int DELAY_TIME_MILLIS = 1000;

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
/*
        pendingExecutor.scheduleWithFixedDelay(new Runnable() {
            public void run() {

            }
        }, 500, 500, TimeUnit.MILLISECONDS);
*/

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

    public synchronized void executeAsyncTaskMultiThreading(final AsyncTask asyncTask){
        logger.d(" executeAsyncTaskMultiThreading() running tasks: " + runningTasks.size());
        for(Iterator<AsyncTask> it = runningTasks.iterator(); it.hasNext();){
            AsyncTask runningTask = it.next();
            if(runningTask.isCancelled() || runningTask.getStatus() == AsyncTask.Status.FINISHED) {
                it.remove();
            }
        }

        //Slow down to avoid rejecting
//      java.util.concurrent.ThreadPoolExecutor@b11d4808[Running, pool size = 128, active threads = 127, queued tasks = 10, completed tasks = 527]
//      09-11 18:55:30.566: E/AndroidRuntime(2088): 	at java.util.concurrent.ThreadPoolExecutor$AbortPolicy.rejectedExecution(ThreadPoolExecutor.java:1967)
        if(runningTasks.size() > MULTI_THREADING_SLOW_DOWN_VALVE) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    executeAsyncTaskMultiThreading(asyncTask);
                }
            }, random.nextInt(DELAY_TIME_MILLIS));
            return;
        }

        runningTasks.add(asyncTask);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

   	@Override
   	public void onDestroy() {
   		logger.d(" onDestroy() invoked!!  Active async task: " + ((ThreadPoolExecutor)AsyncTask.THREAD_POOL_EXECUTOR).getActiveCount());
   		super.onDestroy();
        for(Iterator<AsyncTask> it = runningTasks.iterator(); it.hasNext();){
            AsyncTask runningTask = it.next();
            if(!runningTask.isCancelled() || runningTask.getStatus() != AsyncTask.Status.FINISHED) {
                boolean cancelled = runningTask.cancel(true);
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
