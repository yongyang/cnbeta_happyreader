package org.jandroid.cnbeta.loader;

import java.io.File;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractLoader<T> {

    /**
     * 
     * @param baseDir the dir for store cache file
     * @return
     * @throws Exception
     */
    public abstract T fromHttp(File baseDir) throws Exception;

    /**
     * 
     * @param baseDir the dir for store cache file
     * @return
     * @throws Exception
     */
    public abstract T fromDisk(File baseDir) throws Exception;

    /**
     * 
     * @param baseDir
     * @param obj the obj wants te be stored
     * @throws Exception
     */
    protected abstract <Obj> void  toDisk(File baseDir, Obj obj) throws Exception;
    
    public abstract File getCacheFile(File baseDir);
    
    public final boolean isCached(File baseDir) {
        return getCacheFile(baseDir).exists();
    }
    
}
