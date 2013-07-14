package org.jandroid.cnbeta.task;

import android.os.AsyncTask;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class PageAsyncTask< P extends EntityLoader, I, R> extends AsyncTask<P, I, R> {

    @Override
    protected R doInBackground(P... params) {
        return null;
    }

    public static void main(String[] args) {
    }
}
