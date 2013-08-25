package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.client.CnBetaHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:15 PM
 */
public class ImageLoader extends AbstractLoader<Bitmap> {

    private String imageUrl;

    private byte[] imageData;

    public ImageLoader(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Bitmap fromHttp(File baseDir) throws Exception {
        // some url has space char
        String url = imageUrl.replace(" ", "%20");
        byte[] bytes = CnBetaHttpClient.getInstance().httpGetImage(url);
        imageData = bytes;
        writeDiskByteArray(baseDir, bytes);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    @Override
    public Bitmap fromDisk(File baseDir) throws Exception {
        if(isCached(baseDir)){
            byte[] bytes = readDiskByteArray(baseDir);
            imageData = bytes;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    public byte[] getImageData() {
        return imageData;
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
}
