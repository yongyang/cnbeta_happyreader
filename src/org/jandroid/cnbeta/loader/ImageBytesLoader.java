package org.jandroid.cnbeta.loader;

import android.util.Log;
import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.exception.NoCachedImageException;

import java.io.File;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:15 PM
 */
public class ImageBytesLoader extends AbstractLoader<byte[]> {

    private String imageUrl;

    public ImageBytesLoader(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public byte[] httpLoad(File baseDir) throws Exception {
        // some url has space char
        String url = imageUrl.replace(" ", "%20");
        byte[] bytes = CnBetaHttpClient.getInstance().httpGetBytes(url);
        writeDiskByteArray(baseDir, bytes);
        return bytes;
    }

    @Override
    public byte[] fromDisk(File baseDir) throws Exception {
        return readDiskByteArray(baseDir);
    }

    public String getFileName() {
        try {
            return URLEncoder.encode(imageUrl, "utf-8");
        }
        catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "URLEncoder exception", e);
            return imageUrl;
        }
    }

    @Override
    protected byte[] noCache() throws Exception {
        throw new NoCachedImageException(getImageUrl());
    }
}
