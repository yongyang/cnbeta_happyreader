package org.jandroid.cnbeta.exception;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class InfoException extends Exception {

    public InfoException(String detailMessage) {
        super(detailMessage);
    }

    public InfoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
