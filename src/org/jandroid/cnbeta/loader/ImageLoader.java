package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.client.RequestContext;
import org.jandroid.cnbeta.exception.NoCachedImageException;

import java.io.File;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:15 PM
 */
public class ImageLoader extends AbstractLoader<Bitmap> {

    private String imageUrl;

    public ImageLoader(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Bitmap httpLoad(File baseDir, RequestContext requestContext) throws Exception {
        // some url has space char
        String url = imageUrl.replace(" ", "%20");
        byte[] bytes = CnBetaHttpClient.getInstance().httpGetBytes(url, requestContext);
        writeDiskByteArray(baseDir, bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public Bitmap fromDisk(File baseDir) throws Exception {
        byte[] bytes =  readDiskByteArray(baseDir);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
    protected Bitmap noCache() throws Exception {
        throw new NoCachedImageException(getImageUrl());
    }
}
