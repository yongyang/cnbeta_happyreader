package org.jandroid.cnbeta.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:yyang@redhat.com">Yong Yang</a>
 * @create 7/29/13 3:15 PM
 */
public class CaptchaLoader extends AbstractLoader<Bitmap> {
    
    //responseJSON
    //{"hash1":428,"hash2":428,"url":"\/captcha.htm?v=521f018a90a1f"}

    public static final String URL_TEMPLATE = "http://www.cnbeta.com/captcha.htm?refresh=1&_={0}";
    
    private long sid;

    public CaptchaLoader(long sid) {
        this.sid = sid;
    }

    public long getSid() {
        return sid;
    }

    @Override
    public Bitmap httpLoad(File baseDir) throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        //should add this "X-Requested-With" header, so remote return result
        //httpget.addHeader("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Requested-With", "XMLHttpRequest");
        //this header is optional, better to add
        //httpget.addHeader("Referer", "http://www.cnbeta.com/articles/250243.htm");
        headers.put("Referer", "http://www.cnbeta.com/articles/" + getSid() + ".htm");

        String url = MessageFormat.format(URL_TEMPLATE, "" + System.currentTimeMillis());
        String responseJSONString = CnBetaHttpClient.getInstance().httpGet(url, headers);

        JSONObject responseJSONObject = (JSONObject)JSONValue.parse(responseJSONString);
        
        String captchaImageURL = (String)responseJSONObject.get("url");
        captchaImageURL = "http://www.cnbeta.com" + captchaImageURL;
        
        Map<String, String> headers2 = new HashMap<String, String>();
        //this header is optional, better to add
        //httpget.addHeader("Referer", "http://www.cnbeta.com/articles/250243.htm");
        headers2.put("Referer", "http://www.cnbeta.com/articles/" + getSid() + ".htm");
        byte[] imageBytes = CnBetaHttpClient.getInstance().httpGetBytes(captchaImageURL, headers2);       
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    @Override
    public Bitmap fromDisk(File baseDir) throws Exception {
        throw new UnsupportedOperationException("fromDisk");
    }

    public String getFileName() {
        throw new UnsupportedOperationException("getFileName");
    }
}
