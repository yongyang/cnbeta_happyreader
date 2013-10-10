package org.jandroid.cnbeta.client;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface RequestContext {

    // indicate that the http request needs to abort
    public boolean needAbort();

}
