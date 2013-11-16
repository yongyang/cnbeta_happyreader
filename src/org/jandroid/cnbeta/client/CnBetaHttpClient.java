package org.jandroid.cnbeta.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
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
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.jandroid.common.Logger;
import org.jandroid.common.UnicodeUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaHttpClient {

    private static Logger logger = Logger.getLogger(CnBetaHttpClient.class);

    public static final int MAX_TOTAL_CONNECTIONS = 30;
    public static final int MAX_CONNECTIONS_PER_ROUTE = 30;
    public static final int TIMEOUT_CONNECT = 10000;
    public static final int SO_TIMEOUT_READ = 30000;

    private final static CnBetaHttpClient INSTANCE = new CnBetaHttpClient();

    public static String DEFAULT_ENCODING = HTTP.UTF_8;
    private static final Map<String, String> DEFAULT_HEADERS = new HashMap<String, String>();

    private SoftReference<HttpClient> httpClientSoftReference = new SoftReference<HttpClient>(initHttpClient());

    static {
        DEFAULT_HEADERS.put("Accept-Encoding", "gzip, deflate");
        DEFAULT_HEADERS.put("Accept", "*/*");
        DEFAULT_HEADERS.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.72 Safari/537.36");
        DEFAULT_HEADERS.put("Connection", "keep-alive");
        // Must add Referer, so site return data, default Referer
        DEFAULT_HEADERS.put("Referer", "http://www.cnbeta.com/");
        DEFAULT_HEADERS.put("Content-Type", "application/x-www-form-urlencoded; charset=" + DEFAULT_ENCODING);
    }
    
    private CnBetaHttpClient() {

    }

    public static CnBetaHttpClient getInstance() {
        return INSTANCE;
    }

    private static HttpClient initHttpClient () {
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams, MAX_TOTAL_CONNECTIONS);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(MAX_CONNECTIONS_PER_ROUTE));
        ConnManagerParams.setTimeout(httpParams, SO_TIMEOUT_READ);
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_CONNECT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT_READ);
        HttpConnectionParams.setTcpNoDelay(httpParams, true);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, DEFAULT_ENCODING);
        HttpProtocolParams.setHttpElementCharset(httpParams, DEFAULT_ENCODING);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
        HttpClient httpClient = new DefaultHttpClient(cm, httpParams);
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
        return httpClient;
    }

    public synchronized HttpClient getHttpClient() {
        HttpClient httpClient = httpClientSoftReference.get();
        if(httpClient == null) {
            httpClient = initHttpClient();
            httpClientSoftReference = new SoftReference<HttpClient>(httpClient);
        }
        return  httpClient;
    }

    public synchronized void shutdown() {
        HttpClient httpClient = httpClientSoftReference.get();
        if(httpClient != null) {
            httpClient.getConnectionManager().shutdown();
        }
        httpClientSoftReference.clear();
    }

    public String httpGet(String url, RequestContext requestContext) throws Exception {
        return httpGet(url, DEFAULT_ENCODING, requestContext);
    }


    public String httpGet(String url, Map<String, String> headers, RequestContext requestContext) throws Exception {
        return httpGet(url, DEFAULT_ENCODING, headers, requestContext);
    }

    @SuppressWarnings("unchecked")
    public String httpGet(String url, String encoding, RequestContext requestContext) throws Exception {
        return httpGet(url, encoding, Collections.EMPTY_MAP, requestContext);
    }

    public String httpGet(String url, String encoding, Map<String, String> headers, RequestContext requestContext) throws Exception {
        long start = System.currentTimeMillis();
        logger.d("GET: " + url);
        HttpGet httpGet = newHttpGet(url, encoding, headers);
        HttpResponse response = getHttpClient().execute(httpGet);

        String result = handleRequest(httpGet, response, requestContext);
        logger.d("GET: " + url + ", consume " + ((System.currentTimeMillis() - start)) + "ms");
        return result;
    }


    @SuppressWarnings("unchecked")
    public String httpPost(String url, Map<String, String> datas, RequestContext requestContext) throws Exception {
        return httpPost(url, DEFAULT_ENCODING, Collections.EMPTY_MAP, datas, requestContext);
    }
    
    public String httpPost(String url, Map<String, String> headers, Map<String, String> datas, RequestContext requestContext) throws Exception {
        return httpPost(url, DEFAULT_ENCODING, headers, datas, requestContext);
    }

        
    public String httpPost(String url, String encoding, Map<String, String> headers, Map<String, String> datas, RequestContext requestContext) throws Exception {
        long start = System.currentTimeMillis();
        logger.d("POST: " + url);
        HttpPost httpPost = newHttpPost(url, encoding, headers, datas);

        HttpResponse response = getHttpClient().execute(httpPost);
        String result =  handleRequest(httpPost, response, requestContext);
        logger.d("POST: " + url + ", consume " + ((System.currentTimeMillis() - start)) + "ms");
        return result;
    }
    
    private String handleRequest(HttpRequestBase request, HttpResponse response, RequestContext requestContext) throws Exception {
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
                InputStream in = httpEntity.getContent();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int readSize  = 0;
                while((readSize = IOUtils.read(in, buffer)) > 0 ) {
                    //abort request when async task cancelled
                    if(requestContext.needAbort()) {
                        request.abort();
                        logger.d("Request aborted, " + request.getURI());
                        return null;
                    }
                    byteArrayOutputStream.write(buffer, 0, readSize);
                }

                Header contentEncodingHeader = response.getFirstHeader("Content-Encoding");
                if (contentEncodingHeader != null && contentEncodingHeader.getValue().contains("zip")) {
                    GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                    result = new String(IOUtils.toByteArray(gzipInputStream), encoding);
                }
                else {
                    result = new String(byteArrayOutputStream.toByteArray(), encoding);
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

        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry : datas.entrySet()){
            if(sb.length() != 0) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        httpPost.setEntity(new StringEntity(sb.toString(), encoding));
        return httpPost;
    }


    @SuppressWarnings("unchecked")
    public byte[] httpGetBytes(String url, RequestContext requestContext) throws Exception {
        return httpGetBytes(url, Collections.EMPTY_MAP, requestContext);
    }

    public byte[] httpGetBytes(String url, Map<String, String> headers, RequestContext requestContext) throws Exception {
        long start = System.currentTimeMillis();
        logger.d("GET: " + url);
        final HttpGet httpGet = newHttpGet(url, DEFAULT_ENCODING, headers);
        HttpResponse response = getHttpClient().execute(httpGet);
        byte[] result = handleBytesRequest(httpGet, response, requestContext);
        logger.d("GET: " + url + ", consume " + ((System.currentTimeMillis() - start)) + "ms");
        return result;
    }

    private byte[] handleBytesRequest(HttpRequestBase request, HttpResponse response, RequestContext requestContext) throws Exception {
        try {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new Exception("Server Error: " + response.getStatusLine().toString());
            }
            final HttpEntity httpEntity = response.getEntity();
            try {
                // Bug on slow connections, fixed in future release.
                InputStream in = httpEntity.getContent();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int readSize  = 0;
                while((readSize = IOUtils.read(in, buffer)) > 0 ) {
                    //abort request when async task cancelled
                    if(requestContext.needAbort()) {
                        request.abort();
                        logger.d("Request aborted, " + request.getURI());
                        return null;
                    }
                    byteArrayOutputStream.write(buffer, 0, readSize);
                }
                return byteArrayOutputStream.toByteArray();
            }
            finally {
                httpEntity.consumeContent();
            }
        }
        catch (Exception e) {
            request.abort();
            throw e;
        }
    }

    public String getCookie(String key) {
        for(Cookie cookie : ((DefaultHttpClient)getHttpClient()).getCookieStore().getCookies() ) {
            if(cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}