package org.jandroid.cnbeta.async;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ImageLoader;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class ImageAsyncTask extends AbstractLoaderAsyncTask<Bitmap> {

    protected abstract String getImageUrl();

    @Override
    protected boolean isLocalLoadFirst() {
        return true;
    }

    @Override
    public AbstractLoader<Bitmap> getLoader() {
        return new ImageLoader(getImageUrl());
    }

    @Override
    protected Bitmap defaultResult() throws Exception {
        return BitmapFactory.decodeResource(((Application) getAsyncContext().getCnBetaApplicationContext()).getResources(), R.drawable.default_img);
    }
}