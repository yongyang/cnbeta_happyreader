package org.jandroid.cnbeta.async;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.jandroid.cnbeta.R;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ImageBytesLoader;

import java.io.ByteArrayOutputStream;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class ImageBytesAsyncTask extends AbstractLoaderAsyncTask<byte[]> {

    protected abstract String getImageUrl();

    @Override
    protected boolean isLocalLoadFirst() {
        return true;
    }

    @Override
    public AbstractLoader<byte[]> getLoader() {
        return new ImageBytesLoader(getImageUrl());
    }

}