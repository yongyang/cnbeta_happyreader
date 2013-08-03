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

    public ImageLoader(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Bitmap fromHttp() throws Exception {
        // some url has space char
        String url = imageUrl.replace(" ", "%20");
        byte[] bytes = CnBetaHttpClient.getInstance().httpGetImage(url);
        setLoadedData(bytes);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        setLoadedObject(bitmap);
        return bitmap;
    }

    public boolean isImageCached(File baseDir) {
        File file = getFile(baseDir);
        return file.exists();
    }

    @Override
    public Bitmap fromDisk(File baseDir) throws Exception {
        File file = getFile(baseDir);
        if (file.exists()) {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else {
            return null;
        }
    }

    @Override
    public void toDisk(File baseDir, Bitmap bitmap) throws Exception {
        if (bitmap == getLoadedObject()) {
            FileUtils.writeByteArrayToFile(getFile(baseDir), getLoadedData());
        }
        else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            FileUtils.writeByteArrayToFile(getFile(baseDir), baos.toByteArray());
        }
    }

    private File getFile(File baseDir) {
        return new File(baseDir, getFilename());
    }

    private String getFilename() {
        try {
            return URLEncoder.encode(imageUrl, "utf-8");
        }
        catch (Exception e) {
            Log.w(this.getClass().getSimpleName(), "URLEncoder exception", e);
            return imageUrl;
        }
    }
}
