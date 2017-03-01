package org.jandroid.cnbeta.service;

import org.jandroid.common.autoupdate.AbstractCheckVersionService;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class CheckVersionService extends AbstractCheckVersionService {

    public static final String URL_VERSION_CHECKING = "http://www.jandroid.org/cnBeta/versions.xml";

    @Override
    protected String getURL() {
        return URL_VERSION_CHECKING;
    }
}
