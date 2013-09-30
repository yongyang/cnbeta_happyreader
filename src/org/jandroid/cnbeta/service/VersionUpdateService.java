package org.jandroid.cnbeta.service;

import org.jandroid.common.autoupdate.AbstractVersionUpdateService;
import org.jandroid.common.autoupdate.VersionInfo;

import java.text.MessageFormat;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class VersionUpdateService extends AbstractVersionUpdateService {
    public static final String URL_DOWNLOAD_TEMPLATE = "http://www.jandroid.org/cnBeta/cnBeta-{0}.apk";

    @Override
    protected String getURL(VersionInfo newVersionInfo) {
        return MessageFormat.format(URL_DOWNLOAD_TEMPLATE, newVersionInfo.getVersion());
    }
}
