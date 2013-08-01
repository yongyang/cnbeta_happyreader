package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import android.util.Log;
import org.jandroid.cnbeta.client.CnBetaHttpClient;

import java.io.File;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:15 PM
 */
public class ImageLoader extends AbstractLoader<Bitmap> {

    private String imageUrl;

    public ImageLoader(String imageUrl) {
        this.imageUrl = imageUrl.replace(" ", "%20");
    }

    @Override
    public Bitmap fromHttp() throws Exception {
        // some url has space char
        String url = imageUrl.replace(" ", "%20");
        return CnBetaHttpClient.getInstance().httpGetImage(url);
    }

    @Override
    public Bitmap fromDisk(File baseDir) throws Exception {
        return null;
    }

    @Override
    public void toDisk(File baseDir, Bitmap bitmap) throws Exception {

    }

    private String getFilename(){
        try {
            return URLEncoder.encode(imageUrl,"utf-8");
        }
        catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "URLEncoder exception", e);
            return imageUrl;
        }
    }
}
