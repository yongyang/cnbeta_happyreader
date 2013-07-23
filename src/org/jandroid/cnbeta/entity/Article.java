package org.jandroid.cnbeta.entity;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Article {

    public int sid;
    public String titleShow;

    public boolean bFavored;
    public boolean bReaded;
    public int nCommentsTimes;
    public int nEventScore;
    public int nFavoredTime;
    public int nQualityScore;
    public int nReadTimes;
    public int nReadedTime;
    public int nRecommendTimes;
    public int nScoreTime;
    public String strBriefContent;
    public String strCatalogID;
    public String strCatalogImage;
    public String strCatalogName;
    public String strPublishDate;
    public String strTitle;

    @Override
    public String toString() {
        return "Article{" +
                "sid=" + sid +
                ", titleShow='" + titleShow + '\'' +
                '}';
    }
}
