package org.jandroid.cnbeta.entity;

import java.io.Serializable;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class BaseArticle implements Serializable {

    private long sid;
    private String title;

    public final long getSid() {
        return sid;
    }

    public final void setSid(long sid) {
        this.sid = sid;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + "{" +
                "sid=" + sid +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseArticle that = (BaseArticle) o;

        if (sid != that.sid) return false;

        return true;
    }

    @Override
    public final int hashCode() {
        return (int) (sid ^ (sid >>> 32));
    }

}
