package org.jandroid.cnbeta.loader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class LoaderTask<T> {

    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public abstract T fromHttp();

    public abstract T fromDisk();

    public abstract void toDisk(T t);
}
