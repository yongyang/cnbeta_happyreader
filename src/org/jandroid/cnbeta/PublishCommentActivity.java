package org.jandroid.cnbeta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jandroid.cnbeta.async.CaptchaAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.async.PublishCommentAsyncTask;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.common.DateFormatUtils;
import org.jandroid.common.FontUtils;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;
import org.json.simple.JSONObject;

import java.util.Date;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class PublishCommentActivity extends CnBetaThemeActivity {
    protected ProgressBar captchaProgressBar;
    protected ProgressBar postProgressBar;

    protected ImageView captchaImageView;

    protected TextView commentTextView;
    protected TextView captchaTextView;

    TextView cancelTextView;
    TextView sendTextView;

    protected long sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        sid = getIntent().getLongExtra("sid", 0);

        captchaImageView = (ImageView)findViewById(R.id.seccode_image);

        captchaImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initCaptcha();
            }
        });

        captchaProgressBar = (ProgressBar)findViewById(R.id.captcha_progessBar);

        commentTextView = (TextView)findViewById(R.id.comment);
        captchaTextView = (TextView)findViewById(R.id.seccode);

        cancelTextView = (TextView)findViewById(R.id.cancel);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        sendTextView = (TextView)findViewById(R.id.send);
        sendTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                post();
            }
        });

        postProgressBar = (ProgressBar)findViewById(R.id.post_progessBar);
    }



    protected int getLayoutResourceId(){
        return R.layout.comment_publish;
    }

    public long getArticleSid() {
        return sid;
    }

    public long getParentCommentTid() {
        return 0;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();

        CnBetaPreferences pref = ((CnBetaApplicationContext)getApplicationContext()).getCnBetaPreferences();
        FontUtils.updateTextSize(this, commentTextView, R.dimen.listitem_comment_text_size, pref.getFontSizeOffset());
        FontUtils.updateTextSize(this, captchaTextView, R.dimen.listitem_comment_text_size, pref.getFontSizeOffset());
        FontUtils.updateTextSize(this, cancelTextView, R.dimen.listitem_title_text_size, pref.getFontSizeOffset());
        FontUtils.updateTextSize(this, sendTextView, R.dimen.listitem_title_text_size, pref.getFontSizeOffset());

        updateTypeFace(getWindow().getDecorView().getRootView());
        initCaptcha();
    }

    private void initCaptcha() {
        executeAsyncTaskMultiThreading(new CaptchaAsyncTask() {
            @Override
            protected long getSid() {
                return getArticleSid();
            }

            @Override
            public HasAsync<Bitmap> getAsyncContext() {
                return new HasAsync<Bitmap>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplication)getApplicationContext();
                    }

                    public void onProgressShow() {
                        captchaProgressBar.setVisibility(View.VISIBLE);
                        captchaImageView.setVisibility(View.GONE);
                    }

                    public void onProgressDismiss() {
                        captchaProgressBar.setVisibility(View.GONE);
                    }

                    public void onSuccess(AsyncResult<Bitmap> bitmapAsyncResult) {
                        captchaImageView.setImageBitmap(bitmapAsyncResult.getResult());
                        captchaImageView.setVisibility(View.VISIBLE);
                    }

                    public void onFailure(AsyncResult<Bitmap> bitmapAsyncResult) {
                        captchaImageView.setImageResource(R.drawable.default_img);
                        captchaImageView.setVisibility(View.VISIBLE);
                        ToastUtils.showLongToast(PublishCommentActivity.this, "验证码获取失败，点击图片刷新！");
                    }
                };
            }
        });
    }


    protected void post(){

        if(commentTextView.getText().toString().isEmpty() || captchaTextView.getText().toString().isEmpty() ) {
            ToastUtils.showShortToast(this, "请输入评论内容和验证码！");
            return;
        }

        executeAsyncTaskMultiThreading(new PublishCommentAsyncTask() {
            @Override
            protected long getSid() {
                return getArticleSid();
            }

            @Override
            protected long getPid() {
                return getParentCommentTid();
            }

            @Override
            protected String getCommentContent() {
                //签名档
                String signature = ((CnBetaApplicationContext)getApplicationContext()).getCnBetaPreferences().getSignature();
                return signature.isEmpty() ? commentTextView.getText().toString() : commentTextView.getText().toString() + signature;
            }

            @Override
            protected String getSeccode() {
                return captchaTextView.getText().toString();
            }

            @Override
            public HasAsync<JSONObject> getAsyncContext() {
                return new HasAsync<JSONObject>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplication)getApplicationContext();
                    }

                    public void onProgressShow() {
                        postProgressBar.setVisibility(View.VISIBLE);
                    }

                    public void onProgressDismiss() {
                        postProgressBar.setVisibility(View.INVISIBLE);
                    }

                    public void onSuccess(AsyncResult<JSONObject> jsonObjectAsyncResult) {
                        JSONObject resultJSON = jsonObjectAsyncResult.getResult();
                        if(!resultJSON.get("status").equals("success")) {
                            initCaptcha(); // 重新生成验证码
                            String message = ((JSONObject)resultJSON.get("result")).get("message").toString();
                            ToastUtils.showShortToast(PublishCommentActivity.this, message);
                        }
                        else {
                            if(getPid() == 0) {
                                ToastUtils.showShortToast(PublishCommentActivity.this, "感谢评论,编辑正在审核中");
                            }
                            else {
                                ToastUtils.showShortToast(PublishCommentActivity.this, "感谢回复,编辑正在审核中");
                            }
                            Comment newComment = new Comment();
                            newComment.setHostName("cnBeta乐读");
                            newComment.setName("匿名人士");
                            newComment.setComment(getCommentContent());
                            newComment.setSid(getSid());
                            newComment.setDate(DateFormatUtils.getDefault().format(new Date()));
                            //设置结果给 ContentActivity, 新comment 将添加到 comments list 中，并刷新 List
                            Intent intent = new Intent();
                            intent.putExtra("comment", newComment);
                            setResult(0, intent);
                            finish();
                        }
                    }

                    public void onFailure(AsyncResult<JSONObject> jsonObjectAsyncResult) {

                    }
                };
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        // nothing
    }


    @Override
    public int getDarkThemeId() {
        return R.style.Theme_Dialog_cnBeta_Light;
    }

    @Override
    public int getLightThemeId() {
        return R.style.Theme_Dialog_cnBeta_Light;
    }

    @Override
    protected boolean isThemeChanged() {
        return super.isThemeChanged();
    }
}
