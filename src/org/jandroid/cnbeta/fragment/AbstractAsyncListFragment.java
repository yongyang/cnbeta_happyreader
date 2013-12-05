package org.jandroid.cnbeta.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.exception.InfoException;
import org.jandroid.common.BaseFragment;
import org.jandroid.common.ThemeFragment;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */

public abstract class AbstractAsyncListFragment<T> extends AbstractListFragment<T> implements HasAsync<List<T>> {

    public AbstractAsyncListFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init reload data
        reloadData();
    }

    public CnBetaApplicationContext getCnBetaApplicationContext() {
        return super.getCnBetaApplicationContext();
    }


    public void onProgressShow() {
        // should call setProgressBarIndeterminate(true) each time before setProgressBarVisibility(true)
        if (!isDetached()) {
            theActivity.setProgressBarIndeterminate(true);
            theActivity.setProgressBarVisibility(true);
            theActivity.setProgressBarIndeterminateVisibility(true);
        }
    }

    public void onProgressDismiss() {
        if (!isDetached()) {
            theActivity.setProgressBarIndeterminateVisibility(false);
            theActivity.setProgressBarVisibility(false);
            //Stop refresh animation anyway
        }
    }

    public synchronized void onFailure(AsyncResult<List<T>> listAsyncResult) {
//        getAdapter().notifyDataSetChanged();
        Exception e = listAsyncResult.getException();
        if (e != null && e instanceof InfoException) {
            ToastUtils.showShortToast(getActivity(), listAsyncResult.getErrorMsg());
        }
    }

    public synchronized void onSuccess(AsyncResult<List<T>> listAsyncResult) {
        setDatas(listAsyncResult.getResult());
    }

    public abstract void loadData();

    public abstract void reloadData();
}
