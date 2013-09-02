package org.jandroid.common;

import android.app.Fragment;
import android.os.AsyncTask;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 8/27/13 2:45 PM
 */
public abstract class BaseFragment extends Fragment {
    
    protected Logger logger = Logger.getLogger(this.getClass());

    public synchronized void executeAsyncTaskMultiThreading(AsyncTask asyncTask){
        ((BaseActivity) getActivity()).executeAsyncTaskMultiThreading(asyncTask);
    }
}
