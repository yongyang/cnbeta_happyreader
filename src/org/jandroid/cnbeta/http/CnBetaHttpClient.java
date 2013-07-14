package org.jandroid.cnbeta.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaHttpClient {

    public static final int HTTP_OK = 200;
    public static final String DEFAULT_ENCODING = "utf-8";

    public static final int MAX_TOTAL_CONNECTIONS = 20;
    public static final int MAX_CONNECTIONS_PER_ROUTE = 20;
    public static final int TIMEOUT_CONNECT = 6000;
    public static final int TIMEOUT_READ = 30000;


    private final static CnBetaHttpClient client = new CnBetaHttpClient();

    private HttpClient httpClient;

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
    }

    public static CnBetaHttpClient getInstance() {
        return client;
    }

    public void shutdown() {
        httpClient.getConnectionManager().shutdown();
    }


    public String httpGet(String url) throws Exception {
        return httpGet(url, "utf-8");
    }

    public String httpGet(String url, String encoding) throws Exception {
        String result = "";
        HttpGet httpGet = newHttpGet(url, encoding);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != HTTP_OK) {
                throw new Exception("Server Error: " + response.getStatusLine().toString());
            }
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity, encoding);
            httpEntity.consumeContent();
            return result;
        }
        catch (IOException e) {
            httpGet.abort();
            throw e;
        }
    }

    public static HttpGet newHttpGet(String url, String encoding){
        HttpGet httpGet = new HttpGet(url);
//        httpGet.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, encoding);
        return httpGet;
    }

    //TODO:
    public byte[] getImage(String url){
        return null;
    }
}

/**
    public String Get(String paramString1, String paramString2, Boolean paramBoolean, String paramString3, CookieStore paramCookieStore)
      throws Exception
    {
      BasicHttpParams localBasicHttpParams = new BasicHttpParams();
      if (paramBoolean.booleanValue())
      {
        localBasicHttpParams.setParameter("http.route.default-proxy", new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort()));
        paramString2 = "utf8";
      }
      HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 50000);
      HttpConnectionParams.setSoTimeout(localBasicHttpParams, 300000);
      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(localBasicHttpParams);
      if (paramCookieStore != null)
        localDefaultHttpClient.setCookieStore(paramCookieStore);
      HttpGet localHttpGet = new HttpGet(paramString1);
      localHttpGet.addHeader("Accept-Encoding", "gzip,deflate");
      localHttpGet.addHeader("Accept", "* / *");
      localHttpGet.addHeader("User-Agent", "Mozilla/5.0 AppleWebKit/533.1 (KHTML, like Gecko)");
      if ((paramString3 != null) && (!paramString3.equals("")))
        localHttpGet.addHeader("Referer", paramString3);
      HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
      Object localObject = localHttpResponse.getEntity().getContent();
      Header localHeader = localHttpResponse.getFirstHeader("Content-Encoding");
      if ((localHeader != null) && (localHeader.getValue().equalsIgnoreCase("gzip")))
        localObject = new GZIPInputStream((InputStream)localObject);
      return Utility.convertStreamToString((InputStream)localObject, paramString2);
    }

    public Bitmap GetImage(String paramString, Boolean paramBoolean)
      throws Exception
    {
      return GetImage(paramString, paramBoolean, null, null);
    }

    public Bitmap GetImage(String paramString1, Boolean paramBoolean, String paramString2)
      throws Exception
    {
      return GetImage(paramString1, paramBoolean, paramString2, null);
    }

    public Bitmap GetImage(String paramString1, Boolean paramBoolean, String paramString2, CookieStore paramCookieStore)
      throws Exception
    {
      BasicHttpParams localBasicHttpParams = new BasicHttpParams();
      if (paramBoolean.booleanValue())
        localBasicHttpParams.setParameter("http.route.default-proxy", new HttpHost(Proxy.getDefaultHost(), Proxy.getDefaultPort()));
      HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 50000);
      HttpConnectionParams.setSoTimeout(localBasicHttpParams, 300000);
      DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(localBasicHttpParams);
      HttpGet localHttpGet = new HttpGet(paramString1);
      localHttpGet.addHeader("Accept-Encoding", "gzip,deflate");
      localHttpGet.addHeader("Accept", "* / *");
      localHttpGet.addHeader("User-Agent", "Mozilla/5.0 AppleWebKit/533.1 (KHTML, like Gecko)");
      if ((paramString2 != null) && (!paramString2.equals("")))
        localHttpGet.addHeader("Referer", paramString2);
      if (paramCookieStore != null)
        localDefaultHttpClient.setCookieStore(paramCookieStore);
      HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
      Object localObject = localHttpResponse.getEntity().getContent();
      Header localHeader = localHttpResponse.getFirstHeader("Content-Encoding");
      if ((localHeader != null) && (localHeader.getValue().equalsIgnoreCase("gzip")))
        localObject = new GZIPInputStream((InputStream)localObject);
      return BitmapFactory.decodeStream((InputStream)localObject);
    }

*/

/*

  class ImageCodeTask extends AsyncTask<String, Integer, Bitmap>
  {
    ImageCodeTask()
    {
    }

    protected Bitmap doInBackground(String[] paramArrayOfString)
    {
      Bitmap localBitmap = null;
      try
      {
        BasicHttpParams localBasicHttpParams = new BasicHttpParams();
        if (Comment.this.currNetworkState == NetworkState.Wap)
          localBasicHttpParams.setParameter("http.route.default-proxy", new HttpHost(android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort()));
        HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 50000);
        HttpConnectionParams.setSoTimeout(localBasicHttpParams, 300000);
        DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(localBasicHttpParams);
        HttpGet localHttpGet = new HttpGet("http://www.cnbeta.com/validate1.php?" + System.currentTimeMillis());
        if (!Comment.this.SessionID.equals(""))
        {
          BasicCookieStore localBasicCookieStore = new BasicCookieStore();
          BasicClientCookie localBasicClientCookie = new BasicClientCookie("PHPSESSID", Comment.this.SessionID);
          localBasicClientCookie.setDomain("www.cnbeta.com");
          localBasicClientCookie.setPath("/");
          localBasicCookieStore.addCookie(localBasicClientCookie);
          localDefaultHttpClient.setCookieStore(localBasicCookieStore);
        }
        localBitmap = BitmapFactory.decodeStream(localDefaultHttpClient.execute(localHttpGet).getEntity().getContent());
        List localList = localDefaultHttpClient.getCookieStore().getCookies();
        for (int i = 0; i < localList.size(); i++)
          if (((Cookie)localList.get(i)).getName().equals("PHPSESSID"))
            Comment.this.SessionID = ((Cookie)localList.get(i)).getValue();
      }
      catch (Exception localException)
      {
      }
      return localBitmap;
    }

    protected void onPostExecute(Bitmap paramBitmap)
    {
      if (paramBitmap != null)
      {
        ImageView localImageView = (ImageView)Comment.this.fabiaoDialog.findViewById(2131296263);
        ((ProgressBar)Comment.this.fabiaoDialog.findViewById(2131296264)).setVisibility(8);
        localImageView.setVisibility(0);
        localImageView.setImageBitmap(paramBitmap);
      }
    }
  }

  class PostTask extends AsyncTask<String, Integer, String>
  {
    PostTask()
    {
    }

    protected String doInBackground(String[] paramArrayOfString)
    {
      String str1 = paramArrayOfString[0];
      String str2 = paramArrayOfString[1] + Comment.this.configEntity.PostBy;
      String str3 = paramArrayOfString[2];
      String str4 = paramArrayOfString[3];
      try
      {
        String str6 = "tid=" + str4 + "&sid=" + Comment.this.id + "&valimg_main=" + str3 + "&nowname=" + str1 + "&comment=" + str2 + "&nowsubject=Re:" + Comment.this.title + "&nowpage=&nowemail=";
        URL localURL = new URL("http://www.cnbeta.com/Ajax.comment.php?ver=new&randnum=" + System.currentTimeMillis());
        HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
        if (Comment.this.currNetworkState == NetworkState.Wap)
          localHttpURLConnection = (HttpURLConnection)localURL.openConnection(new java.net.Proxy(Proxy.Type.HTTP, new InetSocketAddress(android.net.Proxy.getDefaultHost(), android.net.Proxy.getDefaultPort())));
        localHttpURLConnection.setConnectTimeout(50000);
        localHttpURLConnection.setReadTimeout(300000);
        localHttpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        localHttpURLConnection.addRequestProperty("Referer", "http://www.cnbeta.com/articles/" + Comment.this.id + ".htm");
        localHttpURLConnection.addRequestProperty("Cookie", "PHPSESSID=" + Comment.this.SessionID + "; cbGuestName=; cbGuestPage=; cbGuestEmail=");
        localHttpURLConnection.setRequestMethod("POST");
        localHttpURLConnection.setDoOutput(true);
        byte[] arrayOfByte = str6.getBytes("gb2312");
        localHttpURLConnection.getOutputStream().write(arrayOfByte, 0, arrayOfByte.length);
        localHttpURLConnection.getOutputStream().flush();
        localHttpURLConnection.getOutputStream().close();
        String str7 = Utility.convertStreamToString(localHttpURLConnection.getInputStream(), "utf-8");
        str5 = str7;
        return str5;
      }
      catch (Exception localException)
      {
        while (true)
          String str5 = "";
      }
    }

    protected void onPostExecute(String paramString)
    {
      Comment.this.fabiaoProgressDialog.dismiss();
      try
      {
        if (paramString.equals(""))
          return;
        switch (Integer.valueOf(Integer.parseInt(paramString.substring(0, 1))).intValue())
        {
        case 0:
          Toast.makeText(Comment.this, "您要评论的新闻不存在", 0).show();
          Comment.this.fabiaoDialog.dismiss();
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        }
      }
      catch (Exception localException)
      {
        Toast.makeText(Comment.this, "未知错误", 0).show();
      }
      Toast.makeText(Comment.this, "验证码不正确", 0).show();
      Comment.this.fabiaoDialog.findViewById(2131296264).setVisibility(0);
      Comment.this.fabiaoDialog.findViewById(2131296263).setVisibility(8);
      ((EditText)Comment.this.fabiaoDialog.findViewById(2131296262)).setText("");
      Comment.ImageCodeTask localImageCodeTask = new Comment.ImageCodeTask(Comment.this);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "";
      localImageCodeTask.execute(arrayOfString);
      return;
      Toast.makeText(Comment.this, "30秒内不允许再次评论", 0).show();
      Comment.this.fabiaoDialog.dismiss();
      return;
      Toast.makeText(Comment.this, "请填写评论后再提交", 0).show();
      return;
      Toast.makeText(Comment.this, "评论字数超过限制", 0).show();
      return;
      Toast.makeText(Comment.this, "发送评论成功，刷新后显示", 0).show();
      Comment.this.fabiaoDialog.dismiss();
      return;
      Toast.makeText(Comment.this, "CBFW检测到您输入了不适当字词，请纠正", 0).show();
      return;
      Toast.makeText(Comment.this, "您的评论需审核后才能显示", 0).show();
      Comment.this.fabiaoDialog.dismiss();
      return;
      Toast.makeText(Comment.this, "由于种种原因，这条新闻不开放评论，请见谅", 0).show();
      Comment.this.fabiaoDialog.dismiss();
      return;
      Toast.makeText(Comment.this, "评论系统维护中", 0).show();
      Comment.this.fabiaoDialog.dismiss();
    }

    protected void onPreExecute()
    {
      Comment.this.fabiaoProgressDialog = ProgressDialog.show(Comment.this, "", "评论发表中，请稍候...", true, true);
    }
  }

  class SupportTask extends AsyncTask<String, Integer, String>
  {
    SupportTask()
    {
    }

    protected String doInBackground(String[] paramArrayOfString)
    {
      boolean bool = true;
      String str1 = "http://www.cnbeta.com/Ajax.vote.php?tid=" + paramArrayOfString[bool].replace("h", "") + "&" + paramArrayOfString[0] + "=1";
      try
      {
        WebContent localWebContent = WebContent.getInstance();
        if (Comment.this.currNetworkState == NetworkState.Wap);
        while (true)
        {
          String str3 = localWebContent.Get(str1, "utf-8", Boolean.valueOf(bool));
          str2 = str3;
          return str2;
          bool = false;
        }
      }
      catch (Exception localException)
      {
        while (true)
          String str2 = "";
      }
    }

    protected void onPostExecute(String paramString)
    {
      Comment.this.fabiaoProgressDialog.dismiss();
      if (paramString.substring(0, 1).equals("0"))
        Toast.makeText(Comment.this, "谢谢您的参与", 0).show();
      while (true)
      {
        return;
        Toast.makeText(Comment.this, "你已经投过票了", 0).show();
      }
    }

    protected void onPreExecute()
    {
      Comment.this.fabiaoProgressDialog = ProgressDialog.show(Comment.this, "", "投票中，请稍候...", true, true);
    }
  }

  */