package org.jandroid.cnbeta;

import org.jandroid.util.EnvironmentUtils;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public interface CnBetaApplicationContext {

    boolean isNetworkConnected();

    boolean isSdCardMounted();

}
