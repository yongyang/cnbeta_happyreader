package org.jandroid.common;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import org.jandroid.cnbeta.client.CnBetaHttpClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class BaseService extends Service {

    protected Handler handler = new Handler();
    
    protected Logger logger = Logger.getLogger(this.getClass());
    
    protected List<AsyncTask> runningTasks = new ArrayList<AsyncTask>();

    public static final int MULTI_THREADING_SLOW_DOWN_VALVE = (int)(CnBetaHttpClient.MAX_TOTAL_CONNECTIONS/2);
    public static final int DELAY_TIME_MILLIS = 1000;

   	/******************************** 【Activity LifeCycle For Debug】 *******************************************/
   	@Override
   	public void onCreate() {
        logger.d("onCreate() invoked!!");
   		super.onCreate();
   	}

   	@Override
   	public void onStart(Intent intent, int startId) {
   		logger.d(" onStart() invoked!!");
   		super.onStart(intent, startId);
   	}


   	@Override
   	public void onRebind(Intent intent) {
   		logger.d(" onRebind() invoked!!");
   		super.onRebind(intent);
   	}


    public synchronized void executeAsyncTask(final AsyncTask<?, ?, ?> asyncTask, Object... params){
        runningTasks.add(asyncTask);
        asyncTask.execute(params);
    }

    public synchronized void executeAsyncTaskMultiThreading(final AsyncTask<?, ?, ?> asyncTask, Object... params){
        logger.d(" executeAsyncTaskMultiThreading() running tasks: " + runningTasks.size());
        for(Iterator<AsyncTask> it = runningTasks.iterator(); it.hasNext();){
            AsyncTask runningTask = it.next();
            if(runningTask.isCancelled() || runningTask.getStatus() == AsyncTask.Status.FINISHED) {
                it.remove();
            }
        }
        runningTasks.add(asyncTask);
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    public synchronized void cancelAsyncTasks(){
        for(Iterator<AsyncTask> it = runningTasks.iterator(); it.hasNext();){
            AsyncTask runningTask = it.next();
            if(!runningTask.isCancelled() || runningTask.getStatus() != AsyncTask.Status.FINISHED) {
                boolean cancelled = runningTask.cancel(true);
            }
            it.remove();
        }
    }

   	@Override
   	public void onDestroy() {
        logger.d(" onDestroy() invoked!!");
        handler.removeCallbacksAndMessages(null);
   		super.onDestroy();
        //  Service don't cancel Async Tasks automatically
//        cancelAsyncTasks();
   	}

}
