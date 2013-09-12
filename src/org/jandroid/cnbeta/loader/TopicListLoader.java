package org.jandroid.cnbeta.loader;

import org.jandroid.cnbeta.entity.Topic;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class TopicListLoader extends AbstractListLoader<Topic> {

//    public static final int MAX_PAGE = 9;

    public static final String URL_TEMPLATE = "http://www.cnbeta.com/topics.htm?page={0}&_={1}";

    public TopicListLoader(int page) {
        super(Type.ALL, page);
    }

    @Override
    protected String getURL() {
        return MessageFormat.format(URL_TEMPLATE,  "" + getPage(),  "" + System.currentTimeMillis());
    }

    @Override
    public List<Topic> fromHttp(File baseDir) throws Exception {
/*
        if(getPage() > MAX_PAGE){
            return Collections.EMPTY_LIST;
        }
*/
        return super.fromHttp(baseDir);
    }

    @Override
    public List<Topic> fromDisk(File baseDir) throws Exception {
/*
        if(getPage() > MAX_PAGE){
            return Collections.EMPTY_LIST;
        }
*/
        return super.fromDisk(baseDir);
    }

    @Override
    protected JSONArray getJSONArray(String response) {
        JSONArray jSONArray = new JSONArray();

        Document document = Jsoup.parse(response, "UTF-8");
        Elements elements = document.select("dl[title]");
        for(Element element : elements) {
            JSONObject jSONObject = new JSONObject();
            Element imgElement = element.getElementsByTag("img").first();
            jSONObject.put("logo", imgElement.attr("src"));

            Element aElement = element.getElementsByTag("a").last();
            jSONObject.put("name", aElement.text());

            String topicHref = aElement.attr("href");
            String topicId = topicHref.substring("/topics/".length(),topicHref.indexOf(".htm"));
            jSONObject.put("id", topicId);

            jSONArray.add(jSONObject);
        }

        return jSONArray;
    }

    @Override
    protected Topic newEntity(JSONObject jSONObject) {
        return  new Topic(jSONObject);
    }

    @Override
    public String getFileName() {
        return "topics_" + getPage();
    }
}
/*

                    <dl title="">
                        <dt>
                            <div class="imgbox90x90">
                                <a href="/topics/501.htm"><img src="http://static.cnbetacdn.com/topics/13-05-22 11-89.gif" /></a>
                            </div>
                        </dt>
                        <dd>
                            <a href="/topics/501.htm" >115网盘</a>
                        </dd>
                    </dl>
*/