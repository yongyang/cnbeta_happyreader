package org.jandroid.cnbeta.loader;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractLoader<T> {

    protected byte[] loadedData;
    protected T loadedObject;

    public byte[] getLoadedData() {
        return loadedData;
    }

    public void setLoadedData(byte[] loadedData) {
        this.loadedData = loadedData;
    }

    public T getLoadedObject() {
        return loadedObject;
    }

    public void setLoadedObject(T loadedObject) {
        this.loadedObject = loadedObject;
    }

    public abstract T fromHttp() throws Exception;

    public abstract T fromDisk(File baseDir) throws Exception;

    
    public abstract void toDisk(File baseDir, T t) throws Exception;
}
