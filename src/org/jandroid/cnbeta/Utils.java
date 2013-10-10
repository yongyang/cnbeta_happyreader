package org.jandroid.cnbeta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.TypedValue;
import android.webkit.WebView;
import android.widget.TextView;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.HistoryArticle;
import org.jandroid.cnbeta.loader.HistoryArticleListLoader;
import org.jandroid.cnbeta.service.CheckVersionService;
import org.jandroid.cnbeta.service.VersionUpdateService;
import org.jandroid.common.DateFormatUtils;
import org.jandroid.common.IntentUtils;
import org.jandroid.common.PixelUtils;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.autoupdate.AbstractCheckVersionService;
import org.jandroid.common.autoupdate.AbstractVersionUpdateService;
import org.jandroid.common.autoupdate.VersionInfo;

import java.io.File;
import java.util.Date;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class Utils {

    public static void openContentActivity(final Activity theActivity, final long sid, final String title) {
        Bundle bundle = new Bundle();
        bundle.putLong("sid", sid);
        bundle.putString("title", title);
        Intent intent = IntentUtils.newIntent(theActivity, ContentActivity.class, bundle);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        // write history
        new Thread() {
            @Override
            public void run() {
                HistoryArticle historyArticle = new HistoryArticle();
                historyArticle.setSid(sid);
                historyArticle.setTitle(title);
                historyArticle.setDate(DateFormatUtils.getDefault().format(new Date()));
                try {
                    new HistoryArticleListLoader().writeHistory(((CnBetaApplicationContext) theActivity.getApplicationContext()).getBaseDir(), historyArticle);
                }
                catch (final Exception e) {
                    theActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            ToastUtils.showShortToast(theActivity.getApplicationContext(), e.toString());
                        }
                    });
                }
            }
        }.start();
    }

    public static void openMainActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, MainActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void openTopicActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, TopicActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void openTopicActivity(Activity theActivity, long topicId, String topicName) {
        Intent intent = IntentUtils.newIntent(theActivity, TopicActivity.class);
        intent.putExtra("id", topicId);
        intent.putExtra("name", topicName);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void openTypesActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, TypesActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public static void openMRankActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, MRankActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void openHistoryActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, HistoryActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void openPublishCommentActivityForResult(Activity theActivity, long sid) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("sid", sid);
        Intent intent = IntentUtils.newIntent(theActivity, PublishCommentActivity.class, bundle);
        theActivity.startActivityForResult(intent, 0);
    }

    public static void openReplyCommentActivityForResult(Activity theActivity, Comment comment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("sid", comment.getSid());
        bundle.putSerializable("comment", comment);
        Intent intent = IntentUtils.newIntent(theActivity, ReplyCommentActivity.class, bundle);
        theActivity.startActivityForResult(intent, 0);
    }

    public static void openImageViewerActivity(Activity theActivity, String imgOsrc) {
        Bundle bundle = new Bundle();
        bundle.putString("src", imgOsrc);
        Intent intent = IntentUtils.newIntent(theActivity, ImageViewerActivity.class, bundle);
        theActivity.startActivity(intent);
    }

    public static void openPreferenceActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, CnBetaPreferenceActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void openAboutActivity(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, AboutActivity.class);
        theActivity.startActivity(intent);
        theActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void bindVersionCheckService(Activity theActivity, ServiceConnection serviceConnection) {
        Intent intent = IntentUtils.newIntent(theActivity, CheckVersionService.class);
        theActivity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public static void startVersionUpdateService(Activity theActivity) {
        Intent intent = IntentUtils.newIntent(theActivity, VersionUpdateService.class);
        theActivity.startService(intent);
    }

    public static void bindVersionUpdateService(Activity theActivity, ServiceConnection serviceConnection) {
        Intent intent = IntentUtils.newIntent(theActivity, VersionUpdateService.class);
        theActivity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public static void openVersionCheckDialog(final Activity theActivity) {
        new AlertDialog.Builder(theActivity)
                .setTitle("你要检查新版本吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.bindVersionCheckService(theActivity, new ServiceConnection() {
                            public void onServiceConnected(ComponentName name, IBinder service) {
                                AbstractCheckVersionService.CheckVersionBinder binder = (AbstractCheckVersionService.CheckVersionBinder) service;
                                binder.checkVersion(new AbstractCheckVersionService.CheckingCallback() {
                                    public void onStartChecking(String url) {
                                        ToastUtils.showShortToast(theActivity, url);
                                    }

                                    public void onFailure(Exception e) {
                                        ToastUtils.showShortToast(theActivity, "版本检查失败");
                                    }

                                    public void onNewVersion(VersionInfo versionInfo) {
                                        openVersionUpdateDialog(theActivity, versionInfo);
                                    }

                                    public void onSameVersion(VersionInfo versionInfo) {
                                        ToastUtils.showShortToast(theActivity, "已经是最新版本");
                                    }

                                    public void onOldVersion(VersionInfo versionInfo) {
                                        ToastUtils.showShortToast(theActivity, "你很潮，已经是最新版本");
                                    }
                                });
                            }

                            public void onServiceDisconnected(ComponentName name) {

                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    public static void openVersionUpdateDialog(final Activity theActivity, final VersionInfo newVersionInfo) {
        new AlertDialog.Builder(theActivity)
                .setTitle("发现新版本 " + newVersionInfo.getVersion() + " ，要立即更新吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.startVersionUpdateService(theActivity);
                        Utils.bindVersionUpdateService(theActivity, new ServiceConnection() {
                            public void onServiceConnected(ComponentName name, IBinder service) {
                                AbstractVersionUpdateService.ServiceBinder binder = (AbstractVersionUpdateService.ServiceBinder)service;
                                binder.startDownload(newVersionInfo, new AbstractVersionUpdateService.UpdatingCallback() {
                                    @Override
                                    protected void onProgressUpdate(int progress) {
//                                        ToastUtils.showShortToast(theActivity, "onProgressUpdate: " + progress);
                                    }

                                    @Override
                                    protected void onFailure(Exception e) {
                                        ToastUtils.showShortToast(theActivity, "onFailure: " + e.toString());
                                    }

                                    @Override
                                    protected void onDownloaded(VersionInfo versionInfo, File downloadedFile) {
                                        ToastUtils.showShortToast(theActivity, "onDownloaded: " + downloadedFile.toString());
                                    }

                                    @Override
                                    protected void onCancelled(VersionInfo versionInfo) {
                                        ToastUtils.showShortToast(theActivity, "onCancelled");
                                    }
                                });
                            }

                            public void onServiceDisconnected(ComponentName name) {

                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }



    public static void updateTextSize(Activity theActivity, TextView textView, int dimId) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, PixelUtils.pixelsToSp(theActivity, theActivity.getResources().getDimension(dimId)) + ((CnBetaApplicationContext)theActivity.getApplicationContext()).getCnBetaPreferences().getFontSizeIncrement());
    }

    public static void updateTextSize(Activity theActivity, WebView webView, int dimId) {
        webView.getSettings().setDefaultFontSize((int)PixelUtils.pixelsToSp(theActivity,theActivity.getResources().getDimension(dimId)) + ((CnBetaApplicationContext)theActivity.getApplicationContext()).getCnBetaPreferences().getFontSizeIncrement());
        webView.getSettings().setDefaultFixedFontSize((int)PixelUtils.pixelsToSp(theActivity,theActivity.getResources().getDimension(dimId)) + ((CnBetaApplicationContext)theActivity.getApplicationContext()).getCnBetaPreferences().getFontSizeIncrement());
    }


}
