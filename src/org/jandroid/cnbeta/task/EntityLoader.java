package org.jandroid.cnbeta.task;

import android.content.AsyncTaskLoader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class EntityLoader<T> {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T load() {
        T t;
        if(true) {
            t = loadFromWeb();
            writeToDisk(t);
        }
        else {
            loadFromWeb();
        }
        return t;
    }

    abstract T loadFromWeb();

    abstract T loadFromDisk();

    abstract void writeToDisk(T t);

}

/*
http://www.cnbeta.com/more.htm?jsoncallback=jQuery18007005875239260606_1373612093876&type=realtime&sid=244461&_=1373612267852

http://www.cnbeta.com/more.htm?jsoncallback=jQuery180028785929968214596_1373611841741&type=all&page=1&_=1373611882757
*/