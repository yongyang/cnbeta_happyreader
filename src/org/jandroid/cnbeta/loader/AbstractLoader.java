package org.jandroid.cnbeta.loader;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractLoader<T> {

    private String taskName;

    private T loadedObject;

    public String getTaskName() {
        return taskName;
    }

    public T getLoadedObject() {
        return loadedObject;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public final void load(){
        //TODO:
    }

    public abstract T fromHttp() throws Exception;

    public abstract T fromDisk() throws Exception;

    public abstract void toDisk(T t) throws Exception;
}
