package org.jandroid.common.autoupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.RemoteViews;
import android.widget.Toast;
import org.apache.commons.io.FilenameUtils;
import org.jandroid.cnbeta.MainActivity;
import org.jandroid.cnbeta.R;
import org.jandroid.common.BaseService;
import org.jandroid.common.IntentUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public abstract class AbstractVersionUpdateService extends BaseService {

    private boolean cancelled;
    private int progress;

    private NotificationManager notificationManager;

    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    protected abstract String getURL(VersionInfo newVersionInfo);

    protected void startDownload(VersionInfo newVersionInfo, final UpdatingCallback callback) {

        cancelled = false;
        final String url = getURL(newVersionInfo);
        String filename = FilenameUtils.getBaseName(url) + "." + FilenameUtils.getExtension(url);
        final Notification downloadingNotification = createDownloadingNotification(filename);

        executeAsyncTask(
                new AsyncTask<Object, Integer, File>() {

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        super.onProgressUpdate(values);
                        //update progress by notification
                        updateNotificationProgress(values[0], downloadingNotification);
                        if(!isCancelled()) {
                            callback.onProgressUpdate(values[0]);
                        }
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        notificationManager.notify(downloadingNotification.hashCode(), downloadingNotification);
                        if(!isCancelled()) {
                            callback.onStartDownloading();
                        }
                    }

                    @Override
                    protected void onPostExecute(File file) {
                        super.onPostExecute(file);
                        if (file != null) {
                            if(!isCancelled()) {
                                callback.onDownloaded(file);
                            }
                            afterDownload(file);
                        }
                        else {
                            cancelDownload(url, downloadingNotification);
                        }
                    }

                    @Override
                    protected File doInBackground(Object... params) {
                        String url = params[0].toString();

                        File downFile = null;

                        //初始化数据
                        long fileSize = 0;
                        int readSize = 0;
                        long downSize = 0;
                        progress = 0;

                        InputStream is = null;
                        FileOutputStream fos = null;

                        try {
                            URL myURL = new URL(url);                        //取得URL
                            URLConnection conn = myURL.openConnection();        //建立联机
                            conn.connect();
                            fileSize = conn.getContentLength();                    //获取文件长度
                            is = conn.getInputStream();                        //InputStream 下载文件

                            if (is == null) {
                                Log.d("tag", "error");
                                throw new RuntimeException("stream is null");
                            }

                            is = new BufferedInputStream(is);

                            //建立临时文件
                            downFile = File.createTempFile(Base64.encodeToString(url.getBytes(), Base64.NO_WRAP), "." + MimeTypeMap.getFileExtensionFromUrl(url));

                            //将文件写入临时盘
                            fos = new FileOutputStream(downFile);
                            byte buf[] = new byte[1024*1024];
                            while (!cancelled && (readSize = is.read(buf)) > 0) {
                                fos.write(buf, 0, readSize);
                                downSize += readSize;
                                int num = (int) ((double) downSize / (double) fileSize * 100);
                                publishProgress(num);
                            }
                            publishProgress(100); //100% END

                            if (cancelled) { // cancel
                                downFile.delete();
                                return null;
                            }
                            return downFile;
                        }
                        catch (final Exception e) {
                            if(!isCancelled()) {
                                handler.post(new Runnable() {
                                    public void run() {
                                        callback.onFailure(e);
                                    }
                                });
                            }
                            if(downFile != null) {
                                // delete temp file
                                downFile.delete();
                            }
                            return null;
                        }
                        finally {
                            try {
                                if (null != fos) fos.close();
                                if (null != is) is.close();
                            }
                            catch (IOException e) { }
                        }
                    }
                }, getURL(newVersionInfo));
    }

    protected Notification createDownloadingNotification(String filename) {
        CharSequence tickerText = "开始下载";
        long when = System.currentTimeMillis();
        // call build after api 16
        Notification mNotification = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher).setWhen(when).setTicker(tickerText).getNotification();
        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.download_notification);
        contentView.setTextViewText(R.id.fileName, "正在下载：" + filename);

        // 指定个性化视图
        mNotification.contentView = contentView;

        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 指定内容意图
//        mNotification.contentIntent = contentIntent;
        return mNotification;
    }

    protected void updateNotificationProgress(int progress, Notification downloadingNotification) {
        // 更新进度
        RemoteViews contentView = downloadingNotification.contentView;
//        contentView.setTextViewText(R.id.rate, (readSize < 0 ? 0 : readSize) + "b/s   " + msg.arg1 + "%");
        contentView.setTextViewText(R.id.rate, progress + "%");
        contentView.setProgressBar(R.id.progress, 100, progress, false);

        // 更新UI
        notificationManager.notify(downloadingNotification.hashCode(), downloadingNotification);
    }

    protected Notification createDownloadedNotification(String url) {
        CharSequence tickerText2 = "下载完毕";
        long when2 = System.currentTimeMillis();

        PendingIntent contentIntent2 = PendingIntent.getActivity(this, 0, null, 0);
        Notification notification = new Notification.Builder(this)
                .setWhen(when2)
                .setTicker(tickerText2)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("下载完成")
                .setContentText("文件已下载完毕")
                .setContentIntent(contentIntent2)
                .getNotification();
        //放置在"通知形"栏目中
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        Toast.makeText(AbstractVersionUpdateService.this, "下载完成", Toast.LENGTH_SHORT).show();

        return notification;
    }

    protected Notification createCancelledNotification(String url) {
        CharSequence tickerText2 = "下载被取消";
        long when2 = System.currentTimeMillis();

        PendingIntent contentIntent2 = PendingIntent.getActivity(this, 0, null, 0);
        Notification notification = new Notification.Builder(this)
                .setWhen(when2)
                .setTicker(tickerText2)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("下载被取消")
                .setContentText("文件下载已被取消")
                .setContentIntent(contentIntent2)
                .getNotification();
        //放置在"通知形"栏目中
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        Toast.makeText(AbstractVersionUpdateService.this, "下载被取消", Toast.LENGTH_SHORT).show();

        return notification;
    }

    protected void cancelDownload(String url, Notification downloadingNotification) {
        notificationManager.cancel(downloadingNotification.hashCode());
        Notification cancelledNotification = createCancelledNotification(url);
        notificationManager.notify(cancelledNotification.hashCode(), cancelledNotification);
        stopSelf();//停掉服务自身
    }

    protected void afterDownload(File downloadedFile) {
        /*默认打开文件进行安装*/
        this.startActivity(IntentUtils.openFile(downloadedFile));
        stopSelf();//停掉服务自身
    }

    public class ServiceBinder extends Binder {

        public void startDownload(VersionInfo versionInfo, UpdatingCallback callback) {
            AbstractVersionUpdateService.this.startDownload(versionInfo, callback);
        }

        /**
         * 取消下载
         */
        public void cancel() {
            cancelled = true;
        }

        // 是否已被取消
        public boolean isCancelled() {
            return cancelled;
        }
    }

    public static abstract class UpdatingCallback {

        protected void onStartDownloading() {

        }

        protected abstract void onFailure(Exception e) ;

        protected abstract void onProgressUpdate(int progress);

        protected abstract void onDownloaded(File downloadedFile);
    }
}
