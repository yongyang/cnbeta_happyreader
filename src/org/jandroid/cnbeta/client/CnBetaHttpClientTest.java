package org.jandroid.cnbeta.client;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaHttpClientTest extends TestCase {

    public void testHttpGet() throws Exception {
//        Log.v(this.getClass().getPackage().getName(), HttpUtils.httpGet("http://www.cnbeta.com/"));
//        assertNotSame("", HttpUtils.httpGet("http://www.cnbeta.com/"));
        long millis = System.currentTimeMillis();
        String url = "http://www.cnbeta.com/more.htm?jsoncallback=jQuery18008753548712314047_" + millis + "&type=all&page=1&_=" + (millis + 1);
        Assert.fail(UnicodeUtils.unicode2Chinese(CnBetaHttpClient.getInstance().httpGet(url)));
    }
}
