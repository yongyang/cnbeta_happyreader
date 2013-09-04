package org.jandroid.cnbeta.loader;

import org.apache.commons.io.FileUtils;

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
