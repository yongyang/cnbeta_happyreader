package org.jandroid.cnbeta.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.jandroid.common.Logger;
import org.jandroid.common.UnicodeUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaHttpClient {

    private static Logger logger = Logger.getLogger(CnBetaHttpClient.class);

    public static final int MAX_TOTAL_CONNECTIONS = 20;
    public static final int MAX_CONNECTIONS_PER_ROUTE = 20;
    public static final int TIMEOUT_CONNECT = 6000;
    public static final int TIMEOUT_READ = 30000;

    
    private final static CnBetaHttpClient client = new CnBetaHttpClient();

    private HttpClient httpClient;
    
    public static String DEFAULT_ENCODING = "UTF-8";
    private static final Map<String, String> DEFAULT_HEADERS = new HashMap<String, String>();
    
    static {
        DEFAULT_HEADERS.put("Accept-Encoding", "gzip, deflate");
        DEFAULT_HEADERS.put("Accept", "*/*");
        DEFAULT_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36");
        DEFAULT_HEADERS.put("Connection", "keep-alive");
        // Must add Referer, so site return data, default Referer
        DEFAULT_HEADERS.put("Referer", "http://www.cnbeta.com/");
    }
    
    private CnBetaHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(MAX_CONNECTIONS_PER_ROUTE));
        ConnManagerParams.setTimeout(httpParams, TIMEOUT_READ);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_CONNECT);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_READ);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpProtocolParams.setContentCharset(httpParams, DEFAULT_ENCODING);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, httpParams);
        // 设置 cookie 策略
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        // 是否需要设置cookie store
        // Create a local instance of cookie store
/*
        CookieStore cookieStore = new MyCookieStore();
        // Populate cookies if needed
        BasicClientCookie cookie = new BasicClientCookie("name", "value");
        cookie.setVersion(0);
        cookie.setDomain(".mycompany.com");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        // Set the store 
        httpclient.setCookieStore(cookieStore);
*/
        
    }

    public static CnBetaHttpClient getInstance() {
        return client;
    }

    public void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }


    public String httpGet(String url) throws Exception {
        return httpGet(url, DEFAULT_ENCODING);
    }


    public String httpGet(String url, Map<String, String> headers) throws Exception {
        return httpGet(url, DEFAULT_ENCODING, headers);
    }

    @SuppressWarnings("unchecked")
    public String httpGet(String url, String encoding) throws Exception {
        return httpGet(url, DEFAULT_ENCODING, Collections.EMPTY_MAP);
    }

    public String httpGet(String url, String encoding, Map<String, String> headers) throws Exception {
        long start = System.currentTimeMillis();
        logger.d("GET: " + url);
        HttpGet httpGet = newHttpGet(url, encoding, headers);
        HttpResponse response = httpClient.execute(httpGet);

        String result = handleRequest(httpGet, response);
        logger.d("GET: " + url + ", consume " + ((System.currentTimeMillis() - start)) + "ms");
        return result;
    }


    @SuppressWarnings("unchecked")
    public String httpPost(String url, Map<String, String> datas) throws Exception {
        return httpPost(url, DEFAULT_ENCODING, Collections.EMPTY_MAP, datas);
    }
    
    public String httpPost(String url, Map<String, String> headers, Map<String, String> datas) throws Exception {
        return httpPost(url, DEFAULT_ENCODING, headers, datas);
    }

        
    public String httpPost(String url, String encoding, Map<String, String> headers, Map<String, String> datas) throws Exception {
        long start = System.currentTimeMillis();
        logger.d("POST: " + url);
        HttpPost httpPost = newHttpPost(url, encoding, headers, datas);        
        HttpResponse response = httpClient.execute(httpPost);
        String result =  handleRequest(httpPost, response);
        logger.d("POST: " + url + ", consume " + ((System.currentTimeMillis() - start)) + "ms");
        return result;
    }
    
    private String handleRequest(HttpRequestBase request, HttpResponse response) throws Exception {
        String encoding = (String)request.getParams().getParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET);
        if(encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
        
        String result;
        try {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception("Server Error: " + response.getStatusLine().toString());
            }
            HttpEntity httpEntity = response.getEntity();
            try {
                Header contentEncodingHeader = response.getFirstHeader("Content-Encoding");
                if (contentEncodingHeader != null && contentEncodingHeader.getValue().contains("zip")) {
                    GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(EntityUtils.toByteArray(httpEntity)));
                    result = new String(IOUtils.toByteArray(gzipInputStream), encoding);
                }
                else {
                    result = EntityUtils.toString(httpEntity, encoding);
                }
                // convert unicode chars to chinese
                return UnicodeUtils.unicode2Chinese(result);
            }
            finally {
                httpEntity.consumeContent();
            }
        }
        catch (IOException e) {
            request.abort();
            throw e;
        }
    }

    private static void setDefaultHeaders(HttpMessage httpMessage){
        for(Map.Entry<String, String> entry : DEFAULT_HEADERS.entrySet()){
            httpMessage.setHeader(entry.getKey(), entry.getValue());
        }
    }

    public static HttpGet newHttpGet(String url, String encoding, Map<String, String> headers) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, encoding);
        
        setDefaultHeaders(httpGet);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }

        return httpGet;
    }

    
    public static HttpPost newHttpPost(String url, String encoding, Map<String, String> headers, Map<String, String> datas) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        httpPost.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, encoding);
        
        setDefaultHeaders(httpPost);
        
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.setHeader(entry.getKey(), entry.getValue());
        }

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
       
        for(Map.Entry<String, String> entry : datas.entrySet()){
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        return httpPost;
    }


    @SuppressWarnings("unchecked")
    public byte[] httpGetBytes(String url) throws Exception {
        return httpGetBytes(url, Collections.EMPTY_MAP);
    }

    public byte[] httpGetBytes(String url, Map<String, String> headers) throws Exception {
        long start = System.currentTimeMillis();
        logger.d("GET: " + url);
        final HttpGet httpGet = newHttpGet(url, DEFAULT_ENCODING, headers);
        HttpResponse response = httpClient.execute(httpGet);
        byte[] result = handleBytesRequest(httpGet, response);
        logger.d("GET: " + url + ", consume " + ((System.currentTimeMillis() - start)) + "ms");
        return result;
    }

    private byte[] handleBytesRequest(HttpRequestBase request, HttpResponse response) throws Exception {
        try {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception("Server Error: " + response.getStatusLine().toString());
            }
            final HttpEntity entity = response.getEntity();
            try {
                // Bug on slow connections, fixed in future release.
                return EntityUtils.toByteArray(entity);
            }
            finally {
                entity.consumeContent();
            }
        }
        catch (Exception e) {
            request.abort();
            throw e;
        }
    }

    public String getCookie(String key) {
        for(Cookie cookie : ((DefaultHttpClient)httpClient).getCookieStore().getCookies() ) {
            if(cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}