package org.jandroid.cnbeta.loader;

import org.apache.commons.io.FileUtils;
import org.jandroid.cnbeta.client.RequestContext;
import org.jandroid.cnbeta.exception.NoCachedDataException;
import org.jandroid.cnbeta.exception.NoDataInfoException;

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
    public abstract T httpLoad(File baseDir, RequestContext requestContext) throws Exception;

    /**
     * 
     * @param baseDir the dir for store cache file
     * @return
     * @throws Exception
     */
    public T diskLoad(File baseDir) throws Exception {
        if(!isCached(baseDir)) {
            return noCache();
        }
        return fromDisk(baseDir);
    }

    protected abstract T fromDisk(File baseDir) throws Exception;

    protected T noCache() throws Exception {
        throw new NoCachedDataException("未能从本地磁盘加载所请求的缓存数据");
    }
    /**
     *
     * @return filename to store data
     */
    public abstract String getFileName();

    protected void writeDisk(File baseDir, String str) throws Exception {
        FileUtils.writeStringToFile(getFile(baseDir), str);
    }

    protected void writeDiskByteArray(File baseDir, byte[] bytes) throws Exception {
        FileUtils.writeByteArrayToFile(getFile(baseDir), bytes);
    }

    protected String readDisk(File baseDir) throws Exception {
        return FileUtils.readFileToString(getFile(baseDir));
    }

    protected byte[] readDiskByteArray(File baseDir) throws Exception {
        return FileUtils.readFileToByteArray(getFile(baseDir));
    }

    public final File getFile(File baseDir) {
        return new File(baseDir, getFileName());
    }

    public boolean isCached(File baseDir) {
        return getFile(baseDir).exists();
    }
}
