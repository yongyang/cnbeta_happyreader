package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import android.util.Log;
import org.jandroid.cnbeta.client.CnBetaHttpClient;

import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:15 PM
 */
public class ImageLoader extends LoaderTask<Bitmap>{

    private String url;

    public ImageLoader(String url) {
        this.url = url;
    }

    @Override
    public Bitmap fromHttp() throws Exception {
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
            return URLEncoder.encode(url,"utf-8");
        }
        catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "URLEncoder exception", e);
            return url;
        }
    }
}
