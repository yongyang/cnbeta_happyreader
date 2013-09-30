package org.jandroid.common.autoupdate;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jandroid.common.BaseService;
import org.jandroid.common.StringUtils;
import org.jandroid.common.XMLUtils;
import org.jandroid.common.async.AsyncResult;
import org.jandroid.common.async.BaseAsyncTask;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.URL;

/**
 * <metadata>
 * <name>cnBeta</name>
 * <extension>apk</extension>
 * <latest>1.0.2</latest>
 * <update> </update>
 * <description> </description>
 * <version-history>
 * <history>
 * <version>1.0.0</version>
 * <update> </update>
 * <description> </description>
 * </history>
 * <history>
 * <version>1.0.1</version>
 * <update> </update>
 * <description> </description>
 * </history>
 * </version-history>
 * </metadata>
 *
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractCheckVersionService extends BaseService {

    private Binder binder = new CheckVersionBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * @return checkVersion url, ex: http://www.jandroid.org/cnBeta/versions.xml
     */
    protected abstract String getURL();

    protected VersionInfo getLatestVersionInfo() throws Exception {
        //获取XML
        URL url = new URL(getURL());
        final InputStream in = url.openStream();
        try {
            String xml = IOUtils.toString(in);
            Document doc = XMLUtils.loadDocument(xml);
            String name = XMLUtils.getChildElementValueByTagName(doc.getDocumentElement(), "name");
            String latestVersion = XMLUtils.getChildElementValueByTagName(doc.getDocumentElement(), "latest");
            String extension = XMLUtils.getChildElementValueByTagName(doc.getDocumentElement(), "extension");
            String update = XMLUtils.getChildElementValueByTagName(doc.getDocumentElement(), "update");
            String description = XMLUtils.getChildElementValueByTagName(doc.getDocumentElement(), "description");
            VersionInfo versionInfo = new VersionInfo();
            versionInfo.setName(name);
            versionInfo.setVersion(latestVersion);
            versionInfo.setDescription(description);
            versionInfo.setUpdate(update);
            versionInfo.setDownloadUrl(FilenameUtils.getPath(getURL()) + "/" + name + "." + extension);
            return versionInfo;
        }
        finally {
            try {
                in.close();
            }
            catch (Exception e) {
            }
        }
    }

    protected String getCurrentVersion() throws Exception {
        return getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
    }


    /**
     * 检查当前系统是否是最新版本
     *
     * @return 1：当前最新版本 0：发现新的版本可更新 -1：检测新版本失败
     */
    public void checkVersion(final CheckingCallback callback) {

        executeAsyncTask(
                new BaseAsyncTask<VersionInfo>() {
                    @Override
                    protected void onPreExecute() {
                        callback.onStartChecking(getURL());
                    }


                    @Override
                    protected VersionInfo run(Object... params) throws Exception {
                        return getLatestVersionInfo();
                    }

                    @Override
                    protected void onSuccess(AsyncResult<VersionInfo> versionInfoAsyncResult) {
                        VersionInfo latestVersionInfo = versionInfoAsyncResult.getResult();
                        String currentVersion;
                        try {
                            currentVersion = getCurrentVersion();
                            int versionComparation = StringUtils.compareStringVersions(latestVersionInfo.getVersion(), currentVersion);
                            if (versionComparation == 1) {
                                callback.onNewVersion(latestVersionInfo);
                            }
                            else if (versionComparation == 0) {
                                callback.onSameVersion(latestVersionInfo);
                            }
                            else {
                                callback.onOldVersion(latestVersionInfo);
                            }
                        }
                        catch (Exception e) {
                            callback.onFailure(e);
                        }
                    }

                    @Override
                    protected void onFailure(AsyncResult<VersionInfo> versionInfoAsyncResult) {
                        callback.onFailure(versionInfoAsyncResult.getException());
                    }

                    @Override
                    protected void onPostExecute(AsyncResult<VersionInfo> versionInfoAsyncResult) {
                        super.onPostExecute(versionInfoAsyncResult);
                        AbstractCheckVersionService.this.stopSelf();
                    }
                }
        );
    }


    public class CheckVersionBinder extends Binder {

        public void checkVersion(final CheckingCallback callback) {
            AbstractCheckVersionService.this.checkVersion(callback);
        }
    }

    public static interface CheckingCallback {

        void onStartChecking(String url);

        void onFailure(Exception e);

        void onNewVersion(VersionInfo versionInfo);

        void onSameVersion(VersionInfo versionInfo);

        void onOldVersion(VersionInfo versionInfo);
    }
}
