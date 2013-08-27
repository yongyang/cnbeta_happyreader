package org.jandroid.util;

import android.app.Fragment;
import org.jandroid.util.Logger;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 8/27/13 2:45 PM
 */
public abstract class BaseFragment extends Fragment {
    
    protected Logger logger = Logger.newLogger(this.getClass());

}
