package test.org.jandroid.cnbeta;

import android.util.Base64;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.client.RequestContext;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaTest extends TestCase {

    public void testHttpGet() throws Exception {
        long millis = System.currentTimeMillis();
        String url = "http://www.cnbeta.com/more.htm?jsoncallback=jQuery18008753548712314047_" + millis + "&type=all&page=1&_=" + (millis + 1);
        String response = CnBetaHttpClient.getInstance().httpGet(url, new RequestContext() {
            public boolean needAbort() {
                return false;
            }
        });
//        Assert.fail(response);
        Assert.assertTrue(response.length() > 100);
    }

    public void testArticleListLoader() throws Exception {
//        ArticleListLoader loader = new ArticleListLoader(ArticleListLoader.Type.ALL, 1);
//        List<Article> articleList = loader.fromHttp();
//        String s = Arrays.toString(articleList.toArray(new Article[articleList.size()]));
//        Assert.fail(s);
//        Assert.assertTrue(!articleList.isEmpty());
    }

    public void testHttpGetImage() throws Exception {
        byte[] image = CnBetaHttpClient.getInstance().httpGetBytes("http://static.cnbetacdn.com/newsimg/2013/0729/01375107904.jpg_180x132.jpg", new RequestContext() {
            public boolean needAbort() {
                return false;
            }
        });
//        Assert.fail(image.toString());
        Assert.assertTrue(image!=null && image.length > 0);
    }

    public void testCookie() throws Exception {

        HttpClient httpclient = new DefaultHttpClient();

        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);


        // Create a local instance of cookie store
        CookieStore cookieStore = new BasicCookieStore();

        // Create local HTTP context
        HttpContext localContext = new BasicHttpContext();
        // Bind custom cookie store to the local context
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        HttpGet httpget = new HttpGet("http://www.google.com/");
//        httpget.addHeader("Host", "www.google.com");
        // Must add Referer, so site return data
        httpget.addHeader("Referer", "http://www.cnbeta.com/");
        httpget.addHeader("Accept-Encoding", "gzip, deflate");
        httpget.addHeader("Accept", "*/*");
//        httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

//        httpget.addHeader("User-Agent", "Mozilla/5.0 AppleWebKit/533.1 (KHTML, like Gecko)");
//        httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36");

        httpget.addHeader("Connection", "keep-alive");

        System.out.println("executing request " + httpget.getURI());

        // Pass local context as a parameter
        HttpResponse response = httpclient.execute(httpget, localContext);
        HttpEntity entity = response.getEntity();

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        List<Cookie> cookies = cookieStore.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            System.out.println("Local cookie: " + cookies.get(i));
        }

        Assert.fail(Arrays.toString(cookies.toArray()));


        // Consume response content
        if (entity != null) {
            entity.consumeContent();
        }

        System.out.println("----------------------------------------");

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();
    }

    public void testComment() throws Exception {
        String URL_TEMPLATE = "http://www.cnbeta.com/cmt?jsoncallback=okcb{0}&op={1}";
        Map<String, String> headers = new HashMap<String, String>();
        //should add this "X-Requested-With" header, so remote return result
        //httpget.addHeader("X-Requested-With", "XMLHttpRequest");
        headers.put("X-Requested-With", "XMLHttpRequest");
        //this header is optional, better to add
        //httpget.addHeader("Referer", "http://www.cnbeta.com/articles/250243.htm");
        headers.put("Referer", "http://www.cnbeta.com/articles/" + 250243 + ".htm");

        String url = MessageFormat.format(URL_TEMPLATE, "" + System.currentTimeMillis(), generateOP());
        String response = CnBetaHttpClient.getInstance().httpGet(url, headers, new RequestContext() {
            public boolean needAbort() {
                return false;
            }
        });

        //if failed
        if(response.indexOf("error") > 0){
            throw new Exception("error to read comments of article ");
        }
        Assert.fail(response);
    }

    private static String generateOP() throws Exception {
        String b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        String encoded = Base64.encodeToString("1,250243,63701".getBytes("UTF-8"), Base64.NO_WRAP);
//        encoded = encoded.substring(0, encoded.length()-2);
        for(int i=0; i<8; i++){
            encoded += b64.charAt((int)(Math.random() * b64.length()));
        }

        // 两次 url encode
        encoded = URLEncoder.encode(URLEncoder.encode(encoded, "UTF-8"), "UTF-8");
        return encoded;
    }

}
