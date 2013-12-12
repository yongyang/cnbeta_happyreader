package org.jandroid.cnbeta.async;

import org.jandroid.cnbeta.CnBetaApplicationContext;
import org.jandroid.cnbeta.loader.AbstractLoader;
import org.jandroid.cnbeta.loader.ImageBytesLoader;

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
    protected boolean isLocalLoadOnly() {
        CnBetaApplicationContext applicationContext = getAsyncContext().getCnBetaApplicationContext();
        //设置了移动网络不加载图片
        if(applicationContext.isMobileNetworkConnected() && !applicationContext.getCnBetaPreferences().isImageEnabledOnPhoneNetwork()) {
            return true;
        }
        return super.isLocalLoadOnly();
    }

    @Override
    public AbstractLoader<byte[]> getLoader() {
        return new ImageBytesLoader(getImageUrl());
    }

}