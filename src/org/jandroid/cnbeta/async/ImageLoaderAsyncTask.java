package org.jandroid.cnbeta.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ImageBytesLoader;
import org.jandroid.cnbeta.loader.ImageLoader;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class ImageLoaderAsyncTask extends BaseAsyncTask<Bitmap> {

    protected abstract String getImageUrl();

    @Override
    protected boolean isLocalLoadFirst() {
        return true;
    }

    @Override
    public AbstractLoader<Bitmap> getLoader() {
        return new ImageLoader(getImageUrl());
    }
}