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
    public Bitmap fromHttp(File baseDir) throws Exception {
        // some url has space char
        String url = imageUrl.replace(" ", "%20");
        byte[] bytes = CnBetaHttpClient.getInstance().httpGetImage(url);
        setLoadedData(bytes);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        toDisk(baseDir, bytes);
        setLoadedObject(bitmap);
        return bitmap;
    }

    @Override
    public Bitmap fromDisk(File baseDir) throws Exception {
        File file = getCacheFile(baseDir);
        if (file.exists()) {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        else {
            return null;
        }
    }

    @Override
    protected  void toDisk(File baseDir, Object bitmap) throws Exception {
        if (bitmap == getLoadedObject()) {
            FileUtils.writeByteArrayToFile(getCacheFile(baseDir), getLoadedData());
        }
        else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //TODO: 是否需要根据后缀名设置不同的Format
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
            FileUtils.writeByteArrayToFile(getCacheFile(baseDir), baos.toByteArray());
        }
    }

    public File getCacheFile(File baseDir) {
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
