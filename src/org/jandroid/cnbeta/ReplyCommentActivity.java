package org.jandroid.cnbeta;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.jandroid.cnbeta.async.CaptchaAsyncTask;
import org.jandroid.cnbeta.async.HasAsync;
import org.jandroid.cnbeta.entity.Comment;
import org.jandroid.cnbeta.entity.Content;
import org.jandroid.common.BaseActivity;
import org.jandroid.common.ToastUtils;
import org.jandroid.common.async.AsyncResult;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class ReplyCommentActivity extends BaseActivity {

    private ProgressBar captchaProgressBar;

    private ImageView captchaImageView;

    private Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_reply);

        comment = (Comment)getIntent().getSerializableExtra("comment");

        captchaImageView = (ImageView)findViewById(R.id.seccode_image);

        captchaImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initCaptcha();
            }
        });

        captchaProgressBar = (ProgressBar)findViewById(R.id.captcha_progessBar);

        TextView replyCommentTitleTextView = (TextView)findViewById(R.id.re_comment);
        replyCommentTitleTextView.setText(comment.getComment());

        TextView cancelTextView = (TextView)findViewById(R.id.cancel);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        TextView sendTextView = (TextView)findViewById(R.id.send);
        sendTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reply();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initCaptcha();
    }

    private void initCaptcha() {
        executeAsyncTaskMultiThreading(new CaptchaAsyncTask() {
            @Override
            protected long getSid() {
                return comment.getSid();
            }

            @Override
            public HasAsync<Bitmap> getAsyncContext() {
                return new HasAsync<Bitmap>() {
                    public CnBetaApplicationContext getCnBetaApplicationContext() {
                        return (CnBetaApplication)ReplyCommentActivity.this.getApplicationContext();
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
                        //TODO: 显示请刷新提示图片
                        captchaImageView.setVisibility(View.VISIBLE);
                        logger.e("获取验证码失败，点击刷新！");
                    }
                };
            }
        });
    }


    public void reply(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
