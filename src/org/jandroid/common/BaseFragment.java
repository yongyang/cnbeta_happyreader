package org.jandroid.common;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 8/27/13 2:45 PM
 */
public abstract class BaseFragment extends Fragment {
    
    protected Logger logger = Logger.getLogger(this.getClass());

    protected Handler handler = new Handler();

    protected Activity theActivity;

    @Override
   	public void onCreate(Bundle savedInstanceState) {
        logger.d("onCreate() invoked!!");
   		super.onCreate(savedInstanceState);
   	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        logger.d("onViewCreated() invoked!!");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        logger.d("onActivityCreated() invoked!!");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        theActivity = activity;
    }

    @Override
   	public void onStart() {
   		logger.d(" onStart() invoked!!");
   		super.onStart();
   	}

   	@Override
   	public void onResume() {
   		logger.d(" onResume() invoked!!");
   		super.onResume();
   	}

   	@Override
   	public void onPause() {
   		logger.d(" onPause() invoked!!");
   		super.onPause();
   	}

   	@Override
   	public void onStop() {
   		logger.d(" onStop() invoked!!");
   		super.onStop();
   	}


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        logger.d(" onDestroy() invoked!!");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        logger.d(" onDestroyView() invoked!!");
        super.onDestroyView();
    }

    public synchronized void executeAsyncTaskMultiThreading(AsyncTask asyncTask){
        Activity activity = getActivity();
        if(activity != null) {
            ((BaseActivity) activity).executeAsyncTaskMultiThreading(asyncTask);
        }
    }
}
