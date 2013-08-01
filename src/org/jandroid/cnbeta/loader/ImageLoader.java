package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import android.util.Log;
import org.jandroid.cnbeta.client.CnBetaHttpClient;

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
    public Bitmap fromDisk() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void toDisk(Bitmap bitmap) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
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
