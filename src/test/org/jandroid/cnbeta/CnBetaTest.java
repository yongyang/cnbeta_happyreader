package test.org.jandroid.cnbeta;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.jandroid.cnbeta.client.CnBetaHttpClient;
import org.jandroid.cnbeta.entity.Article;
import org.jandroid.cnbeta.loader.ArticleListLoader;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CnBetaTest extends TestCase {

    public void testHttpGet() throws Exception {
        long millis = System.currentTimeMillis();
        String url = "http://www.cnbeta.com/more.htm?jsoncallback=jQuery18008753548712314047_" + millis + "&type=all&page=1&_=" + (millis + 1);
        String response = CnBetaHttpClient.getInstance().httpGet(url);
//        Assert.fail(response);
        Assert.assertTrue(response.length() > 100);
    }

    public void testArticleListLoader() throws Exception {
        ArticleListLoader loader = new ArticleListLoader(ArticleListLoader.Type.ALL, 1);
        List<Article> articleList = loader.fromHttp();
        String s = Arrays.toString(articleList.toArray(new Article[articleList.size()]));
//        Assert.fail(s);
        Assert.assertTrue(!articleList.isEmpty());
    }
}
