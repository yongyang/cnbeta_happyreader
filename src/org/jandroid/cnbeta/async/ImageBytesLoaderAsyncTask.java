package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ImageBytesLoader;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:21 PM
 */
public abstract class ImageBytesLoaderAsyncTask extends BaseAsyncTask<byte[]> {

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