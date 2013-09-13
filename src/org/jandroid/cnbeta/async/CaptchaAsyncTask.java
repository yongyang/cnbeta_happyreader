package org.jandroid.cnbeta.async;

import android.graphics.Bitmap;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.CaptchaLoader;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class CaptchaAsyncTask extends AbstractLoaderAsyncTask<Bitmap> {

    protected abstract long getSid();

    @Override
    protected boolean isRemoteLoadOnly() {
        return true;
    }

    @Override
    public AbstractLoader<Bitmap> getLoader() {
        return new CaptchaLoader(getSid());
    }
}